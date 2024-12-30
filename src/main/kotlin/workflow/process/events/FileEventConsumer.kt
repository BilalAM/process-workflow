package workflow.process.events

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import workflow.process.data.FileProcessService


@Component
class FileEventConsumer(private val fileProcessService: FileProcessService) {


    @EventListener(FileProcessEvent::class)
    fun consumeFileEvent(fileProcessEvent: FileProcessEvent) {
        // get the file process by name
        // validate the file
        // 'process' the file
    }
}