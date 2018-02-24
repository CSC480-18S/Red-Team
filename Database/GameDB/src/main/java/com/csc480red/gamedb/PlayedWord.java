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

	public Long getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

	public int getValue() {
		return value;
	}

	public int getLength() {
		return length;
	}

	public boolean isStandalone() {
		return standalone;
	}

	public boolean isDw() {
		return dw;
	}

	public boolean isTw() {
		return tw;
	}

	public boolean isDl() {
		return dl;
	}

	public boolean isTl() {
		return tl;
	}

	public Player getPlayer() {
		return player;
	}

}
