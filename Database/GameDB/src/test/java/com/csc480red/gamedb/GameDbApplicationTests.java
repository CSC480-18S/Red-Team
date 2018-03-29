package com.csc480red.gamedb;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Value;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class GameDbApplicationTests {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	/**
	 * Initializes the store with a player
	 */
	@Before
	public void setup() throws Exception {
		mvc = webAppContextSetup(webApplicationContext).build();
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		String team_name = rand.nextInt(2) == 0 ? "Gold" : "Green";
		String name = "TestPlayer420";
		Team team = _getTeam(team_name);
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder builder = post("/players").contentType(MediaType.APPLICATION_JSON_UTF8).content(
				mapper.writeValueAsString(new Player(name, "temp_id", team, "fun", 42, "human", 420, "cool", 5, 4200)));
		mvc.perform(builder).andReturn();
	}

	/**
	 * performs a GET request on /teams
	 */
	@Test
	public void getTeams() throws Exception {
		MockHttpServletRequestBuilder builder = get("/teams");
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		ResultActions res = mvc.perform(builder);
		res.andExpect(jsonPath("$._embedded.teams[0].name", is("Gold")));
		res.andExpect(jsonPath("$._embedded.teams[1].name", is("Green")));
	}

	/**
	 * performs a POST request for adding a player
	 * with a valid username
	 */
	@Test
	public void addPlayerWithValidUsername() throws Exception {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		String team_name = rand.nextInt(2) == 0 ? "Gold" : "Green";
		String name = "TestPlayer";
		Team team = _getTeam(team_name);
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder builder = post("/players").contentType(MediaType.APPLICATION_JSON_UTF8).content(
				mapper.writeValueAsString(new Player(name, "temp_id", team, "fun", 42, "human", 420, "cool", 5, 4200)));
		mvc.perform(builder).andExpect(status().is(201));
	}

	/**
	* performs a POST request for adding a player
	* with a null username
	*/
	@Test
	public void addPlayerWithInvalideUsername() throws Exception {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		String team_name = rand.nextInt(2) == 0 ? "Gold" : "Green";
		String name = null;
		Team team = _getTeam(team_name);
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder builder = post("/players").contentType(MediaType.APPLICATION_JSON_UTF8).content(
				mapper.writeValueAsString(new Player(name, "temp_id", team, "fun", 42, "human", 420, "cool", 5, 4200)));
		mvc.perform(builder).andExpect(status().is(201));
	}

	/**
	* performs a POST request for adding a player
	* with an innapropriate username
	*/
	@Test
	public void addPlayerWithInnapropriateUsername() throws Exception {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		String team_name = rand.nextInt(2) == 0 ? "Gold" : "Green";
		String name = "fuckThisName";
		Team team = _getTeam(team_name);
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder builder = post("/players").contentType(MediaType.APPLICATION_JSON_UTF8).content(
				mapper.writeValueAsString(new Player(name, "temp_id", team, "fun", 42, "human", 420, "cool", 5, 4200)));
		mvc.perform(builder).andExpect(status().is(201));
	}

	/**
	 * performs a GET request on a player
	 * given a username that exists
	 */
	@Test
	public void playerExists() throws Exception {
		MockHttpServletRequestBuilder builder = get("/players/search/findByUsername?username=TestPlayer420");
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		MvcResult res = mvc.perform(builder).andReturn();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
		JsonNode player = root.get("_embedded").get("players").get(0);
		ObjectNode obj = (ObjectNode) player;
		obj.remove("_links");
		String pTemp = mapper.writeValueAsString(obj);
		Player p = mapper.readValue(pTemp, Player.class);
		assertNotNull(p);
	}

	/**
	* performs a GET request on a player
	* given a username that does not exists
	*/
	@Test
	public void playerDoesNotExist() throws Exception {
		MockHttpServletRequestBuilder builder = get("/players/search/findByUsername?username=thisshouldfail");
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		MvcResult res = mvc.perform(builder).andReturn();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
		JsonNode player = root.get("_embedded").get("players").get(0);
		assertNull(player);
	}

	/**
	 * helper function to get a team based on the name using
	 * jackson and removing un-mapped values from the 
	 * response (i.e _links)
	 * @param {String} team_name team name to see if exists
	 * @return Team based on the team_name
	 */
	private Team _getTeam(String team_name) throws Exception {
		MockHttpServletRequestBuilder builder = get("/teams/search/findByName?name=" + team_name);
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		MvcResult res = mvc.perform(builder).andReturn();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
		JsonNode team = root.get("_embedded").get("teams").get(0);
		ObjectNode obj = (ObjectNode) team;
		obj.remove("_links");
		return mapper.treeToValue(obj, Team.class);
	}

}
