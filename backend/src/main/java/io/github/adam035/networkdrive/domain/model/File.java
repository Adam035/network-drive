package io.github.adam035.networkdrive.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static io.github.adam035.networkdrive.domain.model.StorageResource.Type.FILE;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class File extends StorageResource {

    private String mimeType;

    private String storageKey;

    private String checksum;

    public File() {
        setType(FILE);
    }

}
