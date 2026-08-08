package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import com.github.jnrwinfspteam.jnrwinfsp.api.MountOptions;
import com.github.jnrwinfspteam.jnrwinfsp.service.ServiceException;
import com.github.jnrwinfspteam.jnrwinfsp.service.ServiceRunner;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class WinFspStarter {

    private final static Path MOUNT_PATH = Path.of("X:");

    private final WinFspFileSystem winFspFileSystem;

    private final MountOptions winFspMountOptions;

    @PostConstruct
    public void mountDrive() {
        try {
            ServiceRunner.mountLocalDriveAsService("NetworkDriveTest01", winFspFileSystem, MOUNT_PATH, winFspMountOptions);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

}
