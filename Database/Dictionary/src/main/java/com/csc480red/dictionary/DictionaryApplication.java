/*
 * MIT License
 * 
 * Copyright (c) 2018 CSC 480-18S
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.csc480red.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/**
 * Launches the Spring Boot Application and populates the dictionary database with words.
 * 
 * @author Shakhar Dasgupta
 *
 */
@SpringBootApplication
public class DictionaryApplication {
	
	@Value("classpath:validwords.txt")
	private Resource validWordsFile;
	
	@Value("classpath:badwords.txt")
	private Resource badWordsFile;
	
	@Value("classpath:specialwords.txt")
	private Resource specialWordsFile;

	/**
	 * Launches the Spring Boot Application.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DictionaryApplication.class, args);
	}
	
	/**
	 * Populates a {@link WordRepository} with words.
	 * 
	 * @param repository the WordRepository to populate
	 * @return bean with callback to populate the repository
	 */
	@Bean
	public CommandLineRunner populate(ValidWordRepository validWordsRepository, BadWordRepository badWordsRepository, SpecialWordRepository specialWordsRepository) {
		return (args) -> {
			try(Stream<String> validStream = new BufferedReader(new InputStreamReader(validWordsFile.getInputStream())).lines();
					Stream<String> badStream = new BufferedReader(new InputStreamReader(badWordsFile.getInputStream())).lines();
					Stream<String> specialStream = new BufferedReader(new InputStreamReader(specialWordsFile.getInputStream())).lines()) {
				validStream.forEach((line) -> validWordsRepository.save(new ValidWord(line)));
				badStream.forEach((line) -> badWordsRepository.save(new BadWord(line)));
				specialStream.forEach((line) -> specialWordsRepository.save(new SpecialWord(line)));
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		};
	}
}
