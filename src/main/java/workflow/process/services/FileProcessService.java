package workflow.process.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.FileStatus;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;
import workflow.process.exception.DataPersistenceException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProcessService {

    private final DataPersistenceService dataPersistenceService;
    private final FileProcessStorageService fileProcessStorageService;

    public void processFile(MultipartFile fileToProcess) {
        String s3Path = null;
        try {
            validateFile(fileToProcess);

            s3Path = fileProcessStorageService.pushToS3(fileToProcess);

            FileProcess fileProcess = constructFileProcess(s3Path, fileToProcess);
            Outbox outbox = constructOutbox(fileProcess);

            dataPersistenceService.persistData(fileProcess, outbox);
        } catch (DataPersistenceException dataPersistenceException) {
            log.error("Failed to persist data: ", dataPersistenceException);
            cleanUpS3(s3Path);
            throw new RuntimeException("Failed to process file", dataPersistenceException);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    private void cleanUpS3(String s3Path) {
        if (s3Path != null) {
            fileProcessStorageService.deleteFromS3(s3Path);
        }
    }

    private Outbox constructOutbox(FileProcess fileProcess) {
        Outbox outbox = new Outbox();
        outbox.setFileUUID(fileProcess.getUuid());
        outbox.setStatus(OutboxStatus.PENDING);
        return outbox;
    }

    private FileProcess constructFileProcess(String s3Path, MultipartFile fileToProcess) {
        FileProcess fileProcess = new FileProcess();
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