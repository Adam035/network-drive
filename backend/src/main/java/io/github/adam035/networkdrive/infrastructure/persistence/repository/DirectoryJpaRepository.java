package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.infrastructure.persistence.entity.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface DirectoryJpaRepository extends JpaRepository<DirectoryEntity, String> {

    Optional<DirectoryEntity> findByPath(String path);

    List<DirectoryEntity> findAllByParentId(String parentId);

}
