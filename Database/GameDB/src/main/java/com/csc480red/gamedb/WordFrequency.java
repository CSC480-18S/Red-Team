package com.csc480red.gamedb;

public class WordFrequency {
	
	private final String word;
	private final int frequency;
	
	public WordFrequency(String word, int frequency) {
		super();
		this.word = word;
		this.frequency = frequency;
	}

	public String getWord() {
		return word;
	}

	public int getFrequency() {
		return frequency;
	}
	
}
