package io.github.adam035.networkdrive.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static io.github.adam035.networkdrive.domain.model.StorageResource.Type.DIRECTORY;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Directory extends StorageResource {

    public Directory() {
        setType(DIRECTORY);
    }

}
