package com.asr.example.file.deduplicator.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "spring.app.async")
public class ExecutorProperties {
    int corePoolSize;
    int maxPoolSize;
    int queueCapacity;
    String threadNamePrefix;
    long maxBatchSize;

    @ConstructorBinding
    public ExecutorProperties(int corePoolSize, int maxPoolSize, int queueCapacity, String threadNamePrefix, long maxBatchSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueCapacity = queueCapacity;
        this.threadNamePrefix = threadNamePrefix;
        this.maxBatchSize = maxBatchSize;
    }
}
