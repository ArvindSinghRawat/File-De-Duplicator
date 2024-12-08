package com.asr.example.file.deduplicator.entity;

import com.asr.example.file.deduplicator.entity.enums.DirectoryStatus;
import com.asr.example.file.deduplicator.util.jpa.ErrorListConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "TBL_DIRECTORY_DETAILS")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AttributeOverride(name = "id", column = @Column(name = "directory_id"))
public class DirectoryEntity extends AbstractBaseEntity {

    @Column(nullable = false, updatable = false)
    String sourceDirectory;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    DirectoryStatus status = DirectoryStatus.STARTED;

    @Lob
    @Column(name = "errors", columnDefinition = "CLOB")
    @Convert(converter = ErrorListConverter.class)
    Set<String> errors;
}
