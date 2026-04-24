package io.github.adam035.networkdrive.auth.application.port.out;

import io.github.adam035.networkdrive.common.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String email);

}
