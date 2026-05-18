package io.github.adam035.networkdrive.user.infrastructure.inbound.web.controller;

import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.application.usecase.GetUserDetailsUseCase;
import io.github.adam035.networkdrive.user.application.usecase.UpdateUserUseCase;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.assembler.UserAssembler;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto.UpdateUserRequest;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto.UserDetailsResponse;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.mapper.UserDetailsMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final GetUserDetailsUseCase getUserDetailsUseCase;

    private final UpdateUserUseCase updateUserUseCase;

    private final UserDetailsMapper userDetailsMapper;

    private final UserAssembler userAssembler;

    @GetMapping("/{userId}")
    public UserDetailsResponse getUserDetail(@PathVariable String userId) {
        return userDetailsMapper.mapToUserDetailsResponse(getUserDetailsUseCase.getUserDetails(userId));
    }

    @PutMapping("/{userId}")
    public UserDetailsResponse getUserDetail(@PathVariable String userId, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userAssembler.mapToUser(userId, updateUserRequest);
        return userDetailsMapper.mapToUserDetailsResponse(updateUserUseCase.updateUser(user));
    }

}
