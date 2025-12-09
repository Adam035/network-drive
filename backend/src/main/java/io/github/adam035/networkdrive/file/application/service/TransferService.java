package io.github.adam035.networkdrive.file.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {

    private final StorageService storageService;

}
