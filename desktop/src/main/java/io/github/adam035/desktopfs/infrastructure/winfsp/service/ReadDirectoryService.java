package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.ReadDirectoryUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.FileHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ReadDirectoryService {
    private final FileHandleRegistry fileHandleRegistry;
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
        OpenFileState directory = fileHandleRegistry.require(ctx.getFileHandle());

        var children = new TreeMap<String, FileInfo>(String.CASE_INSENSITIVE_ORDER);
        for (var child : readDirectoryUseCase.readDirectory(directory.getPath(), volumeLabel).children()) {
            children.put(child.getName(), fileInfoMapper.toFileInfo(child));
        }

        var remaining = marker == null ? children : children.tailMap(marker, false);
        for (FileInfo fileInfo : remaining.values()) {
            if (!consumer.test(fileInfo)) {
                break;
            }
        }
    }

    public FileInfo getDirInfoByName(OpenContext parentCtx, String name, String volumeLabel) throws NTStatusException {
        OpenFileState parent = fileHandleRegistry.require(parentCtx.getFileHandle());
        String parentPath = parent.getPath();
        String childPath = parentPath.endsWith("\\")
                ? parentPath.concat(name)
                : parentPath.concat("\\").concat(name);

        return openService.findFileInfo(childPath, volumeLabel)
                .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
    }
}
