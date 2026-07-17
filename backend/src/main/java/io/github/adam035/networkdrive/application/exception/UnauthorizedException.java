package io.github.adam035.networkdrive.application.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("User is not authorized to perform this action");
    }
}
