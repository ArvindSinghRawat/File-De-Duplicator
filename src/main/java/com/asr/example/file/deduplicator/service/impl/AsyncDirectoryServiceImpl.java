package com.asr.example.file.deduplicator.service.impl;

import com.asr.example.file.deduplicator.config.ExecutorConfig;
import com.asr.example.file.deduplicator.config.property.ExecutorProperties;
import com.asr.example.file.deduplicator.entity.DirectoryEntity;
import com.asr.example.file.deduplicator.entity.FileDetailsEntity;
import com.asr.example.file.deduplicator.entity.enums.DirectoryStatus;
import com.asr.example.file.deduplicator.repository.DirectoryRepository;
import com.asr.example.file.deduplicator.repository.FileDetailsRepository;
import com.asr.example.file.deduplicator.service.AsyncDirectoryService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

@Slf4j
@Service
@Async
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AsyncDirectoryServiceImpl implements AsyncDirectoryService {

    ExecutorProperties executorProperties;

    DirectoryRepository directoryRepository;

    FileDetailsRepository fileDetailsRepository;

    ExecutorConfig executorConfig;

    @Override
    @Transactional
    public CompletableFuture<Void> saveAllFilesRecursively(final Long sourceDirectoryId)
            throws IOException, ExecutionException, InterruptedException {
        // Get Data from Database
        final DirectoryEntity sourceDir = directoryRepository.findById(sourceDirectoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Details for Source directory with ID: " + sourceDirectoryId + " not found"));
        final Path path = Path.of(sourceDir.getSourceDirectory());
        long batchSize = executorProperties.getMaxBatchSize();
        Set<FileDetailsEntity> entitySet = new LinkedHashSet<>();
        List<CompletableFuture<Void>> futures = new LinkedList<>();
        try (Stream<Path> pathStream = Files.walk(path)) {
            Iterator<Path> iterator = pathStream.iterator();
            while (iterator.hasNext()) {
                // Create entity for each file
                Path currentPath = iterator.next();
                if (Files.isDirectory(currentPath)) {
                    continue;
                }
                entitySet.add(FileDetailsEntity.builder()
                        .fileName(currentPath.getFileName().toString())
                        .completeFilePath(currentPath.toAbsolutePath().toString())
                        .directory(sourceDir)
                        .build());
                if (--batchSize < 1) {
                    // Save each batch to database asynchronously
                    Set<FileDetailsEntity> finalEntitySet = entitySet;
                    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(
                            () -> fileDetailsRepository.saveAll(finalEntitySet),
                            executorConfig.getAsyncExecutor());
                    futures.add(voidCompletableFuture);
                    // Revert to original
                    entitySet = new LinkedHashSet<>();
                    batchSize = executorProperties.getMaxBatchSize();
                }
            }

            // Check if any batch is yet to be saved
            if (batchSize > 0) {
                Set<FileDetailsEntity> finalEntitySet = entitySet;
                CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(
                        () -> fileDetailsRepository.saveAll(finalEntitySet),
                        executorConfig.getAsyncExecutor());
                futures.add(voidCompletableFuture);
            }

            // Wait for all tasks to run
            CompletableFuture
                    .allOf(futures.toArray(CompletableFuture[]::new))
                    .handle((unused, throwable) -> {
                        if (throwable != null) {
                            sourceDir.setStatus(DirectoryStatus.FAILED);
                            sourceDir.setErrors(Set.of(throwable.getClass() + " : " + throwable.getMessage()));
                        } else {
                            sourceDir.setStatus(DirectoryStatus.COMPLETED);
                        }
                        directoryRepository.save(sourceDir);
                        log.info("All {} batches for {} ran successfully",
                                futures.size(), sourceDir.getSourceDirectory());
                        return unused;
                    })
                    .get();
        }
        return null;
    }
}
