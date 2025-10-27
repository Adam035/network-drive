package io.github.adam035.networkdrive.user.infrastructure.entity;

import io.github.adam035.networkdrive.common.infrastructure.entity.BaseEntity;
import io.github.adam035.networkdrive.user.domain.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    private String username;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
