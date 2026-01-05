package com.example.demo.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

// cette classe est un service. Crée la comme un bean et dirige son cycle de vie. Spring créera un objet LocalStorageService lors du start de l'application
@Service
public class LocalStorageService implements StorageService {

	//décrit où se trouve le chemin
  private final Path root;

  public LocalStorageService(@Value("${app.storage.local-root:./uploads}") String rootDir) {
    this.root = Paths.get(rootDir).toAbsolutePath().normalize();
  }

  @Override
  public StoredFile store(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is required and must not be empty");
    }

    // 1) Убедимся, что папка существует
    Files.createDirectories(root);

    // 2) Оригинальное имя (может быть null)
    String originalName = StringUtils.cleanPath(
        file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()
    );

    // 3) MIME type (может быть null)
    String mimeType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();

    // 4) Расширение (если есть)
    String ext = "";
    int dot = originalName.lastIndexOf('.');
    if (dot > -1 && dot < originalName.length() - 1) {
      ext = originalName.substring(dot); // включая точку
      // супер-простая защита от странных расширений
      if (ext.length() > 16) ext = "";
    }

    // 5) Генерируем уникальное имя файла
    String filename = UUID.randomUUID() + ext;

    // 6) Путь назначения
    Path target = root.resolve(filename).normalize();

    // защита от path traversal (на всякий случай)
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid path");
    }

    // 7) Копируем и параллельно считаем SHA-256
		// MessageDigest are secure one-way hash functions that take arbitrary-sized data and output a fixed-length hash value.
    MessageDigest md = sha256Digest();

    long size;
    try (InputStream in = file.getInputStream();
         DigestInputStream din = new DigestInputStream(in, md)) {

      // REPLACE_EXISTING на всякий случай, но UUID почти исключает совпадения
      Files.copy(din, target, StandardCopyOption.REPLACE_EXISTING);
      size = Files.size(target);
    }

    String sha256 = HexFormat.of().formatHex(md.digest());

    // storageKey — это то, что ты будешь хранить в БД
    // Для локального хранения достаточно имени файла или относительного пути
    String storageKey = filename;

    return new StoredFile(storageKey, originalName, mimeType, size, sha256);
  }

  private static MessageDigest sha256Digest() {
    try {
      return MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      // На нормальной JVM такого не бывает
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }
}

