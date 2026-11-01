package io.github.adam035.desktopfs.application.port;

import java.io.InputStream;

public interface FilePort {

    byte[] downloadFile(String path, long offset, int length);

    void uploadFile(String path, byte[] bytes);

}
