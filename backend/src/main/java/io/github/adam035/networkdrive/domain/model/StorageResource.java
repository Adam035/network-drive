package io.github.adam035.networkdrive.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class StorageResource {

    @EqualsAndHashCode.Include
    private String id;

    private String name;

    private String path;

    private Long size;

    private String parentId;

    private User owner;

    private Type type;

    private Instant createdAt;

    private Instant updatedAt;

    public enum Type {
        FILE,
        DIRECTORY
    }

}
