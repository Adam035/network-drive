package io.github.adam035.networkdrive.file.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.file.domain.model.Directory;
import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.domain.model.StorageResource;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.FileEntity;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.StorageResourceEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
        componentModel = "spring",
        uses = {
                FileMapper.class,
                DirectoryMapper.class
        }
)
public interface StorageResourceMapper {

    @BeanMapping(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
    @SubclassMapping(source = FileEntity.class, target = File.class)
    @SubclassMapping(source = DirectoryEntity.class, target = Directory.class)
    StorageResource mapToModel(StorageResourceEntity source);

    @BeanMapping(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
    @SubclassMapping(source = File.class, target = FileEntity.class)
    @SubclassMapping(source = Directory.class, target = DirectoryEntity.class)
    StorageResourceEntity mapToEntity(StorageResource source);

}
