package io.github.adam035.networkdrive.user.application.mapper;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.UserIdentity;
import io.github.adam035.networkdrive.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIdentityMapper {

    default UserIdentity mapToUserIdentity(User source) {
        return UserIdentity.builder()
                .name(source.getUsername())
                .displayName(source.getUsername())
                .id(new ByteArray(source.getId().getBytes()))
                .build();
    }

}
