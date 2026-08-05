package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.*;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import jnr.ffi.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static com.github.jnrwinfspteam.jnrwinfsp.api.CleanupFlags.DELETE;
import static com.github.jnrwinfspteam.jnrwinfsp.api.CreateOptions.FILE_DIRECTORY_FILE;

@Slf4j
@Component
@RequiredArgsConstructor
public class WinFspFileSystem extends WinFspStubFS {

    private static final String ROOT_SECURITY_DESCRIPTOR = "O:BAG:BAD:PAR(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)(A;OICI;FA;;;WD)";

    private static final long MAX_FILE_NODES = 10240;

    private static final long MAX_FILE_SIZE = 16 * 1024 * 1024;

    private final RandomAccessService randomAccessService;

    private final ReadDirectoryUseCase readDirectoryUseCase;

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final DownloadFileUseCase downloadFileUseCase;

    private final UploadFileUseCase uploadFileUseCase;

    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    private final FileInfoMapper fileInfoMapper;

    private final AtomicLong fileHandle;

    private final Object cacheLock;

    private final  Map<String, FileInfo> cacheFileInfo;

    private final Map<String, byte[]> securityDescriptors;

    @Autowired
    public WinFspFileSystem(
            RandomAccessService randomAccessService,
            ReadDirectoryUseCase readDirectoryUseCase,
            CreateDirectoryUseCase createDirectoryUseCase,
            DownloadFileUseCase downloadFileUseCase,
            UploadFileUseCase uploadFileUseCase,
            FileInfoMapper fileInfoMapper,
            GetStorageResourceUseCase getStorageResourceUseCase,
            DeleteStorageResourceUseCase deleteStorageResourceUseCase
    ) throws NTStatusException {
        this.randomAccessService = randomAccessService;
        this.readDirectoryUseCase = readDirectoryUseCase;
        this.createDirectoryUseCase = createDirectoryUseCase;
        this.downloadFileUseCase = downloadFileUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.fileInfoMapper = fileInfoMapper;
        this.getStorageResourceUseCase = getStorageResourceUseCase;
        this.deleteStorageResourceUseCase = deleteStorageResourceUseCase;

        fileHandle = new AtomicLong(0);
        cacheLock = new Object();
        cacheFileInfo = new ConcurrentHashMap<>();
        securityDescriptors = new ConcurrentHashMap<>();
    }

    @Override
    public VolumeInfo getVolumeInfo() throws NTStatusException {
        log.info("GET VOLUME INFO");

        long totalSize = MAX_FILE_NODES * MAX_FILE_SIZE;
        FileInfo fileInfo = getFileInfo("user1");

        synchronized (cacheLock) {
            long freeSize = totalSize - fileInfo.getFileSize();
            String volumeLabel = fileInfo.getFileName();

            return new VolumeInfo(totalSize, freeSize, volumeLabel);
        }
    }

    @Override
    public VolumeInfo setVolumeLabel(String volumeLabel) throws NTStatusException {
        log.info("SET VOLUME INFO - volumeLabel={}", volumeLabel);

        long totalSize = MAX_FILE_NODES * MAX_FILE_SIZE;

        synchronized (cacheLock) {
            long freeSize = totalSize - getFileInfo("user1").getFileSize();

            return new VolumeInfo(totalSize, freeSize, volumeLabel);
        }
    }

    @Override
    public Optional<SecurityResult> getSecurityByName(String fileName) throws NTStatusException {
        log.info("GET SECURITY BY NAME - fileName={}", fileName);

        if (fileName.contains(".inf") || fileName.contains(".ini")) {
            return Optional.empty();
        }

        FileInfo fileInfo = getFileInfo(fileName);

        Set<FileAttributes> fileAttributes = fileInfo != null
                ? fileInfo.getFileAttributes()
                : Set.of(FileAttributes.FILE_ATTRIBUTE_NORMAL);

        byte[] securityDescriptor = securityDescriptors.containsKey(fileName)
                ? securityDescriptors.get(fileName)
                : SecurityDescriptorHandler.securityDescriptorToBytes(ROOT_SECURITY_DESCRIPTOR);

        return Optional.of(new SecurityResult(securityDescriptor, fileAttributes));
    }

    @Override
    public OpenResult create(String fileName, Set<CreateOptions> createOptions, int grantedAccess, Set<FileAttributes> fileAttributes, byte[] securityDescriptor, long allocationSize, ReparsePoint reparsePoint) throws NTStatusException {
        log.info(
                "CREATE - fileName={}, createOptions={}, grantedAccess={}, fileAttributes={}, securityDescriptor={}, allocationSize={}, reparsePoint={}",
                fileName, createOptions, grantedAccess, fileAttributes, securityDescriptor, allocationSize, reparsePoint
        );

        synchronized (cacheLock) {
            if (createOptions.contains(FILE_DIRECTORY_FILE)) {
                return new OpenResult(
                        fileHandle.incrementAndGet(),
                        fileInfoMapper.toFileInfo(createDirectoryUseCase.createDirectory(fileName))
                );
            }

            return new OpenResult(fileHandle.incrementAndGet(), getFileInfo(fileName));
        }
    }

    @Override
    public OpenResult open(String fileName, Set<CreateOptions> createOptions, int grantedAccess) throws NTStatusException {
        log.info("OPEN - fileName={}, createOptions={}, grantedAccess={}", fileName, createOptions, grantedAccess);

        synchronized (cacheLock) {
            if (!createOptions.contains(FILE_DIRECTORY_FILE)) {
                randomAccessService.getTempFile(fileName);
            }

            return new OpenResult(fileHandle.incrementAndGet(), getFileInfo(fileName));
        }
    }

    @Override
    public FileInfo overwrite(OpenContext ctx, Set<FileAttributes> fileAttributes, boolean replaceFileAttributes, long allocationSize) throws NTStatusException {
        log.info("OVERWRITE - ctx={}, fileAttributes={}, replaceFileAttributes={}, allocationSize={}", ctx, fileAttributes, replaceFileAttributes, allocationSize);

        return getFileInfo(ctx);
    }

    @Override
    public void cleanup(OpenContext ctx, Set<CleanupFlags> flags) {
        log.info("CLEANUP - ctx={}, flags={}", ctx, flags);

        synchronized (cacheLock) {
            byte[] bytes = randomAccessService.readAll(ctx.getPath());
            uploadFileUseCase.uploadFile(ctx.getPath(), bytes, "application/octet-stream"); // TODO

            if (flags.contains(DELETE)) {
                deleteStorageResourceUseCase.deleteStorageResource(ctx.getPath());
            }
        }
    }

    @Override
    public void close(OpenContext ctx) {
        log.info("CLOSE - ctx={}", ctx);

        randomAccessService.close(ctx.getPath());
    }

    @Override
    public long read(OpenContext ctx, Pointer pBuffer, long offset, int length) throws NTStatusException {
        log.info("READ - ctx={}, pBuffer={}, offset={}, length={}", ctx, pBuffer, offset, length);

        synchronized (cacheLock) {
            byte[] bytes = downloadFileUseCase.downloadFile(ctx.getPath(), offset, length);

            pBuffer.put(0, bytes, 0, bytes.length);

            return bytes.length;
        }
    }

    @Override
    public WriteResult write(OpenContext ctx, Pointer pBuffer, long offset, int length, boolean c, boolean constrainedIo) throws NTStatusException {
        log.info("WRITE - ctx={}, pBuffer={}, offset={}, length={}, c={}, constrainedIo={}", ctx, pBuffer, offset, length, c, constrainedIo);

        byte[] bytes = new byte[length];

        synchronized (cacheLock) {
            FileInfo fileInfo = getFileInfo(ctx.getPath());

            pBuffer.get(0, bytes, 0, bytes.length);
            randomAccessService.write(ctx.getPath(), bytes, offset);

            return new WriteResult(length, fileInfo);
        }
    }

    @Override
    public FileInfo flush(OpenContext ctx) throws NTStatusException {
        log.info("FLUSH - ctx={}", ctx);

        synchronized (cacheLock) {
            return getFileInfo(ctx.getPath());
        }
    }

    @Override
    public FileInfo getFileInfo(OpenContext ctx) throws NTStatusException {
        log.info("GET FILE INFO - ctx={}", ctx);

        synchronized (cacheLock) {
            return getFileInfo(ctx.getPath());
        }
    }

    @Override
    public FileInfo setBasicInfo(OpenContext ctx, Set<FileAttributes> fileAttributes, WinSysTime creationTime, WinSysTime lastAccessTime, WinSysTime lastWriteTime, WinSysTime changeTime) throws NTStatusException {
        log.info("SET BASIC INFO - ctx={}, fileAttributes={}, creationTime={}, lastAccessTime={}, lastWriteTime={}, changeTime={}", ctx, fileAttributes, creationTime, lastAccessTime, lastWriteTime, changeTime);

        synchronized (cacheLock) {
            return getFileInfo(ctx.getPath());
        }
    }

    @Override
    public FileInfo setFileSize(OpenContext ctx, long newSize, boolean setAllocationSize) throws NTStatusException {
        log.info("SET FILE SIZE - ctx={}, newSize={}, setAllocationSize={}", ctx, newSize, setAllocationSize);

        synchronized (cacheLock) {
            FileInfo fileInfo = getFileInfo(ctx.getPath());

            if (setAllocationSize) {
                fileInfo.setFileSize(newSize);
            }

            return fileInfo;
        }
    }

    @Override
    public void canDelete(OpenContext ctx) throws NTStatusException {
        log.info("CAN DELETE - ctx={}", ctx);

        if (isRootDirectory(ctx.getPath())) {
            throw new NTStatusException(0xC0000022); // STATUS_ACCESS_DENIED
        }
    }

    @Override
    public void rename(OpenContext ctx, String oldFileName, String newFileName, boolean replaceIfExists) throws NTStatusException {
        log.info("RENAME - ctx={}, oldFileName={}, newFileName={}, replaceIfExists={}", ctx, oldFileName, newFileName, replaceIfExists);
    }

    @Override
    public byte[] getSecurity(OpenContext ctx) throws NTStatusException {
        log.info("GET SECURITY - ctx={}", ctx);

        return securityDescriptors.containsKey(ctx.getPath())
                ? securityDescriptors.get(ctx.getPath())
                : SecurityDescriptorHandler.securityDescriptorToBytes(ROOT_SECURITY_DESCRIPTOR);
    }

    @Override
    public void setSecurity(OpenContext ctx, byte[] securityDescriptor) throws NTStatusException {
        log.info("SET SECURITY - ctx={}, securityDescriptor={}", ctx, securityDescriptor);

        securityDescriptors.put(ctx.getPath(), securityDescriptor);
    }

    @Override
    public void readDirectory(OpenContext ctx, String pattern, String marker, Predicate<FileInfo> consumer) throws NTStatusException {
        log.info("READ DIRECTORY - ctx={}, pattern={}, marker={}, consumer={}", ctx, pattern, marker, consumer);

        synchronized (cacheLock) {
            readDirectoryUseCase.readDirectory(ctx.getPath()).children().stream()
                    .map(fileInfoMapper::toFileInfo)
//                    .filter(fileInfo -> pattern.isBlank() || fileInfo.getNormalizedName().startsWith(pattern))
//                    .filter(fileInfo -> marker.isBlank() || fileInfo.getNormalizedName().compareTo(marker) > 0)
                    .forEach(consumer::test);
        }
    }

    @Override
    public FileInfo getDirInfoByName(OpenContext parentDirCtx, String fileName) throws NTStatusException {
        log.info("GET DIR INFO BY NAME - parentDirCtx={}, fileName={}", parentDirCtx, fileName);

        synchronized (cacheLock) {
            return getFileInfo(resolveChildPath(parentDirCtx.getPath(), fileName));
        }
    }

    @Override
    public byte[] getReparsePointData(OpenContext ctx) throws NTStatusException {
        log.info("GET REPARSE POINT DATA - ctx={}", ctx);

//        throw new NTStatusException(0xC0000275); // STATUS_NOT_A_REPARSE_POINT
        return new byte[0];
    }

    @Override
    public void setReparsePoint(OpenContext ctx, byte[] reparseData, int reparseTag) throws NTStatusException {
        log.info("SET REPARSE POINT - ctx={}, reparseData={}, reparseTag={}", ctx, reparseData, reparseTag);

//        throw new NTStatusException(0xC0000275); // STATUS_NOT_A_REPARSE_POINT
    }

    @Override
    public void deleteReparsePoint(OpenContext ctx) throws NTStatusException {
        log.info("DELETE REPARSE POINT - ctx={}", ctx);

//        throw new NTStatusException(0xC0000275); // STATUS_NOT_A_REPARSE_POINT
    }

    private FileInfo getFileInfo(String path) throws NTStatusException {
//        FileInfo fileInfo = cacheFileInfo.get(path);
//
//        if (fileInfo != null) {
//            return fileInfo;
//        }

        String filename = path.substring(Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\')) + 1);
        if (filename.equalsIgnoreCase("desktop.ini")) {
            return new FileInfo(filename);
        }

        StorageResource storageResource = getStorageResourceUseCase.getStorageResource(path);

        if (storageResource == null) {
            throw new NTStatusException(0xC0000034); // STATUS_OBJECT_NAME_NOT_FOUND
        }

        FileInfo fileInfo = fileInfoMapper.toFileInfo(storageResource);
        cacheFileInfo.put(path, fileInfo);

        return fileInfo;
    }

    private boolean isRootDirectory(String path) {
        return path == null || path.isBlank() || "\\".equals(path) || "/".equals(path);
    }

    /**
     * WinFsp passes a child name to getDirInfoByName, while the directory is
     * available from parentDirCtx. Other callbacks pass paths rooted at the
     * mounted filesystem. Keep that distinction at this boundary.
     */
    private String resolveChildPath(String parentPath, String childPath) {
        String normalizedChildPath = childPath.replace('/', '\\');

        // Be defensive if a caller already provides a mount-rooted path.
        if (normalizedChildPath.startsWith("\\")) {
            return normalizedChildPath;
        }

        String normalizedParentPath = parentPath.replace('/', '\\');
        if (!normalizedParentPath.startsWith("\\")) {
            normalizedParentPath = "\\" + normalizedParentPath;
        }

        if ("\\".equals(normalizedParentPath)) {
            return normalizedParentPath + normalizedChildPath;
        }

        return normalizedParentPath.endsWith("\\")
                ? normalizedParentPath + normalizedChildPath
                : normalizedParentPath + "\\" + normalizedChildPath;
    }

}
