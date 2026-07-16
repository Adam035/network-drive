package io.github.adam035.networkdrive.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageResourceNotFoundException extends RuntimeException {
    public StorageResourceNotFoundException() {
        super("Storage resource was not found");
    }
}
