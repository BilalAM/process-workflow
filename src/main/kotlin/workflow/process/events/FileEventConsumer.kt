package workflow.process.events

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import workflow.process.configuration.Loggable
import workflow.process.data.FileStatus
import workflow.process.services.FileProcessService
import workflow.process.services.FileProcessStorageService


@Component
class FileEventConsumer(
    private val fileProcessService: FileProcessService,
    private val fileProcessStorageService: FileProcessStorageService
) : Loggable() {


    @RabbitListener(queues = ["file-events"])
    fun consumeFileEvent(fileProcessEvent: FileProcessEvent) {
        logger.info("Recieved event: ${fileProcessEvent.fileName}")
        // Step1 : Find the metadata in the DB
        val fileMetadata = fileProcessService.findByName(fileProcessEvent.fileName)

        // Step2: Only process if it has s3URL and the state is UPLOADED
        fileMetadata?.takeIf { it.s3Url != null && it.status == FileStatus.UPLOADED }?.let {

            // Step3: Fetch file from s3
            val content = fileProcessStorageService.pullFromS3(it.s3Url!!)
            if (content == null) {
                logger.warn("Unable to pull file from s3 :${fileProcessEvent.fileName}")
                fileMetadata.status = FileStatus.FAILED
                fileProcessService.save(fileMetadata)
                return
            }

            // Step4: Mimik process of file
            Thread.sleep(2000)
            // process the file


            // Step5: Set the state to PROCESSED if all is good and persist it
            fileMetadata.status = FileStatus.PROCESSED;
            fileProcessService.save(fileMetadata)
            logger.info("Processed file ${fileMetadata.fileName}")

        } ?: run {
            logger.error("File meta not found or incorrect state: ${fileProcessEvent.fileName}")
            val fileMetadataa = fileProcessService.findByName(fileProcessEvent.fileName)
            if (fileMetadataa != null) {
                fileMetadataa.status = FileStatus.FAILED
                fileProcessService.save(fileMetadataa)
                return
            }
        }
    }
}