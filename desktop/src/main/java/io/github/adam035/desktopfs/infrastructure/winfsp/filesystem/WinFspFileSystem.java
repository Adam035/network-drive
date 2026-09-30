package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.dto.VolumeResult;
import io.github.adam035.desktopfs.application.port.VolumePort;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.service.*;
import jnr.ffi.Pointer;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class WinFspFileSystem extends WinFspStubFS {

    private final OpenFileStateRegistry openFileStateRegistry;

    private final VolumeResult volumeResult;

    private final Object cacheLock;

    private final CleanupService cleanupService;

    private final CloseService closeService;

    private final CreateService createService;

    private final FlushService flushService;

    private final OpenService openService;

    private final OverwriteService overwriteService;

    private final ReadDirectoryService readDirectoryService;

    private final ReadService readService;

    private final RenameService renameService;

    private final SecurityService securityService;

    private final WriteService writeService;

    public WinFspFileSystem(
            String volumeLabel,
            VolumePort volumePort,
            OpenFileStateRegistry openFileStateRegistry,
            CleanupService cleanupService,
            CloseService closeService,
            CreateService createService,
            FlushService flushService,
            OpenService openService,
            OverwriteService overwriteService,
            ReadDirectoryService readDirectoryService,
            ReadService readService,
            RenameService renameService,
            SecurityService securityService,
            WriteService writeService

    ) {
        this.openFileStateRegistry = openFileStateRegistry;
        this.cleanupService = cleanupService;
        this.closeService = closeService;
        this.createService = createService;
        this.flushService = flushService;
        this.openService = openService;
        this.overwriteService = overwriteService;
        this.readDirectoryService = readDirectoryService;
        this.readService = readService;
        this.renameService = renameService;
        this.securityService = securityService;
        this.writeService = writeService;

        volumeResult = volumePort.getVolume(volumeLabel)
                .orElseThrow(() -> new RuntimeException("Volume not found: " + volumeLabel)); // TODO

        cacheLock = openFileStateRegistry;
    }

    @Override
    public VolumeInfo getVolumeInfo() throws NTStatusException {
        log.info("GET VOLUME INFO");

        synchronized (cacheLock) {
            return new VolumeInfo(volumeResult.totalSize(), volumeResult.freeSize(), volumeResult.volumeLabel());
        }
    }

    @Override
    public VolumeInfo setVolumeLabel(String volumeLabel) throws NTStatusException {
        log.info("SET VOLUME INFO - volumeLabel={}", volumeLabel);

        throw new NTStatusException(0xC00000BB); // STATUS_NOT_SUPPORTED
    }

    @Override
    public Optional<SecurityResult> getSecurityByName(String fileName) throws NTStatusException {
        log.info("GET SECURITY BY NAME - fileName={}", fileName);

        synchronized (cacheLock) {

            return securityService.getSecurityByName(fileName, volumeResult.volumeLabel());
        }
    }

    @Override
    public OpenResult create(String fileName, Set<CreateOptions> createOptions, int grantedAccess, Set<FileAttributes> fileAttributes, byte[] securityDescriptor, long allocationSize, ReparsePoint reparsePoint) throws NTStatusException {
        log.info(
                "CREATE - fileName={}, createOptions={}, grantedAccess={}, fileAttributes={}, securityDescriptor={}, allocationSize={}, reparsePoint={}",
                fileName, createOptions, grantedAccess, fileAttributes, securityDescriptor, allocationSize, reparsePoint
        );

        synchronized (cacheLock) {
            return createService.create(fileName, createOptions, openFileStateRegistry.nextHandle(), volumeResult.volumeLabel(), fileAttributes, securityDescriptor, allocationSize);
        }
    }

    @Override
    public OpenResult open(String fileName, Set<CreateOptions> createOptions, int grantedAccess) throws NTStatusException {
        log.info("OPEN - fileName={}, createOptions={}, grantedAccess={}", fileName, createOptions, grantedAccess);

        synchronized (cacheLock) {

            return openService.open(openFileStateRegistry.nextHandle(), fileName, createOptions, volumeResult.volumeLabel());
        }
    }

    @Override
    public FileInfo overwrite(OpenContext ctx, Set<FileAttributes> fileAttributes, boolean replaceFileAttributes, long allocationSize) throws NTStatusException {
        log.info(
                "OVERWRITE - ctx={}, fileAttributes={}, replaceFileAttributes={}, allocationSize={}",
                ctx, fileAttributes, replaceFileAttributes, allocationSize
        );

        synchronized (cacheLock) {

            return overwriteService.overwrite(ctx, fileAttributes, replaceFileAttributes, allocationSize);
        }
    }

    @Override
    public void cleanup(OpenContext ctx, Set<CleanupFlags> flags) {
        log.info("CLEANUP - ctx={}, flags={}", ctx, flags);

        synchronized (cacheLock) {

            cleanupService.cleanup(ctx, flags, volumeResult.volumeLabel());
        }
    }

    @Override
    public void close(OpenContext ctx) {
        log.info("CLOSE - ctx={}", ctx);

        synchronized (cacheLock) {
            closeService.close(ctx);
        }
    }

    @Override
    public long read(OpenContext ctx, Pointer pBuffer, long offset, int length) throws NTStatusException {
        log.info("READ - ctx={}, pBuffer={}, offset={}, length={}", ctx, pBuffer, offset, length);

        synchronized (cacheLock) {

            return readService.read(ctx, pBuffer, offset, length, volumeResult.volumeLabel());
        }
    }

    @Override
    public WriteResult write(
            OpenContext ctx,
            Pointer pBuffer,
            long offset,
            int length,
            boolean c,
            boolean constrainedIo
    ) throws NTStatusException {
        log.info(
                "WRITE - ctx={}, pBuffer={}, offset={}, length={}, c={}, constrainedIo={}",
                ctx, pBuffer, offset, length, c, constrainedIo
        );

        synchronized (cacheLock) {
            return writeService.write(ctx, pBuffer, offset, length, c, constrainedIo, volumeResult.volumeLabel());
        }
    }

    @Override
    public FileInfo flush(OpenContext ctx) throws NTStatusException {
        log.info("FLUSH - ctx={}", ctx);

        synchronized (cacheLock) {
            return flushService.flush(ctx, volumeResult.volumeLabel());
        }
    }

    @Override
    public FileInfo getFileInfo(OpenContext ctx) throws NTStatusException {
        log.info("GET FILE INFO - ctx={}", ctx);

        synchronized (cacheLock) {
            return openService.getFileInfo(ctx.getFileHandle());
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
            return writeService.setFileSize(ctx, newSize, setAllocationSize, volumeResult.volumeLabel());
        }
    }

    @Override
    public void canDelete(OpenContext ctx) throws NTStatusException {
        log.info("CAN DELETE - ctx={}", ctx);

        String path = openFileStateRegistry.require(ctx.getFileHandle()).getPath();

        if (isRootDirectory(path)) {
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

            renameService.rename(ctx, oldFileName, newFileName, replaceIfExists, volumeResult.volumeLabel());
        }
    }

    @Override
    public byte[] getSecurity(OpenContext ctx) throws NTStatusException {
        log.info("GET SECURITY - ctx={}", ctx);

        synchronized (cacheLock) {
            return securityService.getSecurity(ctx);
        }
    }

    @Override
    public void setSecurity(OpenContext ctx, byte[] securityDescriptor) throws NTStatusException {
        log.info("SET SECURITY - ctx={}, securityDescriptor={}", ctx, securityDescriptor);

        synchronized (cacheLock) {
            securityService.setSecurity(ctx, securityDescriptor);
        }
    }

    @Override
    public void readDirectory(OpenContext ctx, String pattern, String marker, Predicate<FileInfo> consumer) throws NTStatusException {
        log.info("READ DIRECTORY - ctx={}, pattern={}, marker={}, consumer={}", ctx, pattern, marker, consumer);

        synchronized (cacheLock) {

            readDirectoryService.readDirectory(ctx, pattern, marker, consumer, volumeResult.volumeLabel());
        }
    }

    @Override
    public FileInfo getDirInfoByName(OpenContext parentDirCtx, String fileName) throws NTStatusException {
        log.info("GET DIR INFO BY NAME - parentDirCtx={}, fileName={}", parentDirCtx, fileName);

        synchronized (cacheLock) {
            return readDirectoryService.getDirInfoByName(parentDirCtx, fileName, volumeResult.volumeLabel());
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

    private boolean isRootDirectory(String path) {
        return path == null || path.isBlank() || "\\".equals(path) || "/".equals(path);
    }

}
