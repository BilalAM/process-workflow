package workflow.process.data;


import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;

@Repository
interface FileProcessRepository : CrudRepository<FileProcess, Int> {
    fun findByName(fileName: String): FileProcess?
}
