package com.example.demo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import lombok.NoArgsConstructor;

@org.springframework.data.mongodb.core.mapping.Document
@NoArgsConstructor
@Data
public class Index {
    @Id
    private String id;

    @Indexed(unique = true)
    private String term;

    private List<TermFrequency> postings;
}
