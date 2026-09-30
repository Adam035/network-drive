package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private static final String DEFAULT_DESCRIPTOR = "O:BAG:BAD:PAR(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)(A;OICI;FA;;;WD)";
    private final OpenService openService;
    private final OpenFileStateRegistry openFileStateRegistry;

    public Optional<SecurityResult> getSecurityByName(String path, String volumeLabel) throws NTStatusException {
        Optional<FileInfo> info = openService.findFileInfo(path, volumeLabel);

        if (info.isEmpty()) {
            throw new NTStatusException(0xC0000034); // STATUS_OBJECT_NAME_NOT_FOUND
        }

        return Optional.of(new SecurityResult(descriptor(null), info.get().getFileAttributes()));
    }

    public byte[] getSecurity(OpenContext ctx) throws NTStatusException {
        return descriptor(openFileStateRegistry.require(ctx.getFileHandle()));
    }

    public void setSecurity(OpenContext ctx, byte[] descriptor) throws NTStatusException {
        // TODO
    }

    private byte[] descriptor(OpenFileState state) throws NTStatusException {
        return SecurityDescriptorHandler.securityDescriptorToBytes(DEFAULT_DESCRIPTOR); // TODO
    }
}
