package com.example.demo.controller;

import com.example.demo.domain.Document;
import com.example.demo.domain.DocumentType;
import com.example.demo.dto.DocumentDto;
import com.example.demo.dto.DtoMapper;
import com.example.demo.service.DocumentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

  private final DocumentService documentService;

  public DocumentController(DocumentService documentService) {
    this.documentService = documentService;
  }

  @PostMapping("/upload")
  public DocumentDto upload(
      @RequestParam("file") MultipartFile file,
      @RequestParam("title") String title,
      @RequestParam("type") DocumentType type,
      @RequestParam(value = "documentDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate documentDate,
      @RequestParam(value = "folderId", required = false) Long folderId
  ) throws IOException {

    Document saved = documentService.upload(file, title, type, documentDate, folderId);
    return DtoMapper.toDto(saved);
  }

  @GetMapping
  public List<DocumentDto> list() {
    return documentService.listDocuments()
        .stream()
        .map(DtoMapper::toDto)
        .toList();
  }

  @GetMapping("/{id}")
  public DocumentDto get(@PathVariable Long id) {
    return DtoMapper.toDto(documentService.getById(id));
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<InputStreamResource> download(@PathVariable Long id) throws IOException {
    Document doc = documentService.getById(id);
    InputStream in = documentService.openStream(id);

    InputStreamResource resource = new InputStreamResource(in);

    String safeName = doc.getOriginalFilename() == null ? "file" : doc.getOriginalFilename().replace("\"", "");
    String mime = (doc.getMimeType() == null || doc.getMimeType().isBlank())
        ? "application/octet-stream"
        : doc.getMimeType();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeName + "\"")
        .contentType(MediaType.parseMediaType(mime))
        .contentLength(doc.getSizeBytes() == null ? -1 : doc.getSizeBytes())
        .body(resource);
  }
}

