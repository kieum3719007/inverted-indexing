package com.example.demo.tfidf;

import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.example.demo.TermCounter.SimpleTermCounter;
import com.example.demo.entities.Index;
import com.example.demo.entities.TermFrequency;
import com.example.demo.service.IndexService;

@Component
public class TFIDF {

	private final IndexService indexService;
	private final SimpleTermCounter termCounter;

	public TFIDF(
			IndexService indexService,
			SimpleTermCounter termCounter
			) {
		this.termCounter = termCounter;
		this.indexService = indexService;
	}

	private float getTermFrequency(String term, List<String> documentTerms) {
		final Integer result = termCounter.valuesCount(documentTerms).get(term);
		return result == null ? 0 : result;
	}

	private float getTermFrequency(String term, String documentId) {
		final Index idx = indexService.getByTerm(term);
		final TermFrequency termFrequency = idx.getPostings()
				.stream()
				.filter(item -> item.getDocumentId().equals(documentId))
				.findFirst().orElse(null);
		if (termFrequency == null) {
			return 0;
		}
		else {
			return termFrequency.getFrequency();
		}
	}

	private float getTfIdf(String term, float tf) {
		final TreeMap<String, Integer> DF = indexService.getDocumentFrequencies();

		if (DF.containsKey(term)) {
			final float df = DF.get(term);
			final float N = DF.size();
			return TFIDFUtils.tfidf(tf, df, N);
		} else {
			return 0;
		}
	}

	public float tfidfFromContent(String term, List<String> documentTerms) {
		final float tf = getTermFrequency(term, documentTerms);
		if (tf == 0) {
			return 0;
		}
		else {
			return getTfIdf(term, tf);
		}
	}

	public float tfidfFromId(String term, String documentId) {
		final float tf = getTermFrequency(term, documentId);
		if (tf == 0) {
			return 0;
		}
		else {
			return getTfIdf(term, tf);
		}
	}
}
