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
public class GameResult {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="team_id")
	private Team team;
	private int score;
	private boolean win;
	private boolean lose;
	
	protected GameResult() {};
	
	public GameResult(Team team, int score, boolean win, boolean lose) {
		this.team = team;
		this.score = score;
		this.win = win;
		this.lose = lose;
	}
	
	public Team getTeam() {
		return team;
	}

	public int getScore() {
		return score;
	}

	public boolean isWin() {
		return win;
	}

	public boolean isLose() {
		return lose;
	}
	
	@PostPersist
	@PostUpdate
	public void modifyTeam() {
		team.setResultFields();
	}
}
