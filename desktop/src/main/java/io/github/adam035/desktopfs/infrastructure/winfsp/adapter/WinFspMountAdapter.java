package io.github.adam035.desktopfs.infrastructure.winfsp.adapter;

import com.github.jnrwinfspteam.jnrwinfsp.api.MountOptions;
import com.github.jnrwinfspteam.jnrwinfsp.service.ServiceException;
import com.github.jnrwinfspteam.jnrwinfsp.service.ServiceRunner;
import io.github.adam035.desktopfs.application.port.MountPort;
import io.github.adam035.desktopfs.infrastructure.winfsp.filesystem.WinFspFileSystem;
import io.github.adam035.desktopfs.infrastructure.winfsp.filesystem.WinFspFileSystemFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class WinFspMountAdapter implements MountPort {

    private static final String SERVICE_NAME = "NetworkDrive";

    private final MountOptions winFspMountOptions;

    private final WinFspFileSystemFactory winFspFileSystemFactory;

    @Override
    public void mountVolume(String volumeLabel, Path path) {
        WinFspFileSystem winFspFileSystem = winFspFileSystemFactory.createFileSystem(volumeLabel);
        try {
            ServiceRunner.mountLocalDriveAsService(SERVICE_NAME, winFspFileSystem, path, winFspMountOptions);
        } catch (ServiceException e) {
            throw new RuntimeException(e); // TODO
        }
    }

}
