package com.csc480red.dictionary;

import javax.persistence.Entity;

@Entity
public class ValidWord extends Word {
	
	public ValidWord(String word) {
		super(word);
	}
}
