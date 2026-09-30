package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.service.FileSynchronizationService;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FlushService {

    private final OpenFileStateRegistry openFileStateRegistry;
    private final FileInfoMapper fileInfoMapper;

    private final FileSynchronizationService synchronizationService;

    public FileInfo flush(OpenContext openContext, String volumeLabel) throws NTStatusException {
        try {
            if (openContext == null) {
                flushVolume(volumeLabel);
                return null;
            }

            OpenFileState openFileState = openFileStateRegistry.require(openContext.getFileHandle());
            synchronizationService.synchronize(openFileState, volumeLabel);

            return fileInfoMapper.toFileInfo(openFileState);
        } catch (IOException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }

    }

    private void flushVolume(String volumeLabel) throws IOException {
        Collection<OpenFileState> openFileStates = openFileStateRegistry.getOpenFileStates().values();

        for (OpenFileState state : openFileStates) {
            synchronizationService.synchronize(state, volumeLabel);
        }
    }

}
