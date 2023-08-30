package com.assignment.typeface.dropboxservice.repository;

import com.assignment.typeface.dropboxservice.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
