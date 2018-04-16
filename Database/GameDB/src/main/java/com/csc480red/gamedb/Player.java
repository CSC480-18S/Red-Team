package com.csc480red.gamedb;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;

import lombok.Getter;

@Entity
@Getter
public class Player {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String username;
	private String macAddr;
	
	@ManyToOne
	@JoinColumn(name="team_id")
	private Team team;
	
	@OneToMany(mappedBy="player")
	private List<PlayedWord> playedWords;
	
	private int score;
	
	protected Player() {}

	public Player(String username, String macAddr, Team team) {
		super();
		this.username = username;
		this.macAddr = macAddr;
		this.team = team;
	}

	@PostLoad
	public void setScore() {
		int score = 0;
		for(PlayedWord word : playedWords) {
			score += word.getValue();
		}
		this.score = score;
	}

}
