package com.assignment.typeface.dropboxservice.service;

import com.assignment.typeface.dropboxservice.entity.FileEntity;
import com.assignment.typeface.dropboxservice.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public void uploadFile(MultipartFile file) throws IOException {
        // Handle file upload and metadata storage

//        data sanitization can be done
//        checks on file size, file type etc.
        FileEntity fileEntity = new FileEntity();

        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setFileContent(file.getBytes());

        try {
//           additional check if file already exists
//            if file exists raise exception
//            else save to repository
            fileRepository.save(fileEntity);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Optional<FileEntity> getFileById(Long fileId) {
        // Retrieve file entity by ID
//        validate id
        try {
            return fileRepository.findById(fileId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void updateFile(Long fileId, MultipartFile newFile) throws IOException {
        // Update file binary data or metadata
        FileEntity newFileEntity = getFileById(fileId).get();

        newFileEntity.setFileName(newFile.getOriginalFilename());
        newFileEntity.setSize(newFile.getSize());
        newFileEntity.setFileType(newFile.getContentType());
        newFileEntity.setFileContent(newFile.getBytes());

        try {
            fileRepository.save(newFileEntity);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void deleteFile(Long fileId) {

        // Delete file by ID
        try {
            fileRepository.deleteById(fileId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<FileEntity> listAllFiles() {
        // Retrieve all files' metadata
        try {
            return fileRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
