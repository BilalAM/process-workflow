package workflow.process.data;


import jakarta.persistence.OptimisticLockException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import workflow.process.events.FileEventProducer
import workflow.process.extensions.toDto
import workflow.process.extensions.toEntity

@Service
class FileProcessService(
    private val fileProcessRepository: FileProcessRepository,
    private val fileEventProducer: FileEventProducer
) {

    @Transactional(rollbackFor = [Exception::class])
    fun upsertFileProcess(multipartFile: MultipartFile): FileProcessDto {
        val fileName = multipartFile.originalFilename ?: throw IllegalArgumentException("File name is required")
        checkFileNameNotInUse(fileName)
        // upload file to s3 immediately
        val fileProcess = createFileProcess(fileName, s3Url)

        // upload file to s3 immediately
        save(fileProcess.toEntity()) // first save the process in UPLOADED state

        enqueueEventInAmqp(fileName) // enqueue the event to be processed asynchronously

        fileProcess.status = FileStatus.QUEUED
        save(fileProcess.toEntity()) // save the process as QUEUED

        return fileProcess
    }

    private fun createFileProcess(fileName: String, s3Url: String): FileProcessDto {
        val fileProcess = FileProcessDto(
            fileName = fileName,
            s3Url = s3Url,
            status = FileStatus.UPLOADED
        )
        return fileProcess
    }

    private fun checkFileNameNotInUse(fileName: String) {
        val existingFileProcess = findByName(fileName)
        if (existingFileProcess != null) {
            throw IllegalArgumentException("File name is already in use.")
        }
    }

    fun enqueueEventInAmqp(fileName: String) {
        fileEventProducer.produceFileEvent(fileName)
    }

    fun save(fileProcess: FileProcess): FileProcess {
        try {
            return fileProcessRepository.save(fileProcess)
        } catch (e: OptimisticLockException) {
            throw RuntimeException("Error uploading file process.")
        }
    }

    fun findByName(fileName: String): FileProcessDto? {
        return fileProcessRepository.findByName(fileName)?.toDto()
    }

    fun findById(id: Int): FileProcess {
        return fileProcessRepository.findById(id).orElseThrow { RuntimeException("FileProcess not found") }
    }

    fun findAll(): List<FileProcess> {
        return fileProcessRepository.findAll().toCollection(ArrayList())
    }

}
