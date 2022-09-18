package com.example.demo.texttransform;

public class ExcludePucntuationTransform implements TextTransform {

    @Override
    public String transform(String text) {
	text = text.replaceAll("(?:--|[\\[\\]{}()+/\\\\\\.])", "");
	return text;
    }

}
