package io.github.adam035.networkdrive.application.exception;

public class DeviceAlreadyRegisteredException extends RuntimeException {
    public DeviceAlreadyRegisteredException() {
        super("Device is already registered");
    }
}
