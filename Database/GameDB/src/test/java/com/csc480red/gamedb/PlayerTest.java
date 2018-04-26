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
public class PlayerTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  PlayerRepository players;

  /**
  * adds a Gold team to the Team Repository
  * adds a player to the Player Repository
  */
  @Before
  public void setup() {
    Team gold = new Team("Gold");
    Player player = new Player("brendan", "", gold);
    entityManager.persist(gold);
    entityManager.persist(player);
  }

  /**
   * should find the first player in the Player Repository
   */
  @Test
  public void getPlayer() {
    Player player = players.findByUsername("brendan").get(0);
    assertNotNull(player);
  }

  /**
   * should not find a player in the Player Repository with
   * the name 'doesnotexist' and the result of the query
   * has length 0
   */
  @Test
  public void playerDoesNotExist() {
    List<Player> temp = players.findByUsername("doesnotexist");
    assertTrue(temp.size() == 0);
  }

  /**
  * should not find a player in the Player Repository with
  * an empty name and the result of the query
  * has length 0
  */
  @Test
  public void playerWithEmptyName() {
    List<Player> temp = players.findByUsername("");
    assertTrue(temp.size() == 0);
  }

  /**
  * should not find a player in the Player Repository with
  * a null name and the result of the query
  * has length 0
  */
  @Test
  public void playerWithNullName() {
    List<Player> temp = players.findByUsername(null);
    assertTrue(temp.size() == 0);
  }

  /**
   * should allow player to play word, and
   * updated the counter for words played
   */
  @Test
  public void playsWord() {
    Player player = players.findByUsername("brendan").get(0);
    assertTrue(player.getPlayedWords().size() == 0);
    player.getPlayedWords().add(new PlayedWord("cool", 100, false, false, player));
    assertTrue(player.getPlayedWords().size() == 1);
  }

}