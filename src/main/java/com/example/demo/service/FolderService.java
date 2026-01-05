package com.example.demo.service;

import com.example.demo.domain.Folder;
import com.example.demo.repo.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class FolderService {

  private final FolderRepository folderRepository;

  public FolderService(FolderRepository folderRepository) {
    this.folderRepository = folderRepository;
  }

  @Transactional
  public Folder createFolder(String name, Long parentId) {
    Folder parent = null;
    if (parentId != null) {
      parent = folderRepository.findById(parentId)
          .orElseThrow(() -> new IllegalArgumentException("Parent folder not found: " + parentId));
    }

    Folder f = new Folder();
    f.setName(name);
    f.setParent(parent);
    f.setCreatedAt(OffsetDateTime.now()); // если у тебя поле createdAt есть в entity
    return folderRepository.save(f);
  }

  @Transactional(readOnly = true)
  public List<Folder> listFolders() {
    return folderRepository.findAll();
  }
}
