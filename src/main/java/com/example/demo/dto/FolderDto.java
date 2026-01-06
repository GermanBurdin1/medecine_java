package com.example.demo.dto;

public record FolderDto(
    Long id,
    String name,
    Long parentId
) {}
