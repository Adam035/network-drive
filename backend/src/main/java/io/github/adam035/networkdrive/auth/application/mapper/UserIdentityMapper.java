package io.github.adam035.networkdrive.auth.application.mapper;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.UserIdentity;
import io.github.adam035.networkdrive.auth.domain.model.User;
import org.mapstruct.Mapper;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring")
public interface UserIdentityMapper {

    default UserIdentity mapToUserIdentity(User source) {
        return UserIdentity.builder()
                .name(source.getUsername())
                .displayName(source.getUsername())
                .id(new ByteArray(source.getId().getBytes(StandardCharsets.UTF_8)))
                .build();
    }

}
