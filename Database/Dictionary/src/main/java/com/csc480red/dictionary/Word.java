package com.csc480red.dictionary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/**
 * Represents a data entity containing a word.
 * 
 * @author Shakhar Dasgupta
 *
 */
@Entity
@Inheritance
public abstract class Word {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String word;
	
	protected Word() {}

	/**
	 * Constructs a {@code Word} object representing a word.
	 * @param word the word to be represented
	 */
	public Word(String word) {
		this.word = word;
	}

	/**
	 * Returns the id of this {@code Word}.
	 * @return the id of this {@code Word}
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the word represented by this {@code Word}.
	 * @return the word represented by this {@code Word}
	 */
	public String getWord() {
		return word;
	}

}
