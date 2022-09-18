package com.example.demo.texttransform;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class DocumentTransform extends ComposeTransform {

	public DocumentTransform() throws IOException {
		this
		.add(new WordSegmenatationTransform())
		.add(new LowerTransform());
	}
}
