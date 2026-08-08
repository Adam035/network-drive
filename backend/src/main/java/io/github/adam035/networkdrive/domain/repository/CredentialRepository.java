package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.Credential;

public interface CredentialRepository {

    Credential save(Credential credential);

}
