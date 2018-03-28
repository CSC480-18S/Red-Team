package com.csc480red.dictionary;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Value;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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

	@Before
	public void setup() throws Exception {
		mvc = webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * performs a validation request for a valid,
	 * bad, special, and invalid word
	 */
	@Test
	public void validateOneWordEach() throws Exception {
		mvc.perform(get("/dictionary/validate?words=hello,fuck,geese,xadf").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4))).andExpect(jsonPath("$[0].word", is("hello")))
				.andExpect(jsonPath("$[0].valid", is(true))).andExpect(jsonPath("$[0].bad", is(false)))
				.andExpect(jsonPath("$[0].special", is(false))).andExpect(jsonPath("$[1].word", is("fuck")))
				.andExpect(jsonPath("$[1].valid", is(true))).andExpect(jsonPath("$[1].bad", is(true)))
				.andExpect(jsonPath("$[1].special", is(false))).andExpect(jsonPath("$[2].word", is("geese")))
				.andExpect(jsonPath("$[2].valid", is(true))).andExpect(jsonPath("$[2].bad", is(false)))
				.andExpect(jsonPath("$[2].special", is(true))).andExpect(jsonPath("$[3].word", is("xadf")))
				.andExpect(jsonPath("$[3].valid", is(false))).andExpect(jsonPath("$[3].bad", is(false)))
				.andExpect(jsonPath("$[3].special", is(false)));
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
	 * performs a validation request on 42 words and expects
	 * all 42 to be valid words
	 */
	@Test
	public void validateValidFourtyTwoWords() throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(validWordsFile.getInputStream()));
		int times = 42;
		String words = "";
		for (int i = 0; i < times; i++)
			if (i < times - 1)
				words += buffer.readLine() + ",";
			else
				words += buffer.readLine();
		MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + words);
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		ResultActions res = mvc.perform(builder);
		res.andExpect(jsonPath("$", hasSize(42)));
		for (int i = 0; i < times; i++) {
			res.andExpect(jsonPath(String.format("$[%d].valid", i), is(true)));
		}
	}

	/**
	* performs a validation request on 42 words and expects
	* all 42 to be special words
	*/
	@Test
	public void validateSpecialFourtyTwoWords() throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(specialWordsFile.getInputStream()));
		int times = 42;
		String words = "";
		for (int i = 0; i < times; i++)
			if (i < times - 1)
				words += buffer.readLine() + ",";
			else
				words += buffer.readLine();
		MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + words);
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		ResultActions res = mvc.perform(builder);
		res.andExpect(jsonPath("$", hasSize(42)));
		for (int i = 0; i < times; i++) {
			res.andExpect(jsonPath(String.format("$[%d].special", i), is(true)));
		}
	}

	/**
	* performs a validation request on 42 words and expects
	* all 42 to be bad words
	*/
	@Test
	public void validateBadFourtyTwoWords() throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(badWordsFile.getInputStream()));
		int times = 42;
		String words = "";
		for (int i = 0; i < times; i++)
			if (i < times - 1)
				words += buffer.readLine() + ",";
			else
				words += buffer.readLine();
		MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + words);
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		ResultActions res = mvc.perform(builder);
		res.andExpect(jsonPath("$", hasSize(42)));
		for (int i = 0; i < times; i++) {
			res.andExpect(jsonPath(String.format("$[%d].special", i), is(true)));
		}
	}

	/**
	 * performs a validation request on 42 words and expects
	 * all 42 to be invalid words
	 */
	@Test
	public void validateInvalidWords() throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(invalidWordsFile.getInputStream()));
		int times = 42;
		String words = "";
		for (int i = 0; i < times; i++)
			if (i < times - 1)
				words += buffer.readLine() + ",";
			else
				words += buffer.readLine();
		MockHttpServletRequestBuilder builder = get("/dictionary/validate?words=" + words);
		builder.contentType(MediaType.APPLICATION_JSON_UTF8);
		ResultActions res = mvc.perform(builder);
		res.andExpect(jsonPath("$", hasSize(42)));
		for (int i = 0; i < times; i++) {
			res.andExpect(jsonPath(String.format("$[%d].valid", i), is(false)));
		}
	}

}
