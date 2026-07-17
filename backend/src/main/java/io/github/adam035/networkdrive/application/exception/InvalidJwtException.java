package io.github.adam035.networkdrive.application.exception;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException() {
        super("Invalid or expired JWT token");
    }
}
