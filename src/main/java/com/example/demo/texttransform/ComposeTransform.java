package com.example.demo.texttransform;

import java.util.ArrayList;
import java.util.List;

public class ComposeTransform implements TextTransform {

	private List<TextTransform> transforms;

	public ComposeTransform add(TextTransform transform) {

		if (transforms == null) {
			transforms = new ArrayList<TextTransform>();
		} else {
			// do nothing
		}

		transforms.add(transform);
		return this;
	}

	@Override
	public String transform(String text) {
		for (final TextTransform transform : transforms) {
			text = transform.transform(text);
		}
		return text;
	}
}
