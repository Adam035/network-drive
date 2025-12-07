package io.github.adam035.networkdrive.file.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.common.infrastructure.entity.BaseEntity;
import io.github.adam035.networkdrive.file.domain.model.StorageResourceType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_resources")
@EqualsAndHashCode(callSuper = true)
public class StorageResourceEntity extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private StorageResourceEntity parent;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    private StorageResourceType type;

}
