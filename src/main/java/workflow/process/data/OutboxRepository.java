package workflow.process.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import workflow.process.data.model.Outbox;
import workflow.process.data.model.OutboxStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRepository extends CrudRepository<Outbox, Integer> {
    List<Outbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    Optional<Outbox> findByFileUUID(String fileUUID);
}
