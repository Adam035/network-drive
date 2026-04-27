package io.github.adam035.networkdrive.user.application.usecase;

import io.github.adam035.networkdrive.auth.domain.exception.UserDoesNotExist;
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
public class GetUserDetailsUseCase {

    private final UserRepository userRepository;

    private final AuthTokenExtractorPort authTokenExtractorPort;

    public User getUserDetails(String userId) {
        log.info("Getting user details for user {}", userId);

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        if (!authTokenExtractorPort.extractSubject().equals(userId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExist(String.format("User do not exist: %s", userId)));
    }

}
