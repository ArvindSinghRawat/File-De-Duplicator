package com.asr.example.file.deduplicator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackages = {"com.asr.example.file.deduplicator.repository"})
@EnableJpaAuditing(auditorAwareRef = "auditAwareBean")
public class DataSourceConfig {

    @Bean
    public AuditorAware<String> auditAwareBean() {
        return () -> Optional.of("system");
    }
}
