package com.assignment.typeface.dropboxservice.controller;

import com.assignment.typeface.dropboxservice.entity.FileEntity;
import com.assignment.typeface.dropboxservice.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/")
    public String showTemplate() {
        Context context = new Context();
        // You can add any necessary variables to the context here

        String renderedHtml = templateEngine.process("index", context);
        return renderedHtml;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileName") String fileName) {
        // Handle file upload and return file ID
        if (file.getSize() == 0) {
            return ResponseEntity.badRequest().body("File Not Saved");
        }
        try {
            fileService.uploadFile(file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File Not Saved");
        }
        return ResponseEntity.ok("File Saved");
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFileById(@PathVariable Long fileId) {
        if (fileId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Optional<FileEntity> opfileEntity = fileService.getFileById(fileId);
            if (opfileEntity.isPresent()) {
                FileEntity fileEntity = opfileEntity.get();
                byte[] fileContent = fileEntity.getFileContent();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileEntity.getFileName());

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(fileContent);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<String> updateFile(@PathVariable Long fileId,
                                                 @RequestParam("file") MultipartFile newFile) {
        // Update file binary data or metadata
        if (fileId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            fileService.updateFile(fileId, newFile);
            return ResponseEntity.ok("File Updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File Not Updated");
        }

    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        // Delete file by ID
        if (fileId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok().body("File Deleted !!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> listFiles() {
        // Retrieve a list of file metadata
        try {
            List<FileEntity> fileEntityList = fileService.listAllFiles();
            return ResponseEntity.ok(fileEntityList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
