package com.example.demo.controller;

import com.example.demo.domain.Folder;
import com.example.demo.service.FolderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

  private final FolderService folderService;

  public FolderController(FolderService folderService) {
    this.folderService = folderService;
  }

  @PostMapping
  public Folder create(
      @RequestParam("name") String name,
      @RequestParam(value = "parentId", required = false) Long parentId
  ) {
    return folderService.createFolder(name, parentId);
  }

  @GetMapping
  public List<Folder> list() {
    return folderService.listFolders();
  }
}

