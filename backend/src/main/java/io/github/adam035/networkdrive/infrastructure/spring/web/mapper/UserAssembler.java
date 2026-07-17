package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.UpdateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public User mapToUser(String userId, UpdateUserRequest updateUserRequest) {
        User user = new User();
        user.setId(userId);
        user.setUsername(updateUserRequest.username());
        user.setEmail(updateUserRequest.email());

        return user;
    }

}
