package com.csc480red.gamedb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Entity
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
		this.word = word;
		this.value = value;
		this.dirty = dirty;
		this.special = special;
		this.player = player;
	}

	public String getWord() {
		return word;
	}

	public int getValue() {
		return value;
	}

	public boolean isDirty() {
		return dirty;
	}

	public boolean isSpecial() {
		return special;
	}

	public Player getPlayer() {
		return player;
	}
	
	@PostPersist
	@PostUpdate
	public void modifyPlayerAndTeam() {
		player.setScore();
		player.getTeam().setTransientFields();
	}
	
}
