package io.github.adam035.networkdrive.auth.domain.service;

import io.github.adam035.networkdrive.auth.domain.model.User;
import io.github.adam035.networkdrive.auth.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isRegistrationAllowed(User user) {
        return userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .isEmpty();
    }

}
