package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.*;
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

    private static final Set<String> UNSUPPORTED_FILE_NAMES = Set.of(
            "\\desktop.ini",
            "\\autorun.inf"
    );

    private final RandomAccessService randomAccessService;

    private final ReadDirectoryUseCase readDirectoryUseCase;

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final DownloadFileUseCase downloadFileUseCase;

    private final UploadFileUseCase uploadFileUseCase;

    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final MoveStorageResourceUseCase moveStorageResourceUseCase;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    private final FileInfoMapper fileInfoMapper;

    private final AtomicLong fileHandle;

    private final Object cacheLock;

    private final  Map<String, FileInfo> filesByPath;

    private final Map<String, byte[]> securityDescriptors;

    private final Map<Long, String> pathsByHandle;

    @Autowired
    public WinFspFileSystem(
            RandomAccessService randomAccessService,
            ReadDirectoryUseCase readDirectoryUseCase,
            CreateDirectoryUseCase createDirectoryUseCase,
            DownloadFileUseCase downloadFileUseCase,
            UploadFileUseCase uploadFileUseCase,
            FileInfoMapper fileInfoMapper,
            GetStorageResourceUseCase getStorageResourceUseCase,
            DeleteStorageResourceUseCase deleteStorageResourceUseCase,
            MoveStorageResourceUseCase moveStorageResourceUseCase
    ) {
        this.randomAccessService = randomAccessService;
        this.readDirectoryUseCase = readDirectoryUseCase;
        this.createDirectoryUseCase = createDirectoryUseCase;
        this.downloadFileUseCase = downloadFileUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.fileInfoMapper = fileInfoMapper;
        this.getStorageResourceUseCase = getStorageResourceUseCase;
        this.deleteStorageResourceUseCase = deleteStorageResourceUseCase;
        this.moveStorageResourceUseCase = moveStorageResourceUseCase;

        fileHandle = new AtomicLong(0);
        cacheLock = new Object();
        filesByPath = new ConcurrentHashMap<>();
        securityDescriptors = new ConcurrentHashMap<>();
        pathsByHandle = new ConcurrentHashMap<>();
    }

    @Override
    public VolumeInfo getVolumeInfo() throws NTStatusException {
        log.info("GET VOLUME INFO");

        long totalSize = MAX_FILE_NODES * MAX_FILE_SIZE;
        FileInfo fileInfo = getFileInfoByPath("\\");

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
            long freeSize = totalSize - getFileInfoByPath("\\").getFileSize();

            return new VolumeInfo(totalSize, freeSize, volumeLabel);
        }
    }

    @Override
    public Optional<SecurityResult> getSecurityByName(String fileName) throws NTStatusException {
        log.info("GET SECURITY BY NAME - fileName={}", fileName);

        synchronized (cacheLock) {
            if (UNSUPPORTED_FILE_NAMES.stream().anyMatch(fileName.toLowerCase()::contains)) {
                return Optional.of(
                        new SecurityResult(
                                SecurityDescriptorHandler.securityDescriptorToBytes(ROOT_SECURITY_DESCRIPTOR),
                                Set.of(FileAttributes.FILE_ATTRIBUTE_NORMAL)
                        )
                );
            }

            FileInfo fileInfo = getFileInfoByPath(fileName);

            byte[] securityDescriptor = securityDescriptors.getOrDefault(
                    fileName,
                    SecurityDescriptorHandler.securityDescriptorToBytes(ROOT_SECURITY_DESCRIPTOR)
            );

            return Optional.of(new SecurityResult(securityDescriptor, fileInfo.getFileAttributes()));
        }
    }

    @Override
    public OpenResult create(String fileName, Set<CreateOptions> createOptions, int grantedAccess, Set<FileAttributes> fileAttributes, byte[] securityDescriptor, long allocationSize, ReparsePoint reparsePoint) throws NTStatusException {
        log.info(
                "CREATE - fileName={}, createOptions={}, grantedAccess={}, fileAttributes={}, securityDescriptor={}, allocationSize={}, reparsePoint={}",
                fileName, createOptions, grantedAccess, fileAttributes, securityDescriptor, allocationSize, reparsePoint
        );

        synchronized (cacheLock) {
            if (UNSUPPORTED_FILE_NAMES.stream().anyMatch(fileName.toLowerCase()::contains)) {
                return new OpenResult(fileHandle.incrementAndGet(), new FileInfo(fileName));
            }

            if (createOptions.contains(FILE_DIRECTORY_FILE)) {
                return createOpenResult(fileName, fileInfoMapper.toFileInfo(createDirectoryUseCase.createDirectory(fileName)));
            }

            uploadFileUseCase.uploadFile(fileName, new byte[0], "application/octet-stream"); // TODO

            return createOpenResult(fileName, getFileInfoByPath(fileName));
        }
    }

    @Override
    public OpenResult open(String fileName, Set<CreateOptions> createOptions, int grantedAccess) throws NTStatusException {
        log.info("OPEN - fileName={}, createOptions={}, grantedAccess={}", fileName, createOptions, grantedAccess);

        synchronized (cacheLock) {
            if (!createOptions.contains(FILE_DIRECTORY_FILE)) {
                randomAccessService.getTempFile(fileName);
            }

            return createOpenResult(fileName, getFileInfoByPath(fileName));
        }
    }

    @Override
    public FileInfo overwrite(OpenContext ctx, Set<FileAttributes> fileAttributes, boolean replaceFileAttributes, long allocationSize) throws NTStatusException {
        log.info(
                "OVERWRITE - ctx={}, fileAttributes={}, replaceFileAttributes={}, allocationSize={}",
                ctx, fileAttributes, replaceFileAttributes, allocationSize
        );

        synchronized (cacheLock) {
            return getFileInfo(ctx);
        }
    }

    @Override
    public void cleanup(OpenContext ctx, Set<CleanupFlags> flags) {
        log.info("CLEANUP - ctx={}, flags={}", ctx, flags);

        synchronized (cacheLock) {
            try {
                String path = getPathByHandle(ctx.getFileHandle());

                if (flags.contains(DELETE)) {
                    deleteStorageResourceUseCase.deleteStorageResource(path);
                    return;
                }

                byte[] bytes = randomAccessService.readAll(path);
                uploadFileUseCase.uploadFile(path, bytes, "application/octet-stream"); // TODO
            } catch (NTStatusException e) {
                log.error("Failed to get path by handle: {}", ctx.getFileHandle(), e);
            }
        }
    }

    @Override
    public void close(OpenContext ctx) {
        log.info("CLOSE - ctx={}", ctx);

        synchronized (cacheLock) {
            long handle = ctx.getFileHandle();

            try {
                randomAccessService.close(getPathByHandle(handle));
                pathsByHandle.remove(handle);
            } catch (NTStatusException e) {
                log.error("Failed to get path by handle: {}", ctx.getFileHandle(), e);
            }
        }
    }

    @Override
    public long read(OpenContext ctx, Pointer pBuffer, long offset, int length) throws NTStatusException {
        log.info("READ - ctx={}, pBuffer={}, offset={}, length={}", ctx, pBuffer, offset, length);

        synchronized (cacheLock) {
            String path = getPathByHandle(ctx.getFileHandle());
            long fileSize = getFileInfo(ctx).getFileSize();

            if (offset >= fileSize) {
                return 0;
            }

            int bytesToRead = (int) Math.min(length, fileSize - offset);
            byte[] bytes = downloadFileUseCase.downloadFile(path, offset, bytesToRead);
            int bytesRead = Math.min(bytes.length, bytesToRead);
            pBuffer.put(0, bytes, 0, bytesRead);

            return bytesRead;
        }
    }

    @Override
    public WriteResult write(OpenContext ctx, Pointer pBuffer, long offset, int length, boolean c, boolean constrainedIo) throws NTStatusException {
        log.info("WRITE - ctx={}, pBuffer={}, offset={}, length={}, c={}, constrainedIo={}", ctx, pBuffer, offset, length, c, constrainedIo);

        byte[] bytes = new byte[length];

        synchronized (cacheLock) {
            String path = getPathByHandle(ctx.getFileHandle());

            pBuffer.get(0, bytes, 0, bytes.length);
            randomAccessService.write(path, bytes, offset);

            return new WriteResult(length, getFileInfoByPath(path));
        }
    }

    @Override
    public FileInfo flush(OpenContext ctx) throws NTStatusException {
        log.info("FLUSH - ctx={}", ctx);

        synchronized (cacheLock) {
            return getFileInfo(ctx);
        }
    }

    @Override
    public FileInfo getFileInfo(OpenContext ctx) throws NTStatusException {
        log.info("GET FILE INFO - ctx={}", ctx);

        synchronized (cacheLock) {
            return getFileInfoByPath(getPathByHandle(ctx.getFileHandle()));
        }
    }

    @Override
    public FileInfo setBasicInfo(OpenContext ctx, Set<FileAttributes> fileAttributes, WinSysTime creationTime, WinSysTime lastAccessTime, WinSysTime lastWriteTime, WinSysTime changeTime) throws NTStatusException {
        log.info("SET BASIC INFO - ctx={}, fileAttributes={}, creationTime={}, lastAccessTime={}, lastWriteTime={}, changeTime={}", ctx, fileAttributes, creationTime, lastAccessTime, lastWriteTime, changeTime);

        synchronized (cacheLock) {
            return getFileInfo(ctx);
        }
    }

    @Override
    public FileInfo setFileSize(OpenContext ctx, long newSize, boolean setAllocationSize) throws NTStatusException {
        log.info("SET FILE SIZE - ctx={}, newSize={}, setAllocationSize={}", ctx, newSize, setAllocationSize);

        synchronized (cacheLock) {
            FileInfo fileInfo = getFileInfo(ctx);

            if (!setAllocationSize) {
                fileInfo.setFileSize(newSize);
            }

            return fileInfo;
        }
    }

    @Override
    public void canDelete(OpenContext ctx) throws NTStatusException {
        log.info("CAN DELETE - ctx={}", ctx);

        if (isRootDirectory(getPathByHandle(ctx.getFileHandle()))) {
            throw new NTStatusException(0xC0000022); // STATUS_ACCESS_DENIED
        }
    }

    @Override
    public void rename(OpenContext ctx, String oldFileName, String newFileName, boolean replaceIfExists) throws NTStatusException {
        log.info(
                "RENAME - ctx={}, oldFileName={}, newFileName={}, replaceIfExists={}",
                ctx, oldFileName, newFileName, replaceIfExists
        );

        synchronized (cacheLock) {
            moveStorageResourceUseCase.moveStorageResource(oldFileName, newFileName, replaceIfExists);

            FileInfo fileInfo = getFileInfo(ctx);
            fileInfo.setNormalizedName(newFileName);

            filesByPath.remove(getPathByHandle(ctx.getFileHandle()));
            filesByPath.put(newFileName, fileInfo);

            long handle = ctx.getFileHandle();
            pathsByHandle.remove(handle);
            pathsByHandle.put(handle, newFileName);
        }
    }

    @Override
    public byte[] getSecurity(OpenContext ctx) throws NTStatusException {
        log.info("GET SECURITY - ctx={}", ctx);

        String path = getPathByHandle(ctx.getFileHandle());
        return securityDescriptors.containsKey(path)
                ? securityDescriptors.get(path)
                : SecurityDescriptorHandler.securityDescriptorToBytes(ROOT_SECURITY_DESCRIPTOR);
    }

    @Override
    public void setSecurity(OpenContext ctx, byte[] securityDescriptor) throws NTStatusException {
        log.info("SET SECURITY - ctx={}, securityDescriptor={}", ctx, securityDescriptor);

        securityDescriptors.put(getPathByHandle(ctx.getFileHandle()), securityDescriptor);
    }

    @Override
    public void readDirectory(OpenContext ctx, String pattern, String marker, Predicate<FileInfo> consumer) throws NTStatusException {
        log.info("READ DIRECTORY - ctx={}, pattern={}, marker={}, consumer={}", ctx, pattern, marker, consumer);

        synchronized (cacheLock) {
            String directoryPath = getPathByHandle(ctx.getFileHandle());

            readDirectoryUseCase.readDirectory(directoryPath)
                    .children()
                    .stream()
                    .map(fileInfoMapper::toFileInfo)
//                    .filter(fileInfo -> pattern.isBlank() || fileInfo.getNormalizedName().startsWith(pattern))
//                    .filter(fileInfo -> marker.isBlank() || fileInfo.getNormalizedName().compareTo(marker) > 0)
                    .forEach(fileInfo -> {
                        String path = directoryPath.concat("\\").concat(fileInfo.getFileName());

                        if (!filesByPath.containsKey(path)) {
                            filesByPath.put(path, fileInfo);
                        }

                        consumer.test(fileInfo);
                    });
        }
    }

    @Override
    public FileInfo getDirInfoByName(OpenContext parentDirCtx, String fileName) throws NTStatusException {
        log.info("GET DIR INFO BY NAME - parentDirCtx={}, fileName={}", parentDirCtx, fileName);

        synchronized (cacheLock) {
            String path = getPathByHandle(parentDirCtx.getFileHandle()).concat("\\").concat(fileName);

            return getFileInfoByPath(path);
        }
    }

    @Override
    public byte[] getReparsePointData(OpenContext ctx) {
        log.info("GET REPARSE POINT DATA - ctx={}", ctx);

        return new byte[0];
    }

    @Override
    public void setReparsePoint(OpenContext ctx, byte[] reparseData, int reparseTag) {
        log.info("SET REPARSE POINT - ctx={}, reparseData={}, reparseTag={}", ctx, reparseData, reparseTag);
    }

    @Override
    public void deleteReparsePoint(OpenContext ctx) {
        log.info("DELETE REPARSE POINT - ctx={}", ctx);
    }

    private String getPathByHandle(long handle) throws NTStatusException {
        if (pathsByHandle.containsKey(handle)) {
            return pathsByHandle.get(handle);
        }

        throw new NTStatusException(0xC0000034); // STATUS_OBJECT_NAME_NOT_FOUND
    }

    private FileInfo getFileInfoByPath(String path) throws NTStatusException {
        if (UNSUPPORTED_FILE_NAMES.stream().anyMatch(path.toLowerCase()::contains)) {
            return new FileInfo(path);
        }

        FileInfo cachedFileInfo = filesByPath.get(path);

        if (cachedFileInfo != null) {
            return cachedFileInfo;
        }

        return getStorageResourceUseCase.getStorageResource(path)
                .map(storageResource -> {
                    FileInfo fileInfo = fileInfoMapper.toFileInfo(storageResource);
                    filesByPath.put(path, fileInfo);

                    return fileInfo;
                })
                .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
    }

    private OpenResult  createOpenResult(String path, FileInfo fileInfo) {
        long handle = fileHandle.incrementAndGet();
        pathsByHandle.put(handle, path);

        return new OpenResult(handle, fileInfo);
    }

    private boolean isRootDirectory(String path) {
        return path == null || path.isBlank() || "\\".equals(path) || "/".equals(path);
    }

}
