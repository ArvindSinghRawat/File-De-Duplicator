package com.asr.example.file.deduplicator.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "TBL_FILE_DETAILS",
        indexes = {
                @Index(name = "IDX_FILE_NAME", columnList = "FILE_NAME")
        })
@AttributeOverride(name = "id", column = @Column(name = "file_id"))
public class FileDetailsEntity extends AbstractBaseEntity {

    @Column(length = 2048, nullable = false, updatable = false)
    String fileName;

    @Column(length = 2048, nullable = false, updatable = false)
    String completeFilePath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional = false, targetEntity = DirectoryEntity.class)
    @JoinColumn(name = "directory_id", referencedColumnName = "directory_id",
            nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_FILE_TO_DIR"))
    DirectoryEntity directory;
}
