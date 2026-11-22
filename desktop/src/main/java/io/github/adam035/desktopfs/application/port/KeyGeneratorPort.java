package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.application.dto.KeyPair;

public interface KeyGeneratorPort {

    KeyPair generateKeyPair();

}
