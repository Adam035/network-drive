package io.github.adam035.networkdrive.application.exception;

public class StorageResourceAlreadyExistsException extends RuntimeException {
    public StorageResourceAlreadyExistsException(String path) {
        super("Resource ".concat(path).concat(" already exists"));
    }
}
