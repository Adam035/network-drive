package io.github.adam035.networkdrive.application.mapper;

import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {

    @Mapping(target = "name", source = "multipartFile.name")
    @Mapping(target = "mimeType", source = "multipartFile.contentType")
    @Mapping(target = "size", source = "multipartFile.size")
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "storageKey", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "type", expression = "java(io.github.adam035.networkdrive.domain.model.StorageResource.Type.FILE)")
    File mapToModel(MultipartFile multipartFile, User owner);

}
