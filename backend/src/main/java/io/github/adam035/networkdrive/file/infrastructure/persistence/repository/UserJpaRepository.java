package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("storageUserJpaRepository")
interface UserJpaRepository extends JpaRepository<UserEntity, String> {
}
