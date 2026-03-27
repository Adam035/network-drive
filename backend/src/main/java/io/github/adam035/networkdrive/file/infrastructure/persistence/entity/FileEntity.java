package io.github.adam035.networkdrive.file.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class FileEntity extends StorageResourceEntity {

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "storage_key", nullable = false, unique = true, updatable = false)
    private String storageKey;

    private Long size;

    // TODO: @Column(nullable = false)
    private String checksum;

}
