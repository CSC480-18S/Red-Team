package com.csc480red.gamedb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class TeamTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TeamRepository teams;

  @Autowired
  PlayerRepository players;

  /**
   * adds a Green and Gold team to the Team Repository
   * adds a player to the Player Repository
   */
  @Before
  public void setup() {
    Team gold = new Team("Gold");
    Team green = new Team("Green");
    Player player = new Player("brendan", "", gold);
    entityManager.persist(gold);
    entityManager.persist(green);

    entityManager.persist(player);
  }

  /**
   * should find both the Green and Gold teams that
   * where added during setup
   */
  @Test
  public void teamByName() {
    Team gold = teams.findByName("Gold").get(0);
    Team green = teams.findByName("Green").get(0);
    assertNotNull(gold);
    assertNotNull(green);
  }

  /**
   * should return the correct size of the Team Repository
   */
  @Test
  public void allTeams() {
    List<Team> allTeams = teams.findAll();
    assertTrue(allTeams.size() == 2);
  }

  /**
   * should return that finding an invalid team
   * does not exist
   */
  @Test
  public void invalidTeam() {
    List<Team> invalidTeams = teams.findByName("doesnotexist");
    assert (invalidTeams.size() == 0);
  }

  /**
   * should return the correct size of the Team Repository
   * by searching for an empty name
   */
  @Test
  public void emptyTeamName() {
    List<Team> invalidTeams = teams.findByName("");
    assertTrue(invalidTeams.size() == 0);
  }

  /**
   * should return the correct size of the Team Repository
   * by searching for a null name
   */
  @Test
  public void nullTeamName() {
    List<Team> invalidTeams = teams.findByName(null);
    assertTrue(invalidTeams.size() == 0);
  }

  /**
   * should return the team win count was incremented
   * by 1 after adding a win
   */
  @Test
  public void addWin() {
    Team gold = teams.findAll().get(0);
    GameResult res = new GameResult(gold, 100, true, false);
    gold.getGameResults().add(res);
    assertTrue(gold.getWinCount() == 0);
    gold.setWinCount();
    assertTrue(gold.getWinCount() == 1);
  }

  /**
  * should return the team loss count was incremented
  * by 1 after adding a loss
  */
  @Test
  public void addLoss() {
    Team gold = teams.findAll().get(0);
    GameResult res = new GameResult(gold, 100, false, true);
    gold.getGameResults().add(res);
    assertTrue(gold.getLoseCount() == 0);
    gold.setLoseCount();
    assertTrue(gold.getLoseCount() == 1);
  }

  /**
   * should return that the a dirty word was played
   * and the team's dirty word count was incremented by 1
   */
  @Test
  public void playDirtyWord() {
    Team gold = teams.findAll().get(0);
    Player player = players.findByUsername("brendan").get(0);
    PlayedWord word = new PlayedWord("shit", 0, true, false, player);
    gold.getPlayers().add(player);
    player.getPlayedWords().add(word);
    assertTrue(gold.getDirtyCount() == 0);
    gold.setDirtyCount();
    assertTrue(gold.getDirtyCount() == 1);
  }

  /**
   * should return that the a special word was played
   * and the team's special word count was incremented by 1
   */
  @Test
  public void playSpecialWord() {
    Team gold = teams.findAll().get(0);
    Player player = players.findByUsername("brendan").get(0);
    PlayedWord word = new PlayedWord("oswego", 0, false, true, player);
    gold.getPlayers().add(player);
    player.getPlayedWords().add(word);
    assertTrue(gold.getSpecialCount() == 0);
    gold.setSpecialCount();
    assertTrue(gold.getSpecialCount() == 1);
  }

}