package com.example.demo.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// frontière entre la logique métier et réalisation technique du storage
public interface StorageService {
  StoredFile store(MultipartFile file) throws IOException;
}