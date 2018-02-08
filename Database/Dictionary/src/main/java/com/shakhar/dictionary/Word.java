package com.shakhar.dictionary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Word {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String word;
	
	protected Word() {}

	public Word(String word) {
		this.word = word;
	}

	public Long getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

}
