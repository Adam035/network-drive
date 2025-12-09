package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DirectoryJpaRepository extends JpaRepository<DirectoryEntity, String> {
}
