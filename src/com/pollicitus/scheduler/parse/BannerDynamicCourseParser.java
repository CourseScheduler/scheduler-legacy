/**
  * @(#)BannerDynamicCourseParser.java
  *
  * Parse the Banner Dynamic Course Search Page to retrieve the course list.
  * This parser has been written to be as generic as possible, with the understanding
  * that it may not be able to satisfy all versions of BannerWeb or support other
  * course search options.
  *
  * @author Course Scheduler Team
  * 
  * @license GNU General Public License version 3 (GPLv3)
  *
  * This file is part of Course Scheduler, an open source, cross platform
  * course scheduling tool, configurable for most universities.
  *
  * Copyright (C) 2010-2012 Mike Reinhold
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  * 
  */
package com.pollicitus.scheduler.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

/**
 * Parse the Banner Dynamic Course Search Page to retrieve the course list.
 * This parser has been written to be as generic as possible, with the understanding
 * that it may not be able to satisfy all versions of BannerWeb or support other
 * course search options.
 *
 * Known to work with the following releases of Banner:
 * 		8.5.1
 *
 * @author Course Scheduler Team
 *
 */
public class BannerDynamicCourseParser {
	
	/**
	 * The tags that indicate that course data capture should commence. The list of tags
	 * in this variable should correspond to all of the tags that make up a single course
	 * in the order in which they occur in the source HTML (additional courses represent 
	 * additional matches against this pattern). Also, each tag listed in this array is
	 * considered a requirement - there is no support for optional data capture groups.
	 * 
	 * A tag will only be considered for course content capture if the tag characteristics
	 * also match the corresponding (based on index) position in {@link #captureContentStartCharacteristic}.
	 * If a tag is found that does not have the matching characteristic, it will be ignored for
	 * course content capture.
	 * 
	 * When a tag with the matching characteristic is found, the matched tag and all subordinate 
	 * tags will be placed into the output structure. Any tags outside of the closing tag which
	 * corresponds to the matched tag will not be captured. 
	 * 
	 * Semantics of this function (at this point in time) require that the tags listed herein 
	 * cannot be the closing tag of a tag pair (aka the /TR of a TR tag), a closed tag that is
	 * also the "starting" tag (eg. IMG / ), or an HTML comment tag.
	 * 
	 */
	private String captureContentStartTag[];
	
	/**
	 * The tag characteristics that are used to determine if a captured tag ({@link #captureContentStartTag}
	 * has the appropriate characteristics to be considered as the start of course content. This can be 
	 * any valid string composed of HTML attributes (must be properly quoted).
	 */
	private String captureContentStartCharacteristic[];
		
	/**
	 * A dynamic set of tags which, for the purposes of the parser (not necessarily a given 
	 * HTML standard), do not require a closed tag and thus are not pushed onto the tag stack 
	 * (which tracks the parsers current position relative to the start course capture tag.
	 * 
	 */
	private Set<String> closelessTags = new HashSet<String>();
	
	/**
	 * This instance initialization method initializes the set of closeless tags ({@link #closelessTags})
	 * based on the expected HTML.
	 */
	{
		closelessTags.add("BR");
		closelessTags.add("IMG");
	}
	
	/**
	 * Index into {@link #captureContentStartTag} to keep track of which capture tag we are looking for next
	 */
	private int sequence = 0;
	
	/**
	 * Stack containing the HTML tags, rooted at the current capture tag. Used to determine when we
	 * have finished with the current capture tag.
	 */
	Stack<String> tagStack;

	/**
	 * Collection of captured course content, in order of capture
	 */
	Collection<List<String>> courseTagQueues;
	
	/**
	 * Queue containing the captured content of the current course 
	 */
	List<String> currentCourse;
	
	/**
	 * Initialize the Parser with the content start tags and content start characteristics
	 *
	 * @param contentStartTags the tags which trigger a context capture
	 * @param contentStartCharacteristics tag characteristics which trigger a context capture
	 */
	public void initialize(String[] contentStartTags, String[] contentStartCharacteristics){
		this.captureContentStartTag = contentStartTags;
		this.captureContentStartCharacteristic = contentStartCharacteristics;
	}
	
	/**
	 * Parse the input stream as HTML in the expected form (in this case, from the Banner Dynamic
	 * Course Search).
	 * 
	 * This method expects that the {@link #initialize(String[], String[]) method has first been called with
	 * the appropriate content start tags and the content start characteristics.
	 *
	 * @param in the input stream from which to parse the course list
	 * @throws IOException if data cannot be read from the input stream
	 */
	public Collection<List<String>> parse(InputStream in) throws IOException{
		//make sure the instance variable are ready before parsing
		sequence = 0;
		tagStack = new Stack<String>();
		courseTagQueues =  new Vector<List<String>>();
		currentCourse = new ArrayList<String>();
		
		//text scanner to find the end of the next tag
		Scanner tagEndScanner = new Scanner(in);
		tagEndScanner.useDelimiter(">");
		
		//as long as there are tags to process, keep processing
		while(tagEndScanner.hasNext()){
			//text found prior to the tag
			String contentText = new String();
			
			//the text of the next tag - including leading '<'
			String tagText = tagEndScanner.next();
			
			//the tag may span lines in the source (aka it may contain newlines)
			tagText = tagText.replace("\n"," ").trim();
			
			//text scanner to separate text prior to the tag and the tag itself
			Scanner tagStartScanner = new Scanner(tagText);
			tagStartScanner.useDelimiter("<");
			
			//check if there is text before the tag
			if(!tagText.startsWith("<")){
				//there may not be a tag here at all - could be an empty string!!
				if(tagStartScanner.hasNext()){
					contentText = tagStartScanner.next().trim();
				}
			}
			
			//get a tag still in the text - could be an empty string!!
			if(tagStartScanner.hasNext()){
				tagText = tagStartScanner.next().trim();
			}

			//process any non-comment tag
			tagText.toUpperCase();
			if(!tagText.startsWith("!")){
				processTag(contentText, tagText);
			}
			
			//cleanup the scanner
			tagStartScanner.close();
		}
		
		//cleanup the scanner and the input stream
		tagEndScanner.close();
		in.close();
		
		return courseTagQueues;
	}
	
	
	/**
	 * Process the current HTML tag. Perform any necessary data capture
	 *
	 * @param contentText free text preceding the tag
	 * @param tagText the text of the current tag
	 */
	private void processTag(String contentText, String tagText){
		Scanner tagProperScanner = new Scanner(tagText);
		String tagProper = new String();
		
		//retrieve the pure HTML element from the tag text
		if(tagProperScanner.hasNext()){
			tagProper = tagProperScanner.next();
		}
		
		//if we are inside a capture context, capture the tag
		if(!tagStack.isEmpty()){
			processCapturedTag(contentText, tagText, tagProper);
			
		//if not in a capture context, check for the start of a context
		}else{
			processCaptureCheckTag(contentText, tagText, tagProper);
		}
		
		//cleanup
		tagProperScanner.close();
	}
	
	/**
	 * Process a tag inside a capture context and capture all tag and text content 
	 *
	 * @param contentText free text preceding the tag
	 * @param tagText the text of the current tag
	 * @param tagProper the pure HTML element without any attributes
	 */
	private void processCapturedTag(String contentText, String tagText, String tagProper){
		boolean endTag = tagText.startsWith("/");
		boolean startTag = !endTag;
		
		//indicates that this tag should not be pushed onto the context stack
		boolean noEndTag = tagText.endsWith("/") || closelessTags.contains(tagProper);
		
		//capture the content and the tag
		currentCourse.add(contentText);
		currentCourse.add(tagText);
		
		//add start tags (pure start tags, not including self closing tags)
		if(startTag && !noEndTag){
			tagStack.push(tagText);
			
		//remove end tags (pure end tags, not including self closing tags)
		}else if(endTag && !noEndTag){
			tagStack.pop();
			
			//if the stack is now empty, we've reached the end of current capture context
			if(tagStack.isEmpty()){
				//determine if the capture set for the course has been fully satisfied
				if(sequence == captureContentStartTag.length-1){
					courseTagQueues.add(currentCourse);
					currentCourse = new ArrayList<String>();
					sequence = 0;
				}else{
					sequence++;
				}
			}
		}
	}
	
	/**
	 * Process a tag outside of a capture context and begin a capture context if appropriate
	 *
	 * @param contentText free text preceding the tag
	 * @param tagText the text of the current tag
	 * @param tagProper the pure HTML element without any attributes
	 */
	private void processCaptureCheckTag(String contentText, String tagText, String tagProper){
		//check for the currently expected capture tag
		if(tagProper.compareTo(captureContentStartTag[sequence]) == 0){
			//only initiate a capture if the tag characteristics match
			if(tagText.contains(captureContentStartCharacteristic[sequence])){
				tagStack.push(tagText);
				currentCourse.add(contentText);
				currentCourse.add(tagText);
			}
		}
	}
}
