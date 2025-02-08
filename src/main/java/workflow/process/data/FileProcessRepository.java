package workflow.process.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import workflow.process.data.model.FileProcess;

import java.util.Optional;

@Repository
public interface FileProcessRepository extends CrudRepository<FileProcess, Integer> {
    Optional<FileProcess> findByFileName(String fileName);
}
