package workflow.process.events

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import workflow.process.data.FileStatus
import workflow.process.services.FileProcessService
import workflow.process.services.FileProcessStorageService


@Component
class FileEventConsumer(
    private val fileProcessService: FileProcessService,
    private val fileProcessStorageService: FileProcessStorageService
) {


    @RabbitListener(queues = ["file-events"])
    fun consumeFileEvent(fileProcessEvent: FileProcessEvent) {
        println("Event got for <--:::::-->  " + fileProcessEvent.fileName)
        // Step1 : Find the metadata in the DB
        val fileMetadata = fileProcessService.findByName(fileProcessEvent.fileName)

        // Step2: Only process if it has s3URL and the state is UPLOADED
        fileMetadata?.takeIf { it.s3Url != null && it.status == FileStatus.UPLOADED }?.let {

            // Step3: Fetch file from s3
            val s3File = fileProcessStorageService.pullFromS3(it.s3Url!!)
            println("File pulled from s3: ${s3File.absolutePath}")

            // Step4: Mimik process of file
            Thread.sleep(2000)
            // process the file


            // Step5: Set the state to PROCESSED if all is good and persist it
            fileMetadata.status = FileStatus.PROCESSED;
            fileProcessService.save(fileMetadata)

        } ?: run {
            println("File not found or not in correct state")
        }
    }
}