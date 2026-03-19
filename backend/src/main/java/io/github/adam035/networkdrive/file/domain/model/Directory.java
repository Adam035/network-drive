package io.github.adam035.networkdrive.file.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static io.github.adam035.networkdrive.file.domain.model.StorageResourceType.DIRECTORY;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Directory extends StorageResource {

    public Directory() {
        setType(DIRECTORY);
    }

    private List<String> childIds;

}
