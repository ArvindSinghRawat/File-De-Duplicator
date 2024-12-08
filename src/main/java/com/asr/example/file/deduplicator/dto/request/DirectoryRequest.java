package com.asr.example.file.deduplicator.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectoryRequest {

    @NotEmpty(message = "Field `sourcePath` couldn't be empty")
    String sourcePath;
}
