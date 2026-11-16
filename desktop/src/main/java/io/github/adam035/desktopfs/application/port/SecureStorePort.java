package io.github.adam035.desktopfs.application.port;

import java.util.Optional;

public interface SecureStorePort {

    void put (String key, byte[] value);

    Optional<byte[]> get(String key);

    void remove(String key);

}
