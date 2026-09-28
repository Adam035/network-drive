package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import io.github.adam035.desktopfs.application.port.VolumePort;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.FileHandleRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WinFspFileSystemFactory {

    private final VolumePort volumePort;

    private final FileHandleRegistry fileHandleRegistry;

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

    public WinFspFileSystem createFileSystem(String volumeLabel) {
        return new WinFspFileSystem(
                volumeLabel,
                volumePort,
                fileHandleRegistry,
                cleanupService,
                closeService,
                createService,
                flushService,
                openService,
                overwriteService,
                readDirectoryService,
                readService,
                renameService,
                securityService,
                writeService
        );
    }

}
