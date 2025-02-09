package workflow.process.services;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workflow.process.data.FileProcessRepository;
import workflow.process.data.OutboxRepository;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.FileProcessDto;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;
import workflow.process.exception.DataPersistenceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataPersistenceService {
    private final OutboxRepository outboxRepository;
    private final FileProcessRepository fileProcessRepository;

    @Transactional(rollbackFor = DataPersistenceException.class)
    public FileProcess persistData(@NotNull final FileProcess fileProcess,
                                   @NotNull final Outbox outbox) {
        try {
            fileProcessRepository.findByFileName(fileProcess.getFileName())
                    .ifPresent(process -> {
                        fileProcess.setId(process.getId());
                    });
            FileProcess upsertedFileProcess = fileProcessRepository.save(fileProcess);

            outboxRepository.findByFileUUID(outbox.getFileUUID())
                    .ifPresent(outboxMessage -> {
                        outbox.setId(outboxMessage.getId());
                        outbox.setStatus(OutboxStatus.PENDING);
                    });
            outboxRepository.save(outbox);
            return upsertedFileProcess;
        } catch (Exception e) {
            throw new DataPersistenceException("Failed to persist data", e);
        }
    }

    public List<FileProcessDto> fetchAllFileProcesses() {
        return fileProcessRepository.findAll().stream()
                .map(fileProcess -> new FileProcessDto(fileProcess.getFileName(),
                        fileProcess.getStatus().toString(),
                        fileProcess.getUpdatedAt().toString()))
                .toList();
    }
}
