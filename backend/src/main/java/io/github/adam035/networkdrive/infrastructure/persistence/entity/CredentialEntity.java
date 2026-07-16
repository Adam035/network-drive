package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "credentials")
@EqualsAndHashCode(callSuper = true)
public class CredentialEntity extends BaseEntity {

    @Column(name = "authenticator_id", nullable = false)
    private byte[] authenticatorId;

    @Column(name = "public_key")
    public byte[] publicKey;

    @Column(name = "signature_count")
    private Long signatureCount;

    @Column(name = "is_discoverable")
    private Boolean isDiscoverable;

    @Column(name = "user_id")
    private String userId;

}
