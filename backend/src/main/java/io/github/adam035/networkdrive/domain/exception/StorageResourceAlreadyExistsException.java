package io.github.adam035.networkdrive.domain.exception;

public class StorageResourceAlreadyExistsException extends RuntimeException {

    public StorageResourceAlreadyExistsException(String path) {
        super("Storage resource already exists: " + path);
    }
}
