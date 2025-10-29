package com.task.download_client2;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
public class FileController {

    //This is to setup the path and name of the file/type
    private final Path filePath = Path.of("HOME", "file_to_download_client2.txt").toAbsolutePath();

    //make the file 100mb thats it
    @PostConstruct
    public void createSampleFile() throws IOException {
        Files.createDirectories(filePath.getParent());
        long targetSize = 100_000_000L;
        if (!Files.exists(filePath) || Files.size(filePath) < targetSize) {
            try (OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                byte[] buffer = new byte[1024 * 1024];
                long written = 0;
                while (written < targetSize) {
                    os.write(buffer);
                    written += buffer.length;
                }
            }
        }
    }

    //for API aka postman to call and save in folder
    @GetMapping("/create-file")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_to_download_client2.txt\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(filePath))
                .body(resource);
    }
}