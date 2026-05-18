package io.github.adam035.networkdrive.user.application.usecase;

import io.github.adam035.networkdrive.common.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.common.application.port.in.AuthTokenExtractorPort;
import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    private final AuthTokenExtractorPort authTokenExtractorPort;

    public User updateUser(User user) {
        log.info("Updating user {}", user.getId());

        if (user.getId() == null || user.getId().isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        if (!authTokenExtractorPort.extractSubject().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized");
        }

        return userRepository.save(user);
    }

}
