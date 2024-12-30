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

        // Step1: Create initial entry in DB
        val fileProcess = save(
            createFileProcess(fileName, null, FileStatus.QUEUED).toEntity()
        ).toDto()

        try {

            // Step2: Upload on s3
            val s3Url = uploadToS3();
            fileProcess.status = FileStatus.UPLOADED
            fileProcess.s3Url = s3Url

            // Step3: After s3 upload, we upsert the entry back with s3 url and UPLOADED status
            save(fileProcess.toEntity())


            // Step4: We enqueue event to amq. Status PROCESSING will be done by CONSUMER
            enqueueEventInAmqp(fileProcess.fileName)

        } catch (e: Exception) {
            throw e;
        }
        return fileProcess
    }

    private fun uploadToS3(): String {
        return "https://file_path.csv";
    }

    private fun createFileProcess(fileName: String, s3Url: String?, status: FileStatus): FileProcessDto {
        val fileProcess = FileProcessDto(
            fileName = fileName,
            s3Url = s3Url,
            status = status
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
