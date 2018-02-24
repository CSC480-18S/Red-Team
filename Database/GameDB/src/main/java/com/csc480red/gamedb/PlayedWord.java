package com.csc480red.gamedb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PlayedWord {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String word;
	private int value;
	private int length;
	private boolean standalone;
	private boolean dw;
	private boolean tw;
	private boolean dl;
	private boolean tl;
	
	@OneToOne
	private Player player;
	
	protected PlayedWord() {}

	/**
	 *
	 * @param word - the word that was played
	 * @param value - the value of the played word
	 * @param length = the length of the played word
	 * @param standalone - is the word a standalone word
	 * @param dw - was the word played over a double word tile
	 * @param tw - was the word played over a triple word tile
	 * @param dl - was the word played over a double letter tile
	 * @param tl - was the word played over a triple letter tile
	 * @param player - the player that played the word
	 */
	public PlayedWord(String word, int value, int length, boolean standalone, boolean dw, boolean tw, boolean dl,
			boolean tl, Player player) {
		super();
		this.word = word;
		this.value = value;
		this.length = length;
		this.standalone = standalone;
		this.dw = dw;
		this.tw = tw;
		this.dl = dl;
		this.tl = tl;
		this.player = player;
	}

	/**
	 *
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @return the actual word
	 */
	public String getWord() {
		return word;
	}

	/**
	 *
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 *
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 *
	 * @return is it standalone
	 */
	public boolean isStandalone() {
		return standalone;
	}

	/**
	 *
	 * @return over a double word tile
	 */
	public boolean isDw() {
		return dw;
	}

	/**
	 *
	 * @return over a triple word tile
	 */
	public boolean isTw() {
		return tw;
	}

	/**
	 *
	 * @return over a double letter tile
	 */
	public boolean isDl() {
		return dl;
	}

	/**
	 *
	 * @return over a triple letter tile
	 */
	public boolean isTl() {
		return tl;
	}

	/**
	 *
	 * @return player that played this word
	 */
	public Player getPlayer() {
		return player;
	}

}
