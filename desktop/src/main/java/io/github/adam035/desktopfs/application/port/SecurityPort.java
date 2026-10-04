package io.github.adam035.desktopfs.application.port;

public interface SecurityPort {

    void updateSecurityDescriptor(String path, byte[] securityDescriptor);

}
