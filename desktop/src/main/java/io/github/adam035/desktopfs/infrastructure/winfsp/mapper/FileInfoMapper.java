package io.github.adam035.desktopfs.infrastructure.winfsp.mapper;

import com.github.jnrwinfspteam.jnrwinfsp.api.FileAttributes;
import com.github.jnrwinfspteam.jnrwinfsp.api.FileInfo;
import com.github.jnrwinfspteam.jnrwinfsp.api.WinSysTime;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import org.mapstruct.*;

import static io.github.adam035.desktopfs.domain.model.StorageResource.Type.FILE;

@Mapper(
        componentModel = "spring",
        imports = WinSysTime.class
)
public interface FileInfoMapper {

    @Mapping(target = "fileSize", source = "size")
    @Mapping(target = "allocationSize", source = "size")
    @Mapping(target = "creationTime", expression = "java(WinSysTime.fromInstant(storageResource.getCreatedAt()))")
    @Mapping(target = "lastWriteTime", expression = "java(WinSysTime.fromInstant(storageResource.getUpdatedAt()))")
    @Mapping(target = "lastAccessTime", expression = "java(WinSysTime.fromInstant(storageResource.getUpdatedAt()))")
    @Mapping(target = "changeTime", expression = "java(WinSysTime.fromInstant(storageResource.getUpdatedAt()))")
    FileInfo toFileInfo(StorageResource storageResource);

    default FileInfo toFileInfo(OpenFileState state) {
        FileInfo info = toFileInfo(state.getStorageResource());
        info.setAllocationSize(state.getAllocationSize());

        return info;
    }

    @ObjectFactory
    default FileInfo create(StorageResource storageResource) {
        return new FileInfo(storageResource.getName());
    }

    @AfterMapping
    default void afterMapping(StorageResource storageResource, @MappingTarget FileInfo fileInfo) {
        FileAttributes attribute = FILE.equals(storageResource.getType())
                ? FileAttributes.FILE_ATTRIBUTE_NORMAL
                : FileAttributes.FILE_ATTRIBUTE_DIRECTORY;

        fileInfo.getFileAttributes().clear();
        fileInfo.getFileAttributes().add(attribute);
        fileInfo.setNormalizedName(storageResource.getName());
    }

}
