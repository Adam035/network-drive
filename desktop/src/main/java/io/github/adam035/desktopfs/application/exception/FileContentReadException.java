package io.github.adam035.desktopfs.application.exception;

public class FileContentReadException extends RuntimeException {
    public FileContentReadException(String path) {
        super("Failed to read file content: " + path);
    }
}
