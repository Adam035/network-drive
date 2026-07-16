package io.github.adam035.networkdrive.domain.service;

import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isRegistrationAllowed(User user) {
        log.info("Checking if user {} is allowed to register", user.getUsername());
        return userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .isEmpty();
    }

}
