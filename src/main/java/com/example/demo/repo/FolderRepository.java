package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {}
