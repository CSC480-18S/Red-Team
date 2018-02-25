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

package com.csc480red.bwdictionary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.csc480.bwdictionary.BWDictionaryController;
import com.csc480.bwdictionary.BwDictionaryApplication;
import com.csc480.bwdictionary.WordRepository;

/*
 * Test class; runs JUnit tests on existsByWordIgnoreCase
 * 
 * @author Samantha Wheeler
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes={BwDictionaryApplication.class})
public class BwDictionaryApplicationTests {

	/*
	 * Interjects WordRepository bean, allows for use
	 */
	@Autowired
	WordRepository repository;
	
	/*
	 * First test case: "ass" is a valid word in the "bad" dictionary
	 * existsByWordIgnoreCase should return true
	 */
	@Test
	public void existsTest1() throws Exception {
		boolean result = repository.existsByWordIgnoreCase("ass");
		org.junit.Assert.assertEquals(true, result);
	}
	
	/*
	 * Second test case: "test" is not a valid word in the "bad" dictionary
	 * existsByWordIgnoreCase should return false
	 */
	@Test
	public void existsTest2() throws Exception {
		boolean result = repository.existsByWordIgnoreCase("test");
		org.junit.Assert.assertEquals(false, result);
	}
	
	/*
	 * Third test case: "christ" is a valid word in the "bad" dictionary
	 * This test checks for the existance in the dictionary of a word that wasn't in the 
	 * original list being used as the dictionary of inappropriate words, but is in the 
	 * current list of bad words.
	 * existsByWordIgnoreCase should return true
	 */
	@Test
	public void existsTest3() throws Exception {
		boolean result = repository.existsByWordIgnoreCase("christ");
		org.junit.Assert.assertEquals(true, result);
	}

}

