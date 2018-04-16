package com.csc480red.gamedb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
public class PlayedWord {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String word;
	private int value;
	private boolean dirty;
	private boolean special;
	
	@ManyToOne
	@JoinColumn(name="player_id")
	private Player player;
	
	protected PlayedWord() {}

	public PlayedWord(String word, int value, boolean dirty, boolean special, Player player) {
		super();
		this.word = word;
		this.value = value;
		this.dirty = dirty;
		this.special = special;
		this.player = player;
	}
}
