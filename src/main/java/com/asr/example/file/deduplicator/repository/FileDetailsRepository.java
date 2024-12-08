package com.asr.example.file.deduplicator.repository;

import com.asr.example.file.deduplicator.entity.FileDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetailsEntity, Long> {
}
