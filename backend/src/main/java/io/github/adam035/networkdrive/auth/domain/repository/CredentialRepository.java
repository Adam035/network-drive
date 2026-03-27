package io.github.adam035.networkdrive.auth.domain.repository;

import io.github.adam035.networkdrive.auth.domain.model.Credential;

public interface CredentialRepository {

    Credential save(Credential credential);

}
