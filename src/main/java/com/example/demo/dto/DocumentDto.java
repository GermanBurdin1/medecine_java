package com.example.demo.dto;

import com.example.demo.domain.DocumentType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record DocumentDto(
    Long id,
    String title,
    DocumentType type,
    LocalDate documentDate,

    String originalFilename,
    String mimeType,
    Long sizeBytes,

    Long folderId,
    OffsetDateTime uploadedAt
) {}

