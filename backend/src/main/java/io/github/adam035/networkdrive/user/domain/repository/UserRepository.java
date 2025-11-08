package io.github.adam035.networkdrive.user.domain.repository;

import io.github.adam035.networkdrive.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameOrEmail(String username, String email);

}
