package io.github.adam035.networkdrive.application.exception;

public class FileUploadException extends RuntimeException {
    public FileUploadException() {
        super("Failed to upload file");
    }
}
