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
public class TeamTests {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  /**
   * Initializes the store with a the gold and green teams
   */
  @Before
  public void setup() throws Exception {
    mvc = webAppContextSetup(webApplicationContext).build();
    ThreadLocalRandom rand = ThreadLocalRandom.current();
    String gold_team = "Gold";
    String green_team = "Green";
    ObjectMapper mapper = new ObjectMapper();
    MockHttpServletRequestBuilder gold_builder = post("/teams").contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(mapper.writeValueAsString(new Team(gold_team, "fun", 42, "human", 420, "acquire", 42, 4200, 5, 5)));
    MockHttpServletRequestBuilder green_builder = post("/teams").contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(mapper.writeValueAsString(new Team(green_team, "fun", 42, "human", 420, "acquire", 42, 4200, 5, 5)));
    mvc.perform(gold_builder).andReturn();
    mvc.perform(green_builder).andReturn();
  }

  /**
   * performs a GET request on /teams
   */
  @Test
  public void getAllTeams() throws Exception {
    MockHttpServletRequestBuilder builder = get("/teams");
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    ResultActions res = mvc.perform(builder);
    res.andExpect(jsonPath("$._embedded.teams[0].name", is("Gold")));
    res.andExpect(jsonPath("$._embedded.teams[1].name", is("Green")));
  }

  /**
  * performs a GET request on /teams
  * with a valid name
  */
  @Test
  public void getTeamByName() throws Exception {
    MockHttpServletRequestBuilder builder = get("/teams/search/findByName?name=Gold");
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    ResultActions res = mvc.perform(builder);
    res.andExpect(jsonPath("$._embedded.teams[0].name", is("Gold")));
  }

  /**
  * performs a GET request on /teams
  * with an invalid name
  */
  @Test
  public void getInvalidTeamByName() throws Exception {
    MockHttpServletRequestBuilder builder = get("/teams/search/findByName?name=thishouldnotexist");
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    MvcResult res = mvc.perform(builder).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
    JsonNode team = root.get("_embedded").get("teams").get(0);
    assertNull(team);
  }

  /**
  * performs a GET request on /teams
  * with an empty name
  */
  @Test
  public void getInvalidTeamByEmptyName() throws Exception {
    MockHttpServletRequestBuilder builder = get("/teams/search/findByName?name=");
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    MvcResult res = mvc.perform(builder).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
    JsonNode team = root.get("_embedded").get("teams").get(0);
    assertNull(team);
  }

  /**
  * performs a GET request on /teams
  * with a null name
  */
  @Test
  public void getInvalidTeamByNullName() throws Exception {
    MockHttpServletRequestBuilder builder = get("/teams/search/findByName?name=" + null);
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    MvcResult res = mvc.perform(builder).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(res.getResponse().getContentAsByteArray());
    JsonNode team = root.get("_embedded").get("teams").get(0);
    assertNull(team);
  }

}
