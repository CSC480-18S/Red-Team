package com.csc480red.dictionary;

public class ValidateResult {
	
	private String word;
	private boolean valid;
	private boolean bad;
	private boolean special;
	
	public ValidateResult(String word, boolean valid, boolean bad, boolean special) {
		super();
		this.word = word;
		this.valid = valid;
		this.bad = bad;
		this.special = special;
	}

	public String getWord() {
		return word;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isBad() {
		return bad;
	}

	public boolean isSpecial() {
		return special;
	}
	
}
