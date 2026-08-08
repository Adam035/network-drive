package io.github.adam035.networkdrive.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "directories")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class DirectoryEntity extends StorageResourceEntity {

}