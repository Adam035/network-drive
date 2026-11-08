package io.github.adam035.networkdrive.application.port;

import io.github.adam035.networkdrive.application.dto.OAuth2Attempt;

import java.util.Optional;

public interface AuthorizationCodeStorePort {

    void put (String key, OAuth2Attempt value);

    Optional<OAuth2Attempt> get(String key);

    void remove(String key);

}
