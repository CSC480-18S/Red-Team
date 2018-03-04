package com.csc480red.dictionary;

import javax.persistence.Entity;

@Entity
public class BadWord extends Word {
	
	public BadWord(String word) {
		super(word);
	}
}
