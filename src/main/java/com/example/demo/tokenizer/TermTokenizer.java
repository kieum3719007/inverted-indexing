package com.example.demo.tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.demo.texttransform.TermTransform;

import vn.corenlp.tokenizer.Tokenizer;

@Component
public class TermTokenizer {
	private final TermTransform transform;

	public TermTokenizer(TermTransform transform) {
		this.transform = transform;
	}

	private List<String> joinSegment(List<String> words) {
		final List<String> result = new ArrayList<String>();
		if (CollectionUtils.isEmpty(words)) {
			return result;
		}
		else if (result.size() <= 2) {
			return words;
		}
		else {
			int index = 0;
			while (index < words.size() - 1) {
				final String next = words.get(index + 1);

				if ("_".equals(next)) {
					result.add(words.get(index) + next + words.get(index + 2));
					index += 3;
				}
				else {
					result.add(words.get(index));
					index += 1;
					if (index == words.size() - 1) {
						result.add(words.get(index));
					}
				}
			}
		}
		return result;
	}


	public List<String> tokernize(String text) throws IOException {
		text = transform.transform(text);
		List<String> tokens = Tokenizer.tokenize(text);
		tokens = this.joinSegment(tokens);
		return tokens;
	}
}
