package io.github.adam035.networkdrive.file.application.mapper;

import io.github.adam035.networkdrive.file.api.dto.FileUploadRequest;
import io.github.adam035.networkdrive.file.domain.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {

    @Mapping(target = "name", source = "file.name")
    @Mapping(target = "mimeType", source = "file.contentType")
    @Mapping(target = "size", source = "file.size")
    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "storageKey", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "type", expression = "java(io.github.adam035.networkdrive.file.domain.model.StorageResourceType.FILE)")
    File mapToModel(FileUploadRequest source);

}
