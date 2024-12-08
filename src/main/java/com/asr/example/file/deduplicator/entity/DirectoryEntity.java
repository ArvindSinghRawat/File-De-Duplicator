package com.asr.example.file.deduplicator.entity;

import com.asr.example.file.deduplicator.entity.enums.DirectoryStatus;
import com.asr.example.file.deduplicator.util.jpa.ErrorListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "TBL_DIRECTORY_ENTITY")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AttributeOverride(name = "id", column = @Column(name = "directory_id"))
public class DirectoryEntity extends AbstractBaseEntity {

    String sourceDirectory;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    DirectoryStatus status = DirectoryStatus.STARTED;

    @Lob
    @Column(name = "errors", columnDefinition = "CLOB")
    @Convert(converter = ErrorListConverter.class)
    Set<String> errors;
}
