package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.StorageResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StorageResourceJpaRepository extends JpaRepository<StorageResourceEntity, String> {
}
