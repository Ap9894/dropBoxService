package com.assignment.typeface.dropboxservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private LocalDateTime createdAt;
    private long size;
    private String fileType;


    @Lob
    private byte[] fileContent;

    // Getters and setters
}
