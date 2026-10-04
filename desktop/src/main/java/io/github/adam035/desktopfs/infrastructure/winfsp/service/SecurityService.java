package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.GetSecurityDescriptorUseCase;
import io.github.adam035.desktopfs.application.usecase.UpdateSecurityDescriptorUseCase;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private static final String DEFAULT_DESCRIPTOR = "O:BAG:BAD:PAR(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)(A;OICI;FA;;;WD)";

    private final OpenFileStateRegistry openFileStateRegistry;

    private final OpenService openService;

    private final GetSecurityDescriptorUseCase getSecurityDescriptorUseCase;

    private final UpdateSecurityDescriptorUseCase updateSecurityDescriptorUseCase;

    public Optional<SecurityResult> getSecurityByName(String path, String volumeLabel) throws NTStatusException {
        Optional<FileInfo> fileInfo = openService.findFileInfo(path, volumeLabel);

        Set<FileAttributes> fileAttributes = fileInfo.isEmpty() ? Set.of() : fileInfo.get().getFileAttributes();
        byte[] securityDescriptor = getSecurityDescriptorUseCase.getSecurityDescriptor(path, volumeLabel)
                .orElse(SecurityDescriptorHandler.securityDescriptorToBytes(DEFAULT_DESCRIPTOR));

        return Optional.of(new SecurityResult(securityDescriptor, fileAttributes));
    }

    public byte[] getSecurity(OpenContext openContext, String volumeLabel) throws NTStatusException {
        String path = getOpenFilePath(openContext);
        return getSecurityDescriptorUseCase.getSecurityDescriptor(path, volumeLabel)
                .orElse(SecurityDescriptorHandler.securityDescriptorToBytes(DEFAULT_DESCRIPTOR));
    }

    public void setSecurity(OpenContext openContext, byte[] securityDescriptor, String volumeLabel) throws NTStatusException {
        String path = getOpenFilePath(openContext);
        updateSecurityDescriptorUseCase.updateSecurityDescriptor(path, securityDescriptor, volumeLabel);
    }

    private String getOpenFilePath(OpenContext openContext) {
        return openFileStateRegistry.require(openContext.getFileHandle()).getPath();
    }

}
