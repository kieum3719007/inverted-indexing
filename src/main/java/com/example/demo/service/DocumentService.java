package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Document;
import com.example.demo.repositories.DocumentRepository;
import com.example.demo.texttransform.DocumentTransform;

@Service
public class DocumentService {
	private final DocumentRepository repository;
	private final DocumentTransform documentTransform;

	public DocumentService(
			DocumentRepository repository,
			DocumentTransform documentTransform
			) {
		this.repository = repository;
		this.documentTransform = documentTransform;
	}

	public List<Document> getAll() {
		return repository.findAll();
	}

	public Document getById(String id) {
		return repository.findById(id).orElse(null);
	}

	public List<Document> getLeaders() {
		return repository.getByIsLeader(true);
	}

	public Document saveDocument(String content) {
		final Document doc = new Document();
		doc.setContent(documentTransform.transform(content));
		return repository.save(doc);
	}

	public List<String> search(String searchText) {
		return repository.collectAllContent()
				.stream()
				.map(Document::getContent)
				.collect(Collectors.toList());
	}
}
