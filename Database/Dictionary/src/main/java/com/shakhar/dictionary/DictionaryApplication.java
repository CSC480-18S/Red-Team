package com.shakhar.dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class DictionaryApplication {
	
	@Value("classpath:words.txt")
	private Resource wordsFile;

	public static void main(String[] args) {
		SpringApplication.run(DictionaryApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateDatabase(WordRepository repository) {
		return (args) -> {
			try(Stream<String> stream = Files.lines(Paths.get(wordsFile.getURI()))) {
				stream.forEach((line) -> repository.save(new Word(line)));
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		};
	}
}
