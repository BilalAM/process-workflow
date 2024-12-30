package workflow.process.events

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service


@Service
/**
 * Produces file events to rabbitmq
 */
class FileEventProducer(private val rabbitTemplate: RabbitTemplate) {

    fun produceFileEvent(fileName: String) {
        rabbitTemplate.convertAndSend("file-events", FileProcessEvent(fileName))
    }
}