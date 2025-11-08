package io.github.adam035.networkdrive.user.domain.repository;

import io.github.adam035.networkdrive.user.domain.model.Credential;

public interface CredentialRepository {

    Credential save(Credential credential);

}
