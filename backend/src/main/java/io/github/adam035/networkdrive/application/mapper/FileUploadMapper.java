package io.github.adam035.networkdrive.application.mapper;

import io.github.adam035.networkdrive.application.dto.FileUploadCommand;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "name", source = "fileUploadCommand.path", qualifiedByName = "pathToName")
    @Mapping(target = "size", expression = "java((long) fileUploadCommand.bytes().length)")
    @Mapping(target = "path", source = "fileUploadCommand.path")
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "storageKey", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "type", expression = "java(io.github.adam035.networkdrive.domain.model.StorageResource.Type.FILE)")
    File mapToModel(FileUploadCommand fileUploadCommand, User owner);

    @Named("pathToName")
    default String pathToName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

}
