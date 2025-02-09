package workflow.process.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import workflow.process.data.model.FileProcess;

import java.util.Optional;

@Repository
public interface FileProcessRepository extends JpaRepository<FileProcess, Integer> {
    Optional<FileProcess> findByFileName(String fileName);

    Optional<FileProcess> findByUuid(String uuid);


}
