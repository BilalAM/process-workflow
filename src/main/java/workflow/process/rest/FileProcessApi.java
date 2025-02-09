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

    @PostMapping("/upload")
    public ResponseEntity<FileProcessDto> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileProcessService.processFile(file));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileProcess>> listJobs() {

        return ResponseEntity.ok(null);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<FileProcess> viewJob(@PathVariable String id) {
        return ResponseEntity.ok(null);
    }
}