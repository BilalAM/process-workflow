package workflow.process.services;

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import workflow.process.configuration.Loggable

@Service
class FileProcessStorageService(private val s3Client: S3Client) : Loggable() {

    companion object {
        private const val PROCESSED_FILES_PREFIX = "processed-files/"
        private const val BUCKET = "workflow-process"
    }

    fun pushToS3(fileToPush: MultipartFile): String {
        s3Client.putObject(
            PutObjectRequest.builder()
                .key(PROCESSED_FILES_PREFIX + fileToPush.originalFilename)
                .bucket(BUCKET).build(),
            RequestBody.fromBytes(fileToPush.bytes)
        )
        return "processed-files/" + fileToPush.originalFilename;
    }


    fun pullFromS3(url: String): String? {
        try {
            return s3Client.getObjectAsBytes(
                GetObjectRequest.builder().key(url)
                    .bucket(BUCKET).build()
            ).asUtf8String();
        } catch (e: Exception) {
            logger.warn("Unable to pull $url from s3: ", e)
        }
        return null
    }
}
