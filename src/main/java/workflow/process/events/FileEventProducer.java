package workflow.process.events;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void produceFileEvent(String fileUUID) {
        FileProcessEvent event = new FileProcessEvent();
        event.setFileUUID(fileUUID);
        rabbitTemplate.convertAndSend("file-events", event);
    }
}