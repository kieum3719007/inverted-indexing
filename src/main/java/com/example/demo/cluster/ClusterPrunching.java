package com.example.demo.cluster;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.stereotype.Component;

import com.example.demo.documentvectorspace.DocumentVectorCreator;
import com.example.demo.entities.Document;
import com.example.demo.repositories.DocumentRepository;

@Component
public class ClusterPrunching {

	private final DocumentRepository documentRepository;
	private final DocumentVectorCreator vectorCreator;

	public ClusterPrunching(
			DocumentRepository documentRepository,
			DocumentVectorCreator vectorCreator
			) {
		this.vectorCreator = vectorCreator;
		this.documentRepository = documentRepository;
	}

	private Map<Document, ArrayRealVector> calculateDocumentVector(List<Document> documents) {
		final Map<Document, ArrayRealVector> result = new HashMap<Document, ArrayRealVector>();
		documents.stream().forEach(document -> {
			try {
				final ArrayRealVector vector = vectorCreator.createVectorFromContent(document.getContent());
				result.put(document, vector);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
		return result;
	}

	private Document findNearestLeaders(
			ArrayRealVector vector,
			Map<Document, ArrayRealVector> leaderVectors
			) {
		Document result = leaderVectors.keySet().iterator().next();
		double similarity = 0;
		for (final Document leader : leaderVectors.keySet()) {
			final double cosine = leaderVectors.get(leader).cosine(vector);
			if (cosine > similarity) {
				result = leader;
				similarity = cosine;
			}
		}
		return result;
	}

	public List<Document> nearestCluster(ArrayRealVector queryVector) throws IOException {
		if (queryVector.getL1Norm() == 0) {
			return Collections.emptyList();
		} else {
			final List<Document> leaders = documentRepository.getByIsLeader(true);
			final Map<Document, ArrayRealVector> leaderVectors = this.calculateDocumentVector(leaders);
			final Document nearestLeader = this.findNearestLeaders(queryVector, leaderVectors);
			final List<Document> nearestCluster = documentRepository.getByLeaderId(nearestLeader.getId());
			return nearestCluster;
		}
	}

	private List<Document> pickLeaders() {
		final long totalDocuments = documentRepository.count();
		final long numLeaders = (long) Math.sqrt(totalDocuments);
		final List<Document> leaders = documentRepository.randomSample(numLeaders);
		leaders.forEach(item -> {
			item.setIsLeader(true);
			item.setLeaderId(item.getId());
		});
		return documentRepository.saveAll(leaders);
	}

	public void prunch() {
		final List<Document> leaders = this.pickLeaders();
		final List<Document> followers = documentRepository.getByIsLeader(null);

		final Map<Document, ArrayRealVector> leaderVectors = this.calculateDocumentVector(leaders);
		final Map<Document, ArrayRealVector> followerVectors = this.calculateDocumentVector(followers);

		followerVectors.entrySet().stream().forEach(entry -> {
			final Document leader = this.findNearestLeaders(entry.getValue(), leaderVectors);
			entry.getKey().setLeaderId(leader.getId());
		});

		documentRepository.saveAll(followerVectors.keySet());
	}

}
