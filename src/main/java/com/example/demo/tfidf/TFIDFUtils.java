package com.example.demo.tfidf;

public class TFIDFUtils {

	public static float idf(float documentFrequency, float totalDocument) {
		return (float) Math.log10(totalDocument / documentFrequency);
	}

	public static float tf(float termFrequency) {
		if (termFrequency > 0) {
			return (float) (1 + Math.log10(termFrequency));
		}
		else {
			return 0;
		}
	}

	public static float tfidf(
			float termFreq,
			float docFreq,
			float N
	) {
		return tf(termFreq) * idf(docFreq, N);
	}
}
