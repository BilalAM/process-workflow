package workflow.process.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import workflow.process.data.FileProcessRepository;
import workflow.process.data.model.FileStatus;


@Component
@Slf4j
@RequiredArgsConstructor
public class FileEventConsumer {
    private final FileProcessRepository fileProcessRepository;

    @RabbitListener(queues = "file-events")
    public void consumeFileEvent(FileProcessEvent event) {
        log.info("Recieved event:", event.fileUUID);
        fileProcessRepository.findByUuid(event.fileUUID)
                .ifPresent(fileProcess -> {
                    // then finally update the status
                    fileProcess.setStatus(FileStatus.PROCESSED);
                    fileProcessRepository.save(fileProcess);
                });
    }
}