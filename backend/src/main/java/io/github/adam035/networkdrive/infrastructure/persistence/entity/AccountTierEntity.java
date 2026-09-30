package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "account_tiers")
@EqualsAndHashCode(callSuper = true)
public class AccountTierEntity extends BaseEntity {

    private String id;

    private String name;

    private Integer price;

    private Long maxVolumeSize;

}
