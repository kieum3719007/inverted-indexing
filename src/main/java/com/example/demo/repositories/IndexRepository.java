package com.example.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Index;

@Repository
public interface IndexRepository extends MongoRepository<Index, String> {

    Index getByTerm(String key);

}
