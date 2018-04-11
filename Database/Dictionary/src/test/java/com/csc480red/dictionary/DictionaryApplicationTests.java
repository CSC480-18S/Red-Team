package com.csc480red.dictionary;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Value;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DictionaryApplicationTests {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Value("classpath:validwords.txt")
  private Resource validWordsFile;

  @Value("classpath:specialwords.txt")
  private Resource specialWordsFile;

  @Value("classpath:badwords.txt")
  private Resource badWordsFile;

  @Value("classpath:invalidwords.txt")
  private Resource invalidWordsFile;

  private static final int times = 2;

  @Before
  public void setup() throws Exception {
    mvc = webAppContextSetup(webApplicationContext).build();
  }

  /**
  * performs a validation request with no words
  */
  @Test
  public void validateNoWords() throws Exception {
    mvc.perform(get("/dictionary/validate?words=").contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  /**
   * performs a validation request for one valid,
   * bad, special, and invalid word
   */
  @Test
  public void validateOneWordEach() throws Exception {
    mvc.perform(get("/dictionary/validate?words=hello,fuck,lake,snarfblar").contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(4))).andExpect(jsonPath("$[0].word", is("hello")))
        .andExpect(jsonPath("$[0].valid", is(true))).andExpect(jsonPath("$[0].bad", is(false)))
        .andExpect(jsonPath("$[0].special", is(false))).andExpect(jsonPath("$[1].word", is("fuck")))
        .andExpect(jsonPath("$[1].valid", is(true))).andExpect(jsonPath("$[1].bad", is(true)))
        .andExpect(jsonPath("$[1].special", is(false))).andExpect(jsonPath("$[2].word", is("lake")))
        .andExpect(jsonPath("$[2].valid", is(true))).andExpect(jsonPath("$[2].bad", is(false)))
        .andExpect(jsonPath("$[2].special", is(true))).andExpect(jsonPath("$[3].word", is("snarfblar")))
        .andExpect(jsonPath("$[3].valid", is(false))).andExpect(jsonPath("$[3].bad", is(false)))
        .andExpect(jsonPath("$[3].special", is(false)));
  }

  @Test
  public void validateTwoWordsEach() throws Exception {
    String words = "hello,acquire,fuck,shit,lake,snow,snarfblar,xadf";
    MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + words);
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    ResultActions res = mvc.perform(builder);
    res.andExpect(jsonPath("$", hasSize(8)));
    res.andExpect(jsonPath(String.format("$[0].valid"), is(true)));
    res.andExpect(jsonPath(String.format("$[1].valid"), is(true)));
    res.andExpect(jsonPath(String.format("$[2].bad"), is(true)));
    res.andExpect(jsonPath(String.format("$[3].bad"), is(true)));
    res.andExpect(jsonPath(String.format("$[4].special"), is(true)));
    res.andExpect(jsonPath(String.format("$[5].special"), is(true)));
    res.andExpect(jsonPath(String.format("$[6].valid"), is(false)));
    res.andExpect(jsonPath(String.format("$[7].valid"), is(false)));
  }

  /**
   * performs a validate request on an Integer
   */
  @Test
  public void validateInteger() throws Exception {
    MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + 42);
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    ResultActions res = mvc.perform(builder);
    res.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].valid", is(false)));
  }

  /**
   * performs a validation request with an entry
   * containing whitespace
   */
  @Test
  public void validateWithWhiteSpace() throws Exception {
    String word = "this has whitespace";
    MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + word);
    builder.contentType(MediaType.APPLICATION_JSON_UTF8);
    ResultActions res = mvc.perform(builder);
    res.andExpect(jsonPath("$", hasSize(1)));
    res.andExpect(jsonPath("$[0].valid", is(false)));
  }

}
