package com.csc480red.gamedb;

import javax.persistence.Embeddable;

@Embeddable
public class WordFrequency {
	
	private String word;
	private int frequency;
	
	public WordFrequency() {}
	
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
