package io.github.adam035.networkdrive.file.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class StorageResource {

    @EqualsAndHashCode.Include
    private String name;

    private StorageResource parent;

    private User owner;

    private StorageResourceType type;

}
