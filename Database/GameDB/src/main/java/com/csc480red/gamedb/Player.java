package com.csc480red.gamedb;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Player {

	/*
	*@Id indicates the required '_id' field
	*@GeneratedValue marks the field is automatically generated
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;
	private String macAddr;

	//@ManyToOne indicates the many to one relation to team (many players on a team)
	//@JoinColumn indicates that the specified entity is the owner of the relation (team has many players)
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	//@OneToMany indicates relation one to many relation to playedWords (Player has many played words)
	@OneToMany(mappedBy = "player")
	private List<PlayedWord> playedWords;

	private int score;

	protected Player() {
		playedWords = new ArrayList<>();
	}

	public Player(String username, String macAddr, Team team) {
		super();
		this.username = username;
		this.macAddr = macAddr;
		this.team = team;
		this.score = 0;
		playedWords = new ArrayList<>();
	}

	public void setScore() {
		int score = 0;
		for (PlayedWord word : playedWords) {
			if (!word.isDirty())
				score += word.getValue();
		}
		this.score = score;
	}

	public String getUsername() {
		return username;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public Team getTeam() {
		return team;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}

	public int getScore() {
		return score;
	}

}
