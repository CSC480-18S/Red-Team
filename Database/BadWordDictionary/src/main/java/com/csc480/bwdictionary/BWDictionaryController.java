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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Uses the repository to check if the word is a valid word
 * 
 * @author Samantha Wheeler
 */

@Controller
@RequestMapping("bwdictionary")
public class BWDictionaryController {

	/*
	 * Interjects WordRepository bean, allows for use
	 */
	@Autowired
	private WordRepository wordRepository;
	
	/*
	 * @param String  The word being checked
	 * @return boolean  True if the word is in the "bad" dictionary, false if it isn't
	 * Checks whether the word is a "valid" word in the "bad" dictionary
	 */
	@RequestMapping(method = RequestMethod.GET, value = "validate")
	public boolean isValid(@Param("word") String word) {
		return wordRepository.existsByWordIgnoreCase(word);
	}

}

