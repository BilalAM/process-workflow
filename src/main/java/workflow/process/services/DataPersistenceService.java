package workflow.process.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workflow.process.data.FileProcessRepository;
import workflow.process.data.OutboxRepository;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;
import workflow.process.exception.DataPersistenceException;

@Service
@RequiredArgsConstructor
public class DataPersistenceService {
    private final OutboxRepository outboxRepository;
    private final FileProcessRepository fileProcessRepository;

    @Transactional(rollbackFor = DataPersistenceException.class)
    public void persistData(FileProcess fileProcess, Outbox outbox) {
        try {
            fileProcessRepository.findByFileName(fileProcess.getFileName())
                    .ifPresent(process -> {
                        fileProcess.setId(process.getId());
                        fileProcess.setUuid(process.getUuid());
                    });
            fileProcessRepository.save(fileProcess);

            outboxRepository.findByFileUUID(outbox.getFileUUID())
                    .ifPresent(outboxMessage -> {
                        outbox.setId(outboxMessage.getId());
                        outbox.setStatus(OutboxStatus.PENDING);
                    });
            outboxRepository.save(outbox);
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to persist data");
        }
    }
}
