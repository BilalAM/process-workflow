package workflow.process.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import workflow.process.data.FileProcessRepository;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.FileStatus;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProcessService {

    private final FileProcessRepository fileProcessRepository;

    public void processFile(MultipartFile fileToProcess) {
        try {
            validateFile(fileToProcess);
            constructFileProcess(fileToProcess);


        } catch (Exception e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    private FileProcess constructFileProcess(MultipartFile fileToProcess) {
        FileProcess fileProcess = new FileProcess();
        fileProcess.setFileName(fileToProcess.getOriginalFilename());
        fileProcess.setUuid(UUID.randomUUID().toString());
        fileProcess.setStatus(FileStatus.PROCESSING);
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