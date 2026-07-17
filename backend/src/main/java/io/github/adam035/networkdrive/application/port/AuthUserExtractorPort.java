package io.github.adam035.networkdrive.application.port;

import io.github.adam035.networkdrive.domain.model.User;

import java.util.Optional;

public interface AuthUserExtractorPort {

    Optional<User> extractUser();

}
