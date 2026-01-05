package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {}
