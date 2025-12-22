package io.github.adam035.networkdrive.file.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity(name = "storageUserEntity")
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Immutable
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

}
