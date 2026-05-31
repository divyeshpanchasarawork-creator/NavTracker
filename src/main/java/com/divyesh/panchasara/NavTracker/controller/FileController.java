package com.divyesh.panchasara.NavTracker.controller;

import com.divyesh.panchasara.NavTracker.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/nav")
public class FileController {
    private static final String FILE_DIR =
            System.getProperty("user.dir") + File.separator + "uploads";

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
            ) {
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please select a file to upload.");
        }

        try {
            File uploadDir = new File(FILE_DIR);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath =
                    FILE_DIR + File.separator + file.getOriginalFilename();

            File dest = new File(filePath);

            file.transferTo(dest);

            fileService.processFile(filePath);

            return ResponseEntity.ok("File Processed Successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not store the file: " + e.getMessage());
        }
    }
}
