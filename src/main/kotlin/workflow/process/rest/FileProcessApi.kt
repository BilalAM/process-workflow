package workflow.process.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import workflow.process.data.model.FileProcessDto
import workflow.process.extensions.toDto
import workflow.process.services.FileProcessService


@RestController
@RequestMapping("/api/v1/file-process")
class FileProcessApi(private val fileProcessService: FileProcessService) {


    @PostMapping("/upload")
    fun uploadFile(file: MultipartFile): ResponseEntity<FileProcessDto> {
        println("File uploaded ${file.print()}")
        val uploaded = fileProcessService.upsertFileProcess(file)
        return ResponseEntity.ok(uploaded)
    }

    @GetMapping("/list")
    fun listJobs(): ResponseEntity<List<FileProcessDto>> {
        val jobs = fileProcessService.findAll().map { it.toDto() }
        return ResponseEntity.ok(jobs)
    }

    @GetMapping("/job/{id}")
    fun viewJob(id: String): ResponseEntity<FileProcessDto> {
        val job = fileProcessService.findById(id.toInt()).toDto()
        return ResponseEntity.ok(job)
    }
}

fun MultipartFile.print(): String {
    return "MultipartFile(name=${this.name}, originalFilename=${this.originalFilename}, contentType=${this.contentType}, size=${this.size})"
}