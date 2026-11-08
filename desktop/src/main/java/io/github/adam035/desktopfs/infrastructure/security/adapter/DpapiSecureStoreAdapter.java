package io.github.adam035.desktopfs.infrastructure.security.adapter;

import com.sun.jna.platform.win32.Crypt32Util;
import io.github.adam035.desktopfs.application.port.SecureStorePort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class DpapiSecureStoreAdapter implements SecureStorePort {

    private final Path secureStoreDirectoryPath = Path.of(
            System.getProperty("user.home"), "AppData", "Local", "NetworkDrive"
    );

    @Override
    public void put(String key, byte[] value) {
        byte[] encryptedBytes = Crypt32Util.cryptProtectData(value);
        Path filePath = secureStoreDirectoryPath.resolve(key.concat(".dpapi"));

        try {
            Files.createDirectories(secureStoreDirectoryPath);
            Files.write(filePath, encryptedBytes);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write secure-store entry", e);
        }
    }

    @Override
    public Optional<byte[]> get(String key) {
        Path filePath = secureStoreDirectoryPath.resolve(key.concat(".dpapi"));

        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        try {
            byte[] encryptedBytes = Files.readAllBytes(filePath);
            byte[] decryptedBytes = Crypt32Util.cryptUnprotectData(encryptedBytes);

            return Optional.of(decryptedBytes);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read secure-store entry", e);
        }
    }

    @Override
    public void remove(String key) {
        Path filePath = secureStoreDirectoryPath.resolve(key.concat(".dpapi"));

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Could not remove secure-store entry", e);
        }
    }

}
