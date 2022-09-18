package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.example.demo.TermCounter.SimpleTermCounter;
import com.example.demo.entities.Document;
import com.example.demo.entities.Index;
import com.example.demo.entities.TermFrequency;
import com.example.demo.repositories.IndexRepository;
import com.example.demo.tokenizer.TermTokenizer;

@Service
public class IndexService {

	private final IndexRepository indexRepository;
	private final DocumentService documentService;
	private final SimpleTermCounter counter;
	private final TermTokenizer tokenizer;

	/**
	 * Key-value pairs of term and its document frequency
	 */
	private final TreeMap<String, Integer> documentFrequencies;

	@Autowired
	public IndexService(
			IndexRepository indexRepository,
			DocumentService documentService,
			SimpleTermCounter counter,
			TermTokenizer tokenizer
			) throws IOException {
		this.indexRepository = indexRepository;
		this.documentService = documentService;
		this.counter = counter;
		this.tokenizer = tokenizer;

		documentFrequencies = new TreeMap<String, Integer>();
		this.updateDF();
	}

	public Index getByTerm(String term) {
		return indexRepository.getByTerm(term);
	}

	public TreeMap<String, Integer> getDocumentFrequencies() {
		return documentFrequencies;
	}

	public void indexDocument(CommonsMultipartFile file) throws IOException {
		// convert to text
		final String text = IOUtils.toString(file.getInputStream());
		final Document savedDocument = documentService.saveDocument(text);
		final List<String> tokens = tokenizer.tokernize(text);
		final Map<String, Integer> termsCounts = counter.valuesCount(tokens);
		this.mergeIndex(
				termsCounts, savedDocument.getId()
				);
	}

	private List<TermFrequency> insertWithSort(
			List<TermFrequency> currentList,
			TermFrequency newItem
			) {
		for (final TermFrequency termFrequency : currentList) {
			if (termFrequency.getDocumentId().compareTo(newItem.getDocumentId()) > 0) {
				final int i = currentList.indexOf(termFrequency);
				currentList.add(
						i, newItem
						);
				return currentList;
			}
			else {
				// do nothing
			}
		}
		currentList.add(newItem);
		return currentList;
	}

	private void merge(Map.Entry<String, Integer> termCount, String documentId) {
		Index index = indexRepository.getByTerm(termCount.getKey());
		final TermFrequency termFrequency = new TermFrequency();
		termFrequency.setDocumentId(documentId);
		termFrequency.setFrequency(termCount.getValue());
		if (index != null) {
			index.setPostings(
					this.insertWithSort(
							index.getPostings(), termFrequency
							)
					);
		}
		else {
			index = new Index();
			index.setTerm(termCount.getKey());
			index.setPostings(List.of(termFrequency));
		}
		indexRepository.save(index);
	}

	private void mergeIndex(Map<String, Integer> termCounts, String documentId) {
		termCounts.entrySet().stream().forEach(
				item -> this.merge(item, documentId)
				);
	}

	public void updateDF() {
		final List<Index> allTerms = indexRepository.findAll();
		documentFrequencies.clear();
		allTerms.forEach(term -> {
			documentFrequencies.put(term.getTerm(), term.getPostings().size());
		});
	}
}
