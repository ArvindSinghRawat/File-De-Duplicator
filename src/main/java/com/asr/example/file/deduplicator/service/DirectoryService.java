package com.asr.example.file.deduplicator.service;

import com.asr.example.file.deduplicator.dto.request.DirectoryRequest;

import java.util.Optional;

public interface DirectoryService {
    /**
     * Saves the provided path in the Database
     *
     * @param request Request containing details of the source directory
     * @return An optional error (if any), otherwise empty
     */
    Optional<String> savePath(DirectoryRequest request);
}
