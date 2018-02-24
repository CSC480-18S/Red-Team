package com.csc480red.gamedb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String username;
	private String shortId;
	
	@ManyToOne
	@JoinColumn(name="team_id")
	private Team team;
	
	private String topValueWord;
	private int highestValue;
	private String longestWord;
	private int highestSingleGameScore;
	private String freqPlayedWord;
	private int amountBonusesUsed;
	private int totalScore;
	
	protected Player() {}

	public Player(String username, String shortId, Team team, String topValueWord, int highestValue,
			String longestWord, int highestSingleGameScore, String freqPlayedWord, int amountBonusesUsed,
			int totalScore) {
		super();
		this.username = username;
		this.shortId = shortId;
		this.team = team;
		this.topValueWord = topValueWord;
		this.highestValue = highestValue;
		this.longestWord = longestWord;
		this.highestSingleGameScore = highestSingleGameScore;
		this.freqPlayedWord = freqPlayedWord;
		this.amountBonusesUsed = amountBonusesUsed;
		this.totalScore = totalScore;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getShortId() {
		return shortId;
	}

	public Team getTeam() {
		return team;
	}

	public String getTopValueWord() {
		return topValueWord;
	}

	public int getHighestValue() {
		return highestValue;
	}

	public String getLongestWord() {
		return longestWord;
	}

	public int getHighestSingleGameScore() {
		return highestSingleGameScore;
	}

	public String getFreqPlayedWord() {
		return freqPlayedWord;
	}

	public int getAmountBonusesUsed() {
		return amountBonusesUsed;
	}

	public int getTotalScore() {
		return totalScore;
	}

}
