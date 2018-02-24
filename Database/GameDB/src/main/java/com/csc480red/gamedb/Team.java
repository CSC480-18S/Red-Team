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

	public Team(String name, String topValueWord, int highestValue, String longestWord, int higestSingleGameScore,
			String freqPlayedWord, int amountBonusesUsed, int totalScore, int winCount, int loseCount) {
		super();
		this.name = name;
		this.topValueWord = topValueWord;
		this.highestValue = highestValue;
		this.longestWord = longestWord;
		this.higestSingleGameScore = higestSingleGameScore;
		this.freqPlayedWord = freqPlayedWord;
		this.amountBonusesUsed = amountBonusesUsed;
		this.totalScore = totalScore;
		this.winCount = winCount;
		this.loseCount = loseCount;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public int getHigestSingleGameScore() {
		return higestSingleGameScore;
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

	public int getWinCount() {
		return winCount;
	}

	public int getLoseCount() {
		return loseCount;
	}
	
}
