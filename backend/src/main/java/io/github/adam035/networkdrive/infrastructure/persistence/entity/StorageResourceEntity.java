package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.domain.model.StorageResource;
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

    private String path;

    private Long size;

    @JoinColumn(name = "parent_id")
    private String parentId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    private StorageResource.Type type;

}