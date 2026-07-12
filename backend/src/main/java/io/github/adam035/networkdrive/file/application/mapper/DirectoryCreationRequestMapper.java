package io.github.adam035.networkdrive.file.application.mapper;

import io.github.adam035.networkdrive.file.api.dto.DirectoryCreationRequest;
import io.github.adam035.networkdrive.file.domain.model.Directory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectoryCreationRequestMapper {

    @Mapping(target = "name", source = "directoryCreationRequest.name")
    @Mapping(target = "parentId", source = "directoryCreationRequest.parentId")
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "childIds", expression = "java(java.util.List.of())")
    @Mapping(target = "type", expression = "java(io.github.adam035.networkdrive.file.domain.model.StorageResourceType.DIRECTORY)")
    Directory mapToModel(DirectoryCreationRequest directoryCreationRequest, String ownerId);

}
