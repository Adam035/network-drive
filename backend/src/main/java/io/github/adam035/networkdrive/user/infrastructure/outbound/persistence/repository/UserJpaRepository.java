package io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.repository;

import io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByUsernameOrEmail(String username, String email);

}
