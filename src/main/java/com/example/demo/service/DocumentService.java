package com.example.demo.service;

import com.example.demo.domain.Document;
import com.example.demo.domain.DocumentType;
import com.example.demo.domain.Folder;
import com.example.demo.repo.DocumentRepository;
import com.example.demo.repo.FolderRepository;
import com.example.demo.storage.StorageService;
import com.example.demo.storage.StoredFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DocumentService {

	private final StorageService storageService;
	private final DocumentRepository documentRepository;
	private final FolderRepository folderRepository;

	public DocumentService(StorageService storageService,
			DocumentRepository documentRepository,
			FolderRepository folderRepository) {
		this.storageService = storageService;
		this.documentRepository = documentRepository;
		this.folderRepository = folderRepository;
	}

	@Transactional
	public Document upload(MultipartFile file,
			String title,
			DocumentType type,
			LocalDate documentDate,
			Long folderId) throws IOException {

		StoredFile stored = storageService.store(file);

		Folder folder = null;
		if (folderId != null) {
			folder = folderRepository.findById(folderId)
					.orElseThrow(() -> new IllegalArgumentException("Folder not found: " + folderId));
		}

		Document doc = new Document();
		doc.setTitle(title);
		doc.setDocType(type);
		doc.setDocumentDate(documentDate);

		doc.setOriginalFilename(stored.originalFilename());
		doc.setStorageKey(stored.storageKey());
		doc.setMimeType(stored.mimeType());
		doc.setSizeBytes(stored.sizeBytes());
		doc.setChecksumSha256(stored.sha256());

		doc.setFolder(folder);
		doc.setUploadedAt(OffsetDateTime.now());

		return documentRepository.save(doc);
	}

	@Transactional(readOnly = true)
	public List<Document> listDocuments() {
		return documentRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Document getById(Long id) {
		return documentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
	}

	@Transactional(readOnly = true)
	public InputStream openStream(Long id) throws IOException {
		Document doc = getById(id);
		return storageService.open(doc.getStorageKey());
	}

	@Transactional
	public void deleteById(Long id) throws IOException {
		Document doc = documentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));

		// 1) Сначала удалим файл (можно и после БД, но тогда при ошибке файла уже не
		// узнаем)
		storageService.delete(doc.getStorageKey());

		// 2) Потом удалим запись в БД
		documentRepository.delete(doc);
	}

}
