package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Document;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {

	@Query(value = "{}", fields = "{'_id':0, 'content':1}")
	List<Document> collectAllContent();

	List<Document> getByIsLeader(Boolean isLeader);

	List<Document> getByLeaderId(String id);

	@Aggregation("{'$sample': {'size':?0}}")
	List<Document> randomSample(long n);
}
