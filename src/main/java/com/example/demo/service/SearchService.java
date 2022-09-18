package com.example.demo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.documentvectorspace.DocumentVectorCreator;
import com.example.demo.entities.Document;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class SearchService {

	@Value("${index.consine.top.k}")
	private int K;
	private final DocumentService documentService;
	private final DocumentVectorCreator documentVectorCreator;

	public SearchService(
			DocumentService documentService,
			DocumentVectorCreator documentVectorCreator
			) {
		this.documentService = documentService;
		this.documentVectorCreator = documentVectorCreator;
	}

	private <T> List<T> getTopK(List<T> data)
	{
		if (data.size() <= K) {
			return data;
		}
		else {
			return data.subList(0, K);
		}
	}

	public List<String> search(String query) throws IOException {
		final List<Document> allDocuments = documentService.getAll();
		final ArrayRealVector queryVector = documentVectorCreator.createVectorFromContent(query);

		if (StringUtils.isBlank(query)) {
			return getTopK(allDocuments)
					.stream()
					.map(Document::getContent)
					.collect(Collectors.toList());
		}

		if (queryVector.getL1Norm() == 0) {
			return Collections.emptyList();
		}

		final TreeMap<Float, Document> scores = new TreeMap<Float, Document>();
		for (final Document document : allDocuments) {
			final ArrayRealVector documentVector = documentVectorCreator.createVectorFromContent(document.getContent());
			final float cos = (float) queryVector.cosine(documentVector);
			scores.put(cos, document);
		}

		final List<String> result = scores.descendingKeySet()
				.stream()
				.map(item -> scores.get(item).getContent())
				.collect(Collectors.toList());
		return getTopK(result);
	}
}
