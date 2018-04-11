package com.csc480red.dictionary;

import javax.persistence.Entity;

@Entity
public class SpecialWord extends Word {
	
	public SpecialWord(String word) {
		super(word);
	}
}
