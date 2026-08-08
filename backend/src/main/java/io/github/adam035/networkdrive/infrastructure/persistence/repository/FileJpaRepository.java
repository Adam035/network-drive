package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface FileJpaRepository extends JpaRepository<FileEntity, String> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByParentId(String parentId);

}
