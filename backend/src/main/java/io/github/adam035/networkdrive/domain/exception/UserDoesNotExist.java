package io.github.adam035.networkdrive.domain.exception;

public class UserDoesNotExist extends RuntimeException {
    public UserDoesNotExist() {
        super("User does not exist!");
    }
}
