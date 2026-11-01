package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import io.github.adam035.networkdrive.domain.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "account_tier_id")
    private AccountTierEntity accountTier;

}
