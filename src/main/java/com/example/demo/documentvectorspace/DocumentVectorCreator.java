package com.example.demo.documentvectorspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.stereotype.Component;

import com.example.demo.service.IndexService;
import com.example.demo.tfidf.TFIDF;
import com.example.demo.tokenizer.TermTokenizer;

@Component
public class DocumentVectorCreator {

	private final IndexService indexService;
	private final TermTokenizer tokenizer;
	private final TFIDF tfidf;

	public DocumentVectorCreator(
			IndexService indexService,
			TermTokenizer tokenizer,
			TFIDF tfidf
			) {
		this.tfidf = tfidf;
		this.indexService = indexService;
		this.tokenizer = tokenizer;
	}

	public ArrayRealVector createVectorFromContent(String query) throws IOException {

		final double[] data = new double[indexService.getDocumentFrequencies().size()];

		final List<String> terms = tokenizer.tokernize(query);
		final List<String> vocabulary = new ArrayList<String>(
				indexService.getDocumentFrequencies().keySet()
				);

		terms.stream()
		.forEach(term -> {
			final int termIndex = vocabulary.indexOf(term);
			if (termIndex == -1) {
				return; // not found
			}
			else {// found
				data[termIndex] = tfidf.tfidfFromContent(term, terms);
			}

		});
		return new ArrayRealVector(data);
	}
}
