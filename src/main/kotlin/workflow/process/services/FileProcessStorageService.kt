package workflow.process.services;

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import kotlin.random.Random

@Service
class FileProcessStorageService {


    fun pushToS3(fileToPush: MultipartFile): String {
        return "https://s3url-to-file-" + Random.nextLong() + ".csv";
    }

    fun pullFromS3(url: String): File {
        return File.createTempFile("temp", ".csv");
    }
}
