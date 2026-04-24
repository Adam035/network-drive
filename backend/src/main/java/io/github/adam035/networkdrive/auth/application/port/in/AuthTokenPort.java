package io.github.adam035.networkdrive.auth.application.port.in;

import io.github.adam035.networkdrive.auth.application.dto.AuthTokensResult;

public interface AuthTokenPort {

    AuthTokensResult generateAuthTokens(String userId);

}
