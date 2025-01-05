package workflow.process.services;

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class FileProcessStorageService(private val s3Client: S3Client) {


    fun pushToS3(fileToPush: MultipartFile): String {
        s3Client.putObject(
            PutObjectRequest.builder()
                .key("processed-files/" + fileToPush.originalFilename)
                .bucket("workflow-process").build(),
            RequestBody.fromBytes(fileToPush.bytes)
        )
        return "processed-files/" + fileToPush.originalFilename;
    }


    fun pullFromS3(url: String): String? {
        try {
            return s3Client.getObjectAsBytes(
                GetObjectRequest.builder().key(url)
                    .bucket("workflow-process").build()
            ).asUtf8String();
        } catch (e: Exception) {
            println(e)
        }
        return null
    }
}
