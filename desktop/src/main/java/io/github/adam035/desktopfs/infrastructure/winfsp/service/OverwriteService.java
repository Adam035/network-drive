package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.TemporaryFileRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OverwriteService {
    private final OpenHandleRegistry openHandleRegistry;
    private final TemporaryFileRegistry temporaryFileRegistry;

    public FileInfo overwrite(OpenContext ctx, Set<FileAttributes> attributes,
                              boolean replaceAttributes, long allocationSize) throws NTStatusException {
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        synchronized (state) {
            if (state.isDirectory() || state.isDeleted()) {
                throw new NTStatusException(0xC0000022);
            }
            try {
                long allocation = OpenFileState.allocationSize(allocationSize);
                temporaryFileRegistry.truncate(state);
                state.setDirty(true);
                FileInfo info = state.getFileInfo();
                info.setFileSize(0);
                info.setAllocationSize(allocation);
                if (replaceAttributes) {
                    info.getFileAttributes().clear();
                }
                if (attributes != null) {
                    info.getFileAttributes().addAll(attributes);
                }
                info.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
                if (info.getFileAttributes().isEmpty()) {
                    info.getFileAttributes().add(FileAttributes.FILE_ATTRIBUTE_NORMAL);
                } else if (info.getFileAttributes().size() > 1) {
                    info.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_NORMAL);
                }
                return info;
            } catch (IOException | RuntimeException e) {
                log.error("Cannot overwrite {}", state.getPath(), e);
                throw new NTStatusException(0xC0000185);
            }
        }
    }
}
