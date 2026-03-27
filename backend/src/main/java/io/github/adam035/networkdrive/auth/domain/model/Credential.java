package io.github.adam035.networkdrive.auth.domain.model;

import lombok.Data;

@Data
public class Credential {

    private String id;

    private byte[] authenticatorId;

    private byte[] publicKey;

    private Long signatureCount;

    private Boolean isDiscoverable;

    private User user;

}