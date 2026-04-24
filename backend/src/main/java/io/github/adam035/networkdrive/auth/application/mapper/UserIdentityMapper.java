package io.github.adam035.networkdrive.auth.application.mapper;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.UserIdentity;
import io.github.adam035.networkdrive.common.domain.model.User;
import org.mapstruct.Mapper;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring")
public interface UserIdentityMapper {

    default UserIdentity mapToUserIdentity(User user) {
        return UserIdentity.builder()
                .name(user.getUsername())
                .displayName(user.getUsername())
                .id(new ByteArray(user.getId().getBytes(StandardCharsets.UTF_8)))
                .build();
    }

}
