package io.github.adam035.networkdrive.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Device {

    private String id;

    private User user;

    private byte[] publicKey;

    private Instant createdAt;

    private Instant updatedAt;

}
