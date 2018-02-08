package com.shakhar.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dictionary")
public class DictionaryRestController {
	
	@Autowired
	WordRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, value = "validate")
	public boolean isValid(@Param("word") String word) {
		return repository.existsByWord(word);
	}
}
