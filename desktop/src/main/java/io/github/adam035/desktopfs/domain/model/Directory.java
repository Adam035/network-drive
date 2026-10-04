package io.github.adam035.desktopfs.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;

import static io.github.adam035.desktopfs.domain.model.StorageResource.Type.DIRECTORY;

@Data
@EqualsAndHashCode(callSuper = true)
public class Directory extends StorageResource {

    public Directory() {
        setType(DIRECTORY);
    }

}
