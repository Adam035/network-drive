package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    private final AuthUserExtractorPort authUserExtractorPort;

    public User updateUser(User user) {
        log.info("Updating user {}", user.getId());

        if (user.getId() == null || user.getId().isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        boolean canAccess = authUserExtractorPort.extractUser()
                .map(u -> u.getId().equals(user.getId()))
                .orElse(false);

        if (!canAccess) {
            throw new UnauthorizedException();
        }

        return userRepository.save(user);
    }

}
