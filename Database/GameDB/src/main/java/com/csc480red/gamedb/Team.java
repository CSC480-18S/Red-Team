package com.csc480red.gamedb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import lombok.Getter;

@Entity
@Getter
public class Team {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="team")
	private List<Player> players;
	
	@Transient
	private int totalScore;
	@Transient
	@OneToMany
	private List<PlayedWord> highestValueWords;
	@Transient
	@OneToOne
	private PlayedWord longestWord;
	@Transient
	@OneToMany
	private List<WordFrequency> frequentlyPlayedWords;
	@Transient
	private int dirtyCount;
	@Transient
	private int specialCount;
	private int higestSingleGameScore;
	private int winCount;
	private int loseCount;
	
	
	
	protected Team() {}

	public Team(String name) {
		super();
		this.name = name;
	}
	
	public void setTotalScore() {
		int totalScore = 0;
		for(Player player : players)
			totalScore += player.getScore();
		this.totalScore = totalScore;
	}
	
	public void setLongestWord() {
		PlayedWord longestWord = null;
		for(Player player : players)
			for(PlayedWord playedWord : player.getPlayedWords())
				if(longestWord == null || playedWord.getWord().length() > longestWord.getWord().length())
					longestWord = playedWord;
		this.longestWord = longestWord;
	}
	
	public void setHighestValueWords() {
		List<PlayedWord> allPlayedWords = new ArrayList<>();
		for(Player player : players)
			for(PlayedWord playedWord : player.getPlayedWords())
				allPlayedWords.add(playedWord);
		Collections.sort(allPlayedWords, (word1, word2) -> word2.getValue() - word1.getValue());
		highestValueWords = allPlayedWords.subList(0, allPlayedWords.size() > 5 ? 5 : allPlayedWords.size());
	}
	
	public void setFrequentlyPlayedWords() {
		Map<String, Integer> wordFrequencies = new HashMap<>();
		for(Player player : players) {
			for(PlayedWord playedWord : player.getPlayedWords()) {
				if(wordFrequencies.containsKey(playedWord.getWord()))
					wordFrequencies.put(playedWord.getWord(), wordFrequencies.get(playedWord.getWord()) + 1);
				else
					wordFrequencies.put(playedWord.getWord(), 1);
			}
		}
		List<String> words = new ArrayList<>(wordFrequencies.keySet());
		Collections.sort(words, (word1, word2) -> wordFrequencies.get(word2) - wordFrequencies.get(word1));
		List<WordFrequency> highestWordFrequencies = new ArrayList<>();
		for(int i = 0; i < (words.size() > 3 ? 3 : words.size()); i++)
			highestWordFrequencies.add(new WordFrequency(words.get(i), wordFrequencies.get(words.get(i))));
		frequentlyPlayedWords = highestWordFrequencies;
	}
	
	public void setDirtyCount() {
		int dirtyCount = 0;
		for(Player player : players)
			for(PlayedWord playedWord : player.getPlayedWords())
				if(playedWord.isDirty())
					dirtyCount++;
		this.dirtyCount = dirtyCount;
	}
	
	public void setSpecialCount() {
		int specialCount = 0;
		for(Player player : players)
			for(PlayedWord playedWord : player.getPlayedWords())
				if(playedWord.isSpecial())
					specialCount++;
		this.specialCount = specialCount;
	}
	
	@PostLoad
	public void setTransientFields() {
		setTotalScore();
		setLongestWord();
		setHighestValueWords();
		setFrequentlyPlayedWords();
		setDirtyCount();
		setSpecialCount();
	}
}
