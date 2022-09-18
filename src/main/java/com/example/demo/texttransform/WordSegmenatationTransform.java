package com.example.demo.texttransform;

import java.io.IOException;

import vn.corenlp.wordsegmenter.WordSegmenter;

public class WordSegmenatationTransform implements TextTransform {

    private final WordSegmenter wordSegmenter;

    public WordSegmenatationTransform() throws IOException {
	wordSegmenter = WordSegmenter.initialize();
    }

    @Override
    public String transform(String text) {
	try {
	    return wordSegmenter.segmentTokenizedString(text);
	} catch (final IOException e) {
	    e.printStackTrace();
	    return text;
	}
    }

}
