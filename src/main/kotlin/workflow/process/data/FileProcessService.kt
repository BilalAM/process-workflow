package workflow.process.data;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile
import workflow.process.extensions.toEntity
import java.time.OffsetDateTime

@Service
class FileProcessService(private val fileProcessRepository: FileProcessRepository) {

    fun upsertFileProcess(multipartFile: MultipartFile): FileProcessDto {
        requireNotNull(multipartFile.originalFilename) { "File name is required" }
        val fileProcess = FileProcessDto(
            fileName = multipartFile.originalFilename!!,
            status = FileStatus.PROCESSED,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now(),
            s3Url = null
        )
        save(fileProcess.toEntity())
        return fileProcess
    }

    fun save(fileProcess: FileProcess): FileProcess {
        return fileProcessRepository.save(fileProcess)
    }

    fun findById(id: Int): FileProcess {
        return fileProcessRepository.findById(id).orElseThrow { RuntimeException("FileProcess not found") }
    }

    fun findAll(): List<FileProcess> {
        return fileProcessRepository.findAll().toCollection(ArrayList())
    }

}
