package com.csc480red.gamedb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="team")
	private List<Player> players;
	
	private int totalScore;
	
	@OneToMany
	private List<Player> topPlayers;
	@OneToMany
	private List<PlayedWord> highestValueWords;
	@OneToOne
	private PlayedWord longestWord;
	@ElementCollection
	private List<WordFrequency> frequentlyPlayedWords;
	@ElementCollection
	private List<WordFrequency> frequentlyPlayedSpecialWords;
	private int dirtyCount;
	private int specialCount;
	@OneToMany(mappedBy="team")
	private List<GameResult> gameResults;
	@OneToMany
	private List<GameResult> highestGameScores;
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
	
	public void setTopPlayers() {
		List<Player> topPlayers = new ArrayList<>(players);
		Collections.sort(topPlayers, (player1, player2) -> player2.getScore() - player1.getScore());
		this.topPlayers = topPlayers.subList(0, topPlayers.size() > 3 ? 3 : topPlayers.size());
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
	
	public void setFrequentlyPlayedSpecialWords() {
		Map<String, Integer> wordFrequencies = new HashMap<>();
		for(Player player : players) {
			for(PlayedWord playedWord : player.getPlayedWords()) {
				if(playedWord.isSpecial()) {
					if(wordFrequencies.containsKey(playedWord.getWord()))
						wordFrequencies.put(playedWord.getWord(), wordFrequencies.get(playedWord.getWord()) + 1);
					else
						wordFrequencies.put(playedWord.getWord(), 1);
				}
			}
		}
		List<String> words = new ArrayList<>(wordFrequencies.keySet());
		Collections.sort(words, (word1, word2) -> wordFrequencies.get(word2) - wordFrequencies.get(word1));
		List<WordFrequency> highestWordFrequencies = new ArrayList<>();
		for(int i = 0; i < (words.size() > 3 ? 3 : words.size()); i++)
			highestWordFrequencies.add(new WordFrequency(words.get(i), wordFrequencies.get(words.get(i))));
		frequentlyPlayedSpecialWords = highestWordFrequencies;
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
	
	public void setWordFields() {
		setTotalScore();
		setTopPlayers();
		setLongestWord();
		setHighestValueWords();
		setFrequentlyPlayedWords();
		setFrequentlyPlayedSpecialWords();
		setDirtyCount();
		setSpecialCount();
	}
	
	public void setWinCount() {
		int winCount = 0;
		for(GameResult result : gameResults)
			if(result.isWin())
				winCount++;
		this.winCount = winCount;
	}
	
	public void setLoseCount() {
		int loseCount = 0;
		for(GameResult result : gameResults)
			if(result.isLose())
				loseCount++;
		this.loseCount = loseCount;
	}
	
	public void setHighestGameScores() {
		List<GameResult> highestGameScores = new ArrayList<>(gameResults);
		Collections.sort(highestGameScores, (result1, result2) -> result2.getScore() - result1.getScore());
		this.highestGameScores = highestGameScores.subList(0, highestGameScores.size() > 5? 5: highestGameScores.size());
	}
	
	public void setResultFields() {
		setWinCount();
		setLoseCount();
		setHighestGameScores();
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getTotalScore() {
		return totalScore;
	}
	
	public List<Player> getTopPlayers() {
		return topPlayers;
	}

	public List<PlayedWord> getHighestValueWords() {
		return highestValueWords;
	}

	public PlayedWord getLongestWord() {
		return longestWord;
	}
	
	public List<WordFrequency> getFrequentlyPlayedWords() {
		return frequentlyPlayedWords;
	}
	
	public List<WordFrequency> getFrequentlySpecialPlayedWords() {
		return frequentlyPlayedSpecialWords;
	}
	
	public int getDirtyCount() {
		return dirtyCount;
	}

	public int getSpecialCount() {
		return specialCount;
	}
	
	public List<GameResult> getGameResults() {
		return gameResults;
	}

	public List<GameResult> getHighestGameScores() {
		return highestGameScores;
	}

	public int getWinCount() {
		return winCount;
	}

	public int getLoseCount() {
		return loseCount;
	}
	
}
