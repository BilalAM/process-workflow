package workflow.process.events

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import workflow.process.data.FileProcessService


@Component
class FileEventConsumer(private val fileProcessService: FileProcessService) {


    @RabbitListener(queues = ["file-events"])
    fun consumeFileEvent(fileProcessEvent: FileProcessEvent) {
        println(fileProcessEvent.fileName)
        // get the file process by name
        // validate the file
        // 'process' the file
    }
}