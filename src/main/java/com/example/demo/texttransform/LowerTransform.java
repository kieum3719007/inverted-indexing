package com.example.demo.texttransform;

public class LowerTransform implements TextTransform {
    @Override
    public String transform(String text) {
	return text.toLowerCase();
    }
}
