package workflow.process.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import workflow.process.data.model.FileProcess;
import workflow.process.data.model.FileProcessDto;
import workflow.process.services.FileProcessService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/file-process")
@RequiredArgsConstructor
public class FileProcessApi {

    private final FileProcessService fileProcessService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<FileProcessDto> uploadFile(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(fileProcessService.processFile(file));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileProcessDto>> listJobs() {

        return ResponseEntity.ok(fileProcessService.fetchAllFileProcesses());
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<FileProcess> viewJob(@PathVariable String id) {
        return ResponseEntity.ok(null);
    }
}