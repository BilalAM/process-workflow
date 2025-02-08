package workflow.process.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileProcessStorageService {

    private static final String PROCESSED_FILES_PREFIX = "processed-files/";
    private static final String BUCKET = "workflow-process";
    private final S3Client s3Client;
    private final Logger logger = LoggerFactory.getLogger(FileProcessStorageService.class);

    public FileProcessStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String pushToS3(MultipartFile fileToPush) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .key(PROCESSED_FILES_PREFIX + fileToPush.getOriginalFilename())
                            .bucket(BUCKET)
                            .build(),
                    RequestBody.fromBytes(fileToPush.getBytes())
            );
            return PROCESSED_FILES_PREFIX + fileToPush.getOriginalFilename();
        } catch (Exception e) {
            logger.error("Failed to push file to S3: ", e);
            throw new RuntimeException("Failed to push file to S3", e);
        }
    }

    public void pullFromS3(String url) {
        try {
            s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .key(url)
                            .bucket(BUCKET)
                            .build()
            );
        } catch (Exception e) {
            logger.warn("Unable to pull {} from S3: ", url, e);
        }
    }
}
