package workflow.process.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import workflow.process.data.model.*;
import workflow.process.exception.DataPersistenceException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProcessService {

    private final DataPersistenceService dataPersistenceService;
    private final FileProcessStorageService fileProcessStorageService;

    @NotNull
    public FileProcessDto processFile(@NotNull final MultipartFile fileToProcess) {
        String s3Path = null;
        try {
            validateFile(fileToProcess);

            s3Path = fileProcessStorageService.pushToS3(fileToProcess);

            final FileProcess newFileProcess = constructFileProcess(s3Path, fileToProcess);
            final Outbox outbox = constructOutbox(newFileProcess);

            final FileProcess upsertedFileProcess = dataPersistenceService.persistData(newFileProcess, outbox);
            return new FileProcessDto(upsertedFileProcess.getUuid(), upsertedFileProcess.getStatus().toString(), upsertedFileProcess.getUpdatedAt().toString());
        } catch (DataPersistenceException dataPersistenceException) {
            log.error("Failed to persist data: ", dataPersistenceException);
            cleanUpS3(s3Path);
            throw new RuntimeException("Failed to process file", dataPersistenceException);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    @NotNull
    public List<FileProcessDto> fetchAllFileProcesses() {
        return dataPersistenceService.fetchAllFileProcesses();
    }

    private void cleanUpS3(@Nullable final String s3Path) {
        if (s3Path != null) {
            fileProcessStorageService.deleteFromS3(s3Path);
        }
    }

    private Outbox constructOutbox(@NotNull final FileProcess fileProcess) {
        final Outbox outbox = new Outbox();
        outbox.setFileUUID(fileProcess.getUuid());
        outbox.setStatus(OutboxStatus.PENDING);
        return outbox;
    }

    private FileProcess constructFileProcess(@NotNull final String s3Path,
                                             @NotNull final MultipartFile fileToProcess) {
        final FileProcess fileProcess = new FileProcess();
        fileProcess.setFileName(fileToProcess.getOriginalFilename());
        fileProcess.setUuid(UUID.randomUUID().toString());
        fileProcess.setStatus(FileStatus.PROCESSING);
        fileProcess.setFileS3Path(s3Path);
        return fileProcess;
    }


    /**
     * Perform basic file validations
     */
    private static void validateFile(MultipartFile fileToProcess) {
        if (fileToProcess.getOriginalFilename() == null) {
            throw new RuntimeException("File name is required");
        }
        if (fileToProcess.getSize() >= 9000000) {
            throw new RuntimeException("File size is too large");
        }
    }

}