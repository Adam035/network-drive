package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.infrastructure.persistence.entity.StorageResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface StorageResourceJpaRepository extends JpaRepository<StorageResourceEntity, String> {

    List<StorageResourceEntity> findAllByParentId(String parentId);

    Optional<StorageResourceEntity> findByPath(String path);

}
