package com.example.demo.dto;

import com.example.demo.domain.Document;
import com.example.demo.domain.Folder;

public final class DtoMapper {
  private DtoMapper() {}

  public static FolderDto toDto(Folder f) {
    if (f == null) return null;
    Long parentId = (f.getParent() == null) ? null : f.getParent().getId();
    return new FolderDto(f.getId(), f.getName(), parentId);
  }

  public static DocumentDto toDto(Document d) {
    if (d == null) return null;
    Long folderId = (d.getFolder() == null) ? null : d.getFolder().getId();

    return new DocumentDto(
        d.getId(),
        d.getTitle(),
        d.getDocType(),
        d.getDocumentDate(),
        d.getOriginalFilename(),
        d.getMimeType(),
        d.getSizeBytes(),
        folderId,
        d.getUploadedAt()
    );
  }
}

