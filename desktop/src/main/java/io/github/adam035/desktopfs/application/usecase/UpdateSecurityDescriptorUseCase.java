package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.SecurityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSecurityDescriptorUseCase {

    private final SecurityPort securityPort;

    private final PathPort pathPort;

    public void updateSecurityDescriptor(String path, byte[] securityDescriptor, String volumeLabel) {
        securityPort.updateSecurityDescriptor(pathPort.normalizePath(path, volumeLabel), securityDescriptor);
    }

}
