package com.example.demo.texttransform;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class TermTransform extends ComposeTransform {

	public TermTransform() throws IOException {
		this
		.add(new WordSegmenatationTransform())
		.add(new LowerTransform())
		.add(new ExcludePucntuationTransform());
	}
}
