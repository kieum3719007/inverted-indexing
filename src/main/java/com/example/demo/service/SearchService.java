package com.example.demo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.cluster.ClusterPrunching;
import com.example.demo.documentvectorspace.DocumentVectorCreator;
import com.example.demo.entities.Document;

@Service
public class SearchService {

	@Value("${index.consine.top.k}")
	private int K;

	@Value("${index.consine.threshold}")
	private float consineThreshold;
	private final DocumentService documentService;
	private final DocumentVectorCreator documentVectorCreator;
	private final ClusterPrunching cluster;

	public SearchService(
			ClusterPrunching cluster,
			DocumentService documentService,
			DocumentVectorCreator documentVectorCreator
			) {
		this.cluster = cluster;
		this.documentService = documentService;
		this.documentVectorCreator = documentVectorCreator;
	}

	private List<String> findFromCluster(ArrayRealVector queryVector, List<Document> cluster) throws IOException {
		final TreeMap<Float, Document> scores = new TreeMap<Float, Document>();
		for (final Document document : cluster) {
			final ArrayRealVector documentVector = documentVectorCreator.createVectorFromContent(document.getContent());
			final float cos = (float) queryVector.cosine(documentVector);
			scores.put(cos, document);
		}

		final List<String> result = scores.descendingKeySet()
				.stream()
				.filter(item -> item > consineThreshold)
				.map(item -> scores.get(item).getContent())
				.collect(Collectors.toList());
		return getTopK(result);
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

	public List<String> prunchingSearch(String query) throws IOException {
		final ArrayRealVector queryVector = documentVectorCreator.createVectorFromContent(query);

		if (queryVector.getL1Norm() == 0) {
			return Collections.emptyList();
		} else {
			final List<Document> cluster = this.cluster.nearestCluster(queryVector);
			return findFromCluster(queryVector, cluster);
		}
	}

	public List<String> search(String query) throws IOException {
		final ArrayRealVector queryVector = documentVectorCreator.createVectorFromContent(query);

		if (queryVector.getL1Norm() == 0) {
			return Collections.emptyList();
		} else {
			final List<Document> cluster = documentService.getAll();
			return findFromCluster(queryVector, cluster);
		}
	}
}
