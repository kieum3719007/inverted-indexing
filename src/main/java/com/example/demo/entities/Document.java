package com.example.demo.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@org.springframework.data.mongodb.core.mapping.Document
public class Document {

	@Id
	private String id;

	private Boolean isLeader;

	private String leaderId;

	private String content;
}
