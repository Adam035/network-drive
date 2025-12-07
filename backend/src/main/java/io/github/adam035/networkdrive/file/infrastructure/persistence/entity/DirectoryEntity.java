package io.github.adam035.networkdrive.file.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "directories")
@EqualsAndHashCode(callSuper = true)
public class DirectoryEntity extends BaseEntity {

    private List<StorageResourceEntity> children;

}
