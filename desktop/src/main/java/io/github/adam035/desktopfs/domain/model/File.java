package io.github.adam035.desktopfs.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static io.github.adam035.desktopfs.domain.model.StorageResource.Type.FILE;

@Data
@EqualsAndHashCode(callSuper = true)
public class File extends StorageResource {

    private String mimeType;

    private String storageKey;

    private String checksum;

    public File() {
        setType(FILE);
    }

}
