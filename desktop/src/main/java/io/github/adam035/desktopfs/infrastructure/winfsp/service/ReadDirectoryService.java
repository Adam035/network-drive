package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.ReadDirectoryUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ReadDirectoryService {
    private final OpenHandleRegistry openHandleRegistry;
    private final ReadDirectoryUseCase readDirectoryUseCase;
    private final FileInfoMapper fileInfoMapper;
    private final OpenService openService;

    public void readDirectory(OpenContext ctx, String pattern, String marker,
                              Predicate<FileInfo> consumer, String volumeLabel) throws NTStatusException {
        OpenFileState directory = openHandleRegistry.require(ctx.getFileHandle());
        if (!directory.isDirectory()) {
            throw new NTStatusException(0xC0000103);
        }
        var entries = new TreeMap<String, FileInfo>();
        try {
            readDirectoryUseCase.readDirectory(directory.getPath(), directory.getVolumeLabel()).children()
                    .forEach(resource -> entries.put(resource.getName(), fileInfoMapper.toFileInfo(resource)));
        } catch (RuntimeException e) {
            throw new NTStatusException(0xC0000185);
        }
        for (OpenFileState state : openHandleRegistry.states(directory.getVolumeLabel())) {
            String parent = OpenHandleRegistry.normalize(
                    state.getPath().substring(0, state.getPath().lastIndexOf('\\')));
            if (!state.getPath().equals("\\") && parent.equals(directory.getPath())) {
                entries.put(state.getPath().substring(state.getPath().lastIndexOf('\\') + 1), state.getFileInfo());
            }
        }
        for (var entry : entries.entrySet()) {
            if (marker != null && !marker.isEmpty() && entry.getKey().compareTo(marker) <= 0) {
                continue;
            }
            if (!consumer.test(entry.getValue())) {
                break;
            }
        }
    }

    public FileInfo getDirInfoByName(OpenContext parentCtx, String name) throws NTStatusException {
        OpenFileState parent = openHandleRegistry.require(parentCtx.getFileHandle());
        if (!parent.isDirectory()) {
            throw new NTStatusException(0xC0000103);
        }
        String path = OpenHandleRegistry.childPath(parent.getPath(), name);
        return openService.findInfo(path, parent.getVolumeLabel())
                .orElseThrow(() -> new NTStatusException(0xC0000034));
    }
}
