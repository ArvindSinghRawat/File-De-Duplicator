package com.asr.example.file.deduplicator;

import com.asr.example.file.deduplicator.config.property.ExecutorProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExecutorProperties.class})
public class FileDeDuplicatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileDeDuplicatorApplication.class, args);
    }

}
