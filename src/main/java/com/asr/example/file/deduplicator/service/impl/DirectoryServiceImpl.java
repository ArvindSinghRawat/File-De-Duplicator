package com.asr.example.file.deduplicator.service.impl;

import com.asr.example.file.deduplicator.dto.request.DirectoryRequest;
import com.asr.example.file.deduplicator.service.DirectoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectoryServiceImpl implements DirectoryService {

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
        return validatePath(request);
    }
}
