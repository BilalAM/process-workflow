package workflow.process.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import workflow.process.data.FileProcessDto
import workflow.process.data.FileProcessService


@RestController
@RequestMapping("/api/v1/file-process")
class FileProcessApi(private val fileProcessService: FileProcessService) {


    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun uploadFile(file: MultipartFile): ResponseEntity<FileProcessDto> {
        println("File uploaded ${file.print()}")
        val uploaded = fileProcessService.upsertFileProcess(file)
        return ResponseEntity.ok(uploaded)
    }

}

fun MultipartFile.print(): String {
    return "MultipartFile(name=${this.name}, originalFilename=${this.originalFilename}, contentType=${this.contentType}, size=${this.size})"
}