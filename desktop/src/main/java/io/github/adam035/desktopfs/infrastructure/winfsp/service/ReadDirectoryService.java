package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.ReadDirectoryUseCase;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ReadDirectoryService {

    private final OpenFileStateRegistry openFileStateRegistry;

    private final ReadDirectoryUseCase readDirectoryUseCase;

    private final FileInfoMapper fileInfoMapper;

    private final OpenService openService;

    public void readDirectory(
            OpenContext ctx,
            String pattern,
            String marker,
            Predicate<FileInfo> consumer,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState directory = openFileStateRegistry.require(ctx.getFileHandle());
        readDirectoryUseCase.readDirectory(directory.getPath(), volumeLabel)
                .children()
                .forEach(child -> consumer.test(fileInfoMapper.toFileInfo(child)));
    }

    public FileInfo getDirInfoByName(OpenContext parentCtx, String name, String volumeLabel) throws NTStatusException {
        OpenFileState parent = openFileStateRegistry.require(parentCtx.getFileHandle());
        String parentPath = parent.getPath();
        String childPath = parentPath.endsWith("\\")
                ? parentPath.concat(name)
                : parentPath.concat("\\").concat(name);

        return openService.findFileInfo(childPath, volumeLabel)
                .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
    }
    
}
