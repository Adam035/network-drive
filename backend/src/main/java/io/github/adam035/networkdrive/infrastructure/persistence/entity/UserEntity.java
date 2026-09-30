package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_tier_id", nullable = false)
    private AccountTierEntity accountTier;

    @OneToMany(mappedBy = "recipient")
    private Set<ShareEntity> receivedShares = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<ShareEntity> grantedShares = new HashSet<>();

}
