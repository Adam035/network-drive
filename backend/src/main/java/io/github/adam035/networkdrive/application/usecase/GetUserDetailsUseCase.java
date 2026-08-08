package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GetUserDetailsUseCase {

    private final UserRepository userRepository;

    private final AuthUserExtractorPort authUserExtractorPort;

    public User getUserDetails(String userId) {
        log.info("Getting user details for user {}", userId);

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        boolean canAccess = authUserExtractorPort.extractUser()
                .map(user -> user.getId().equals(userId))
                .orElse(false);

        if (!canAccess) {
            throw new UnauthorizedException();
        }

        return userRepository.findById(userId)
                .orElseThrow(UserDoesNotExist::new);
    }

}
