package io.github.adam035.networkdrive.user.infrastructure.inbound.web.assembler;

import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto.UpdateUserRequest;
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
