package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import io.github.adam035.desktopfs.application.usecase.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WinFspFileSystemFactory {

    private final RandomAccessService randomAccessService;

    private final FileInfoMapper fileInfoMapper;

    private final GetVolumeUseCase getVolumeUseCase;

    private final ReadDirectoryUseCase readDirectoryUseCase;

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final DownloadFileUseCase downloadFileUseCase;

    private final UploadFileUseCase uploadFileUseCase;

    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final MoveStorageResourceUseCase moveStorageResourceUseCase;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    public WinFspFileSystem createFileSystem(String volumeLabel) {
        return new WinFspFileSystem(
                volumeLabel,
                randomAccessService,
                getVolumeUseCase,
                readDirectoryUseCase,
                createDirectoryUseCase,
                downloadFileUseCase,
                uploadFileUseCase,
                fileInfoMapper,
                getStorageResourceUseCase,
                deleteStorageResourceUseCase,
                moveStorageResourceUseCase
        );
    }

}
