package workflow.process.events;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void produceFileEvent(@NotNull final String fileUUID) {
        final FileProcessEvent event = new FileProcessEvent();
        event.setFileUUID(fileUUID);
        rabbitTemplate.convertAndSend("file-events", event);
    }
}