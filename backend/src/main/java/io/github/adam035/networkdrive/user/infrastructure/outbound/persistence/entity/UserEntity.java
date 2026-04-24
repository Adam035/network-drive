package io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.entity;

import io.github.adam035.networkdrive.common.infrastructure.outbound.persistence.entity.BaseEntity;
import io.github.adam035.networkdrive.common.domain.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "authUserEntity")
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
