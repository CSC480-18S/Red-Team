package com.csc480red.gamedb;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String topValueWord;
	private int highestValue;
	private String longestWord;
	private int higestSingleGameScore;
	private String freqPlayedWord;
	private int amountBonusesUsed;
	private int totalScore;
	private int winCount;
	private int loseCount;
	
	@OneToMany(mappedBy="team")
	private List<Player> players;
	
	protected Team() {}

	/**
	 *
	 * @param name - name
	 * @param topValueWord - highest score word
	 * @param highestValue - highest word score
	 * @param longestWord - longest word
	 * @param highestSingleGameScore - highest single game score
	 * @param freqPlayedWord - most frequently played word
	 * @param amountBonusesUsed - total amount of bonuses used
	 * @param totalScore - total score
	 * @param winCount - win count
	 * @param loseCount - lose count
	 */
	public Team(String name, String topValueWord, int highestValue, String longestWord, int highestSingleGameScore,
			String freqPlayedWord, int amountBonusesUsed, int totalScore, int winCount, int loseCount) {
		super();
		this.name = name;
		this.topValueWord = topValueWord;
		this.highestValue = highestValue;
		this.longestWord = longestWord;
		this.higestSingleGameScore = highestSingleGameScore;
		this.freqPlayedWord = freqPlayedWord;
		this.amountBonusesUsed = amountBonusesUsed;
		this.totalScore = totalScore;
		this.winCount = winCount;
		this.loseCount = loseCount;
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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return highest score word
	 */
	public String getTopValueWord() {
		return topValueWord;
	}

	/**
	 *
	 * @return highest word score
	 */
	public int getHighestValue() {
		return highestValue;
	}

	public String getLongestWord() {
		return longestWord;
	}

	/**
	 *
	 * @return highest single game score
	 */
	public int getHigestSingleGameScore() {
		return higestSingleGameScore;
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
	 * @return total amount of bonuses used
	 */
	public int getAmountBonusesUsed() {
		return amountBonusesUsed;
	}

	/**
	 *
	 * @return total score
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 *
	 * @return win count
	 */
	public int getWinCount() {
		return winCount;
	}

	/**
	 *
	 * @return lose count
	 */
	public int getLoseCount() {
		return loseCount;
	}
	
}
