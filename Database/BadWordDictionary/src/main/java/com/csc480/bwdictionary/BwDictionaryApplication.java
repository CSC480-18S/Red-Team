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

package com.csc480.bwdictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/*
 * Launches Spring application in main method, populates the "bad" dictionary with "bad" words
 * (words in the dictionary that users shouldn't be using in the game)
 * 
 * @author Samantha Wheeler
 */

@SpringBootApplication
public class BwDictionaryApplication {

	/*
	 * Grabs the text file of "bad" words and saves it as a Resource
	 */
	@Value("classpath:invalid.txt")
	private Resource words = (Resource) new ClassPathResource("classpath:invalid.txt");
	
	/*
	 * @param args Command line arguments
	 * Starts the Spring application
	 */
	public static void main(String[] args) {
		SpringApplication.run(BwDictionaryApplication.class, args);
	}
	
	/*
	 * @param repository The WordRepository
	 * @return CommandLineRunner Indicates bean should run while inside Spring application
	 * Uses the resource "words" to populate the "bad" dictionary with "bad" words
	 */
	@Bean
	public CommandLineRunner populate(WordRepository repository) {
		return (args) -> {
			try(Stream<String> stream = new BufferedReader(new InputStreamReader(((ClassPathResource) words).getInputStream())).lines()) {
				stream.forEach((line) -> repository.save(new Word(line)));
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		};
	}
}
