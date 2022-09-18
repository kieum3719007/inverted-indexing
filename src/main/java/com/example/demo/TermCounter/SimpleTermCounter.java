package com.example.demo.TermCounter;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class SimpleTermCounter implements TermCounterInterface {

	@Override
	public Map<String, Integer> valuesCount(List<String> tokens) {
		final Set<String> terms = new HashSet<String>(tokens);
		final Map<String, Integer> result = new HashMap<String, Integer>();
		for (final String term : terms) {
			final int count = Collections.frequency(terms, term);
			result.put(term, count);
		}
		return result;
	}

}
