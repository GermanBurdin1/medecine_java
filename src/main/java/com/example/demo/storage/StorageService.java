package com.example.demo.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
  StoredFile store(MultipartFile file) throws IOException;

  InputStream open(String storageKey) throws IOException;

  void delete(String storageKey) throws IOException;   // ✅ добавить
}

