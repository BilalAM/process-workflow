package workflow.process.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProcessStorageService {

    private static final String PROCESSED_FILES_PREFIX = "processed-files/";
    private static final String BUCKET = "workflow-process";
    private final S3Client s3Client;

    public String pushToS3(@NotNull final MultipartFile fileToPush) {
        try {
//            s3Client.putObject(
//                    PutObjectRequest.builder()
//                            .key(PROCESSED_FILES_PREFIX + fileToPush.getOriginalFilename())
//                            .bucket(BUCKET)
//                            .build(),
//                    RequestBody.fromBytes(fileToPush.getBytes())
//            );
            return PROCESSED_FILES_PREFIX + fileToPush.getOriginalFilename();
        } catch (Exception e) {
            log.error("Failed to push file to S3: ", e);
            throw new RuntimeException("Failed to push file to S3", e);
        }
    }

    public void pullFromS3(@NotNull final String url) {
        try {
            s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .key(url)
                            .bucket(BUCKET)
                            .build()
            );
        } catch (Exception e) {
            log.warn("Unable to pull {} from S3: ", url, e);
        }
    }

    public void deleteFromS3(@NotNull final String url) {
        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(url)
                            .build()
            );
        } catch (Exception e) {
            log.warn("Unable to delete {} from S3: ", url, e);
            throw e;
        }
    }
}
