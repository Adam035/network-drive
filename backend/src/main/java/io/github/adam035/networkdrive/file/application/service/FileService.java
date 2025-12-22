package io.github.adam035.networkdrive.file.application.service;

import io.github.adam035.networkdrive.file.api.dto.FileDownloadResponse;
import io.github.adam035.networkdrive.file.api.dto.FileUploadRequest;
import io.github.adam035.networkdrive.file.application.exception.ResourceNotFoundException;
import io.github.adam035.networkdrive.file.application.mapper.FileUploadMapper;
import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.domain.repository.FileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@AllArgsConstructor
public class FileService {

    private final StorageService storageService;

    private final FileRepository fileRepository;

    private final FileUploadMapper fileUploadMapper;

    @Transactional
    public void uploadFile(FileUploadRequest fileUploadRequest) {
        File file = fileUploadMapper.mapToModel(fileUploadRequest);
        fileRepository.save(file);

        try (InputStream inputStream = fileUploadRequest.file().getInputStream()) {
            storageService.uploadFile(file, inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public FileDownloadResponse downloadFile(String fileId) {
        return fileRepository.findById(fileId)
                .map(file -> {
                    InputStream fileContent = storageService.downloadFile(file.getStorageKey());
                    return new FileDownloadResponse(file, fileContent);
                })
                .orElseThrow(() -> {
                    log.warn("Attempted to download non-existent file: {}", fileId);
                    return new ResourceNotFoundException(String.format("File with ID %s not found", fileId));
                });
    }

}
