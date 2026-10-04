package io.github.adam035.desktopfs.domain.model;

import lombok.Data;

import java.time.Instant;

@Data
public class StorageResource {

    private String id;

    private String name;

    private String path;

    private Long size;

    private String parentId;

    private User owner;

    private Type type;

    private byte[] securityDescriptor;

    private Instant createdAt;

    private Instant updatedAt;

    public enum Type {
        FILE,
        DIRECTORY,
    }

}
