package io.github.adam035.networkdrive.domain.model;

import lombok.Data;

@Data
public class AccountTier {

    private String id;

    private String name;

    private Integer price;

    private Long maxVolumeSize;

}
