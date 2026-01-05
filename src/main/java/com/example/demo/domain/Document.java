package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "doc_type", nullable = false)
  private DocumentType docType;

  @Column(name = "document_date")
  private LocalDate documentDate;

  @Column(name = "original_filename", nullable = false)
  private String originalFilename;

  @Column(name = "storage_key", nullable = false, length = 500)
  private String storageKey;

  @Column(name = "mime_type", nullable = false, length = 120)
  private String mimeType;

  @Column(name = "size_bytes", nullable = false)
  private Long sizeBytes;

  @Column(name = "checksum_sha256", length = 64)
  private String checksumSha256;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id")
  private Folder folder;

  @Column(name = "uploaded_at", nullable = false)
  private OffsetDateTime uploadedAt;
}

