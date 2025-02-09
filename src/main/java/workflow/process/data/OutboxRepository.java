package workflow.process.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import workflow.process.data.model.Outbox;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRepository extends CrudRepository<Outbox, Integer> {

    @Query(value = "SELECT * from postgres.public.outbox o WHERE o.status =:status " +
                   "order by o.created_at desc limit 10 for update skip locked ",
            nativeQuery = true)
    List<Outbox> findAndLockOutboxMessages(String status);

    Optional<Outbox> findByFileUUID(String fileUUID);
}
