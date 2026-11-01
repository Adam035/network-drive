package io.github.adam035.networkdrive.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AccountTier {

    private String id;

    private String name;

    private int price;

    private long maxVolumeSize;

    private Instant createdAt;

    private Instant updatedAt;

}
