package com.csc480red.dictionary;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DictionaryApplicationTests {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() throws Exception {
		mvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void validate_with_hello_fuck_geese_xadf() throws Exception {
		mvc.perform(get("/dictionary/validate?words=hello,fuck,geese,xadf")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(4)))
		.andExpect(jsonPath("$[0].word", is("hello")))
		.andExpect(jsonPath("$[0].valid", is(true)))
		.andExpect(jsonPath("$[0].bad", is(false)))
		.andExpect(jsonPath("$[0].special", is(false)))
		.andExpect(jsonPath("$[1].word", is("fuck")))
		.andExpect(jsonPath("$[1].valid", is(true)))
		.andExpect(jsonPath("$[1].bad", is(true)))
		.andExpect(jsonPath("$[1].special", is(false)))
		.andExpect(jsonPath("$[2].word", is("geese")))
		.andExpect(jsonPath("$[2].valid", is(true)))
		.andExpect(jsonPath("$[2].bad", is(false)))
		.andExpect(jsonPath("$[2].special", is(true)))
		.andExpect(jsonPath("$[3].word", is("xadf")))
		.andExpect(jsonPath("$[3].valid", is(false)))
		.andExpect(jsonPath("$[3].bad", is(false)))
		.andExpect(jsonPath("$[3].special", is(false)));
	}
}
