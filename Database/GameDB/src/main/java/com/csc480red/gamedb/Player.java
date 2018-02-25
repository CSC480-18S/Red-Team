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

	/**
	 *
	 * @param username - username
	 * @param shortId - shortID
	 * @param team - team
	 * @param topValueWord - highest valued word
	 * @param highestValue - highest word score
	 * @param longestWord - longest word
	 * @param highestSingleGameScore - single game high score
	 * @param freqPlayedWord - most frequently played word
	 * @param amountBonusesUsed - amount of bonuses the player has used
	 * @param totalScore - total score across all games played
	 */
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

	/**
	 *
	 * @return ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 *
	 * @return shortID
	 */
	public String getShortId() {
		return shortId;
	}

	/**
	 *
	 * @return team
	 */
	public com.csc480red.gamedb.Team getTeam() {
		return team;
	}

	/**
	 *
	 * @return highest valued word
	 */
	public String getTopValueWord() {
		return topValueWord;
	}

	/**
	 *
	 * @return highest value word score
	 */
	public int getHighestValue() {
		return highestValue;
	}

	/**
	 *
	 * @return longest word
	 */
	public String getLongestWord() {
		return longestWord;
	}

	/**
	 *
	 * @return highest single game score
	 */
	public int getHighestSingleGameScore() {
		return highestSingleGameScore;
	}

	/**
	 *
	 * @return most frequently played word
	 */
	public String getFreqPlayedWord() {
		return freqPlayedWord;
	}

	/**
	 *
	 * @return total amount of bonuses played
	 */
	public int getAmountBonusesUsed() {
		return amountBonusesUsed;
	}

	/**
	 *
	 * @return total score across all games
	 */
	public int getTotalScore() {
		return totalScore;
	}

}
