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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Defines "Word" entity and attributes, allows retrieval of attributes.
 * 
 * @author Samantha Wheeler
 */

@Entity
public class Word {
	
	/*
	 * "ID" of the Word, a unique identifier
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	/*
	 * The word itself
	 */
	private String word;
	
	/*
	 * @param String The String that will be the "word"
	 * @return Word Returns the Word object created
	 */
	public Word (String w) {
		word = w;
	}
	
	/*
	 * @return Returns the ID of this Word
	 */
	public Long getID() {
		return id;
	}
	
	/*
	 * @return Returns the String of this Word
	 */
	public String getWord() {
		return word;
	}

}

