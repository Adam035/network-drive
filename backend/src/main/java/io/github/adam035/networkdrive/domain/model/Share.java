package io.github.adam035.networkdrive.domain.model;

import lombok.Data;

@Data
public class Share {

    private String id;

    private String ownerId;

    private String recipientId;

}
