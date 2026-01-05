package com.example.demo.storage;

// qu'est-ce qu'on a après la sauvegarde du fichier
public record StoredFile(
    String storageKey,
    String originalFilename,
    String mimeType,
    long sizeBytes,
    String sha256
) {}
