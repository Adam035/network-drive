package io.github.adam035.networkdrive.file.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.common.infrastructure.entity.BaseEntity;
import io.github.adam035.networkdrive.file.domain.model.StorageResourceType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_resources")
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class StorageResourceEntity extends BaseEntity {

    private String name;

    @JoinColumn(name = "parent_id")
    private String parentId;

    @JoinColumn(name = "owner_id")
    private String ownerId;

    private StorageResourceType type;

}
