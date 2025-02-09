package workflow.process.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import workflow.process.data.OutboxRepository;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;
import workflow.process.events.FileEventProducer;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {
    private final OutboxRepository outboxRepository;
    private final FileEventProducer fileEventProducer;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void pollOutbox() {
        outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING)
                .forEach(outboxMessage -> {
                    try {
                        fileEventProducer.produceFileEvent(outboxMessage.getFileUUID());
                        updateOutboxStatusToSent(outboxMessage);
                    } catch (Exception e) {
                        log.warn("Unable to send outbox message {}, will be sent in next poller!", outboxMessage.getFileUUID(), e);
                    }
                });
    }

    private void updateOutboxStatusToSent(Outbox outboxMessage) {
        outboxMessage.setStatus(OutboxStatus.SENT);
        outboxRepository.save(outboxMessage);
    }
}
