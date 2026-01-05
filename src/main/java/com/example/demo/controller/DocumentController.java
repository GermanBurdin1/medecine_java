package com.example.demo.controller;

import com.example.demo.domain.Document;
import com.example.demo.domain.DocumentType;
import com.example.demo.service.DocumentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
  public Document upload(
      @RequestParam("file") MultipartFile file,
      @RequestParam("title") String title,
      @RequestParam("type") DocumentType type,
      @RequestParam(value = "documentDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate documentDate,
      @RequestParam(value = "folderId", required = false) Long folderId
  ) throws IOException {

    return documentService.upload(file, title, type, documentDate, folderId);
  }

  @GetMapping
  public List<Document> list() {
    return documentService.listDocuments();
  }
}
