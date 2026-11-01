package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.UpdateUserCommand;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.AccountTierDoesNotExistException;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.AccountTierRepository;
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

    private final AccountTierRepository accountTierRepository;

    public User updateUser(UpdateUserCommand updateUserCommand) {
        String userId = updateUserCommand.id();

        log.info("Updating user {}", userId);

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UnauthorizedException::new);

        boolean canAccess = user.getId().equals(userId);

        if (!canAccess) {
            throw new UnauthorizedException();
        }

        return updateUserFields(user, updateUserCommand);
    }

    private User updateUserFields(User user, UpdateUserCommand updateUserCommand) {
        if (updateUserCommand.username() != null) {
            user.setUsername(updateUserCommand.username());
        }

        if (updateUserCommand.email() != null) {
            user.setEmail(updateUserCommand.email());
        }

        if (updateUserCommand.accountTierName() != null) {
            accountTierRepository.findByName(updateUserCommand.accountTierName())
                    .ifPresentOrElse(
                            user::setAccountTier,
                            () -> {
                                throw new AccountTierDoesNotExistException();
                            }
                    );
        }

        return userRepository.save(user);
    }

}