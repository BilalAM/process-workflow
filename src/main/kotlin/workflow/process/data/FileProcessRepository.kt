package workflow.process.data;


import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;
import workflow.process.data.model.FileProcess

@Repository
interface FileProcessRepository : CrudRepository<FileProcess, Int> {
    fun findByFileName(fileName: String): FileProcess?
}
