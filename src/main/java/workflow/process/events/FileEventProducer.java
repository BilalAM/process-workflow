package workflow.process.events;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class FileEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void produceFileEvent(String fileName) {
        FileProcessEvent event = new FileProcessEvent();
        event.setFileName(fileName);
        rabbitTemplate.convertAndSend("file-events", event);
    }
}