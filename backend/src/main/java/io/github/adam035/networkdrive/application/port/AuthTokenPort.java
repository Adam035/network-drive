package io.github.adam035.networkdrive.application.port;

import io.github.adam035.networkdrive.application.dto.AuthTokensResult;

public interface AuthTokenPort {

    AuthTokensResult generateAuthTokens(String userId);

}
