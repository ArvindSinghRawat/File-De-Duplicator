package com.asr.example.file.deduplicator.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface AsyncDirectoryService {

    CompletableFuture<Void> saveAllFilesRecursively(Long sourceDirectoryId) throws IOException, ExecutionException, InterruptedException;
}
