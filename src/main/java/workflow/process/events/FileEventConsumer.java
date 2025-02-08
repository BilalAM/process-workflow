package workflow.process.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class FileEventConsumer {

    @RabbitListener(queues = "file-process")
    public void consumeFileEvent(FileProcessEvent event) {
        log.info("Recieved event: ${fileProcessEvent.fileName}");
    }
}