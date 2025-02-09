package workflow.process.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workflow.process.data.FileProcessRepository;
import workflow.process.data.OutboxRepository;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.Outbox;
import workflow.process.exception.DataPersistenceException;

@Service
@RequiredArgsConstructor
public class DataPersistenceService {
    private final OutboxRepository outboxRepository;
    private final FileProcessRepository fileProcessRepository;

    @Transactional(rollbackFor = DataPersistenceException.class)
    public void persistData(FileProcess fileProcess, Outbox outbox) {
        try {
            fileProcessRepository.save(fileProcess);
            outboxRepository.save(outbox);
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to persist data");
        }
    }
}
