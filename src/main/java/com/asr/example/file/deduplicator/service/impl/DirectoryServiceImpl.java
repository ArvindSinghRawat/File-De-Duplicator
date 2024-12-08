package com.asr.example.file.deduplicator.service.impl;

import com.asr.example.file.deduplicator.dto.request.DirectoryRequest;
import com.asr.example.file.deduplicator.entity.DirectoryEntity;
import com.asr.example.file.deduplicator.repository.DirectoryRepository;
import com.asr.example.file.deduplicator.service.DirectoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectoryServiceImpl implements DirectoryService {

    DirectoryRepository directoryRepository;

    private static Optional<String> validatePath(DirectoryRequest request) {
        Path sourcePath = Path.of(request.getSourcePath().trim());
        if (Files.exists(sourcePath)) {
            if (Files.isDirectory(sourcePath)) {
                return Optional.empty();
            } else {
                return Optional.of("Provided path is not a valid directory");
            }
        } else {
            return Optional.of(String.format("Provided path %s doesn't exist", request.getSourcePath()));
        }
    }

    @Override
    public Optional<String> savePath(DirectoryRequest request) {
        // Validate path
        Optional<String> validationError = validatePath(request);
        if (validationError.isPresent()) {
            return validationError;
        }
        // Save path to database
        try {
            directoryRepository.save(DirectoryEntity.builder().sourceDirectory(request.getSourcePath()).build());
        } catch (DataAccessException ex) {
            log.warn("Exception while saving directory {} in the database, Error: {}",
                    request.getSourcePath(), ex.getMessage(), ex);
            return Optional.of(String.format("Exception while saving directory %s to database, please try again!", request.getSourcePath()));
        }
        return Optional.empty();
    }
}
