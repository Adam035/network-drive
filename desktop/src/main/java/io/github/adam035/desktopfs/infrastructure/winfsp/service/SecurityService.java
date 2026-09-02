package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private static final String DEFAULT_DESCRIPTOR =
            "O:BAG:BAD:PAR(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)(A;OICI;FA;;;WD)";
    private final OpenService openService;
    private final OpenHandleRegistry openHandleRegistry;

    public Optional<SecurityResult> getSecurityByName(String path, String volumeLabel) throws NTStatusException {
        Optional<FileInfo> info = openService.findInfo(path, volumeLabel);
        if (info.isEmpty()) {
            return Optional.empty();
        }
        OpenFileState state = openHandleRegistry.find(volumeLabel, path).orElse(null);
        return Optional.of(new SecurityResult(descriptor(state), info.get().getFileAttributes()));
    }

    public byte[] getSecurity(OpenContext ctx) throws NTStatusException {
        return descriptor(openHandleRegistry.require(ctx.getFileHandle()));
    }

    public void setSecurity(OpenContext ctx, byte[] descriptor) throws NTStatusException {
        openHandleRegistry.require(ctx.getFileHandle())
                .setSecurityDescriptor(descriptor == null ? null : descriptor.clone());
    }

    private byte[] descriptor(OpenFileState state) throws NTStatusException {
        return state != null && state.getSecurityDescriptor() != null
                ? state.getSecurityDescriptor().clone()
                : SecurityDescriptorHandler.securityDescriptorToBytes(DEFAULT_DESCRIPTOR);
    }
}
