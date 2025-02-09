package workflow.process.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import workflow.process.data.OutboxRepository;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;
import workflow.process.events.FileEventProducer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {
    private final OutboxRepository outboxRepository;
    private final FileEventProducer fileEventProducer;

    @Scheduled(fixedRate = 4, timeUnit = TimeUnit.SECONDS)
    public void pollOutbox() {
        List<Outbox> eligibleMessages = outboxRepository.findAndLockOutboxMessages(OutboxStatus.PENDING.name());
        if (!eligibleMessages.isEmpty()) {
            log.info("Initial ID {} , Last ID {}", eligibleMessages.getFirst().getId(), eligibleMessages.getLast().getId());
        }
        eligibleMessages.forEach(outboxMessage -> {
            try {
                updateOutboxStatus(outboxMessage, OutboxStatus.IN_PROGRESS);
                fileEventProducer.produceFileEvent(outboxMessage.getFileUUID());
                updateOutboxStatus(outboxMessage, OutboxStatus.SENT);
            } catch (Exception e) {
                log.warn("Unable to send outbox message {}, will be sent in next poller!", outboxMessage.getFileUUID(), e);
            }
        });
    }

    private void updateOutboxStatus(Outbox outboxMessage, OutboxStatus status) {
        outboxMessage.setStatus(status);
        outboxRepository.save(outboxMessage);
    }
}
