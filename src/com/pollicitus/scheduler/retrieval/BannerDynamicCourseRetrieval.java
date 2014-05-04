/**
  * @(#)BannerDynamicCourseRetrieval.java
  *
  * Organize and coordinate the course retrieval from the Banner Dynamic Course 
  * search pages. This class performs the connection to the Banner server and the
  * query for the course list. The resulting input stream is handed to a BannerDynamicCourseParser
  * for parsing the relevant course data out of the resulting web page. This class
  * also follows hyperlinks that match the specified patterns in order to retrieve 
  * course data from subsequent pages.
  * 
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
package com.pollicitus.scheduler.retrieval;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;

import com.pollicitus.scheduler.parse.BannerDynamicCourseParser;

import Scheduler.ClientHttpRequest;

/**
 * Organize and coordinate the course retrieval from the Banner Dynamic Course 
 * search pages. This class performs the connection to the Banner server and the
 * query for the course list. The resulting input stream is handed to a BannerDynamicCourseParser
 * for parsing the relevant course data out of the resulting web page. This class
 * also follows hyperlinks that match the specified patterns in order to retrieve 
 * course data from subsequent pages.
 * 
 * Known to work with the following releases of Banner:
 * 		8.5.1
 *
 * @author Course Scheduler Team
 *
 */
public class BannerDynamicCourseRetrieval extends Observable {	
	
	/**
	 * The map of strings that should be replaced in a URL retrieved
	 * from an href attribute. The link text in the href attribute
	 * typically have certain strings or characters replaced with
	 * an HTML code corresponding to that text. In order to actually
	 * follow the link, these strings must be replaced prior to 
	 * connection.
	 */
	protected Map<String, String> urlReplacementStrings;
	
	{
		//the map of string codes to strings
		urlReplacementStrings = new HashMap<>();
		
		urlReplacementStrings.put("&amp;", "&");
	}
	
	/**
	 * The list of link characteristics used to determine if a link should
	 * be followed by the course retrieval engine. Each entry in this list
	 * represents an individual link and must be able to provide independent
	 * confirmation that a link should be followed.
	 */
	protected String[] followLinkCharacteristic = new String[]{
			"bwckschd.p_disp_detail_sched",
			"bwckctlg.p_display_courses"
	};
	
	/**
	 * The set of parse options that will be used when following a link. 
	 * Each entry in this list is related to the link characteristic ({@link #followLinkCharacteristic) 
	 * based on its position in this list.
	 */
	protected String[][][] followLinkParseOptions = new String[][][] {
			//parse options corresponding to the bwckschd.p_disp_detail_sched link 
			{
				{"TH","TD"},
				{"ddlabel", "dddefault"}
			},
			//parse options corresponding to the bwckctlg.p_display_courses link
			{
				{"TD", "TD"},
				{"nttitle", "ntdefault"}
			}
	};
	
	/**
	 * The URL base for the current retrieval process. This should be comprised of the protocol,
	 * the hostname, and port. This is used for following relative links and is built based on
	 * the primary url of the retrieval.
	 */
	protected String requestBase;
	
	/**
	 * Perform the network retrieval and primary parsing of the resulting web pages. 
	 * Links will be followed as appropriate and the results of parsing that page
	 * will be added to the current course. 
	 *
	 * @param url The POST URL for the Banner Dynamic Course Search page
	 * @param term the term code representing the course that should be retrieved.
	 * @return a collection of course data sets, where each list in the collection is a course
	 * @throws MalformedURLException in case we are unable to process the URLs
	 * @throws IOException in case there is an issue retrieving from the URL
	 */
	public Collection<List<String>> retrieveCourses(String url, String term) throws MalformedURLException, IOException{
		//capture the URL and build the request base for following links
		URL target = new URL(url);
		requestBase = target.getProtocol() + "://" + target.getHost() + 
				(target.getPort() != -1 ? target.getPort() : "");
		
		//publish status
		setChanged();
		notifyObservers("Connecting to " + target);
		
		//perform a post using the specified parameters
		InputStream in = ClientHttpRequest.post(	
            target, 								
            new Object[] {							
    			"begin_ap","a",
				"begin_hh","0",
				"begin_mi","0",
				"end_ap","a",
				"end_hh","0",
				"end_mi","0",
				"sel_attr","dummy",
				"sel_camp","dummy",
				"sel_crse","",
				"sel_day","dummy",
				"sel_from_cred","",
				"sel_insm","dummy",
				"sel_instr","dummy",
				"sel_instr","%",
				"sel_levl","dummy",
				"sel_ptrm","dummy",
				"sel_schd","dummy",
				"sel_sess","dummy",
				"sel_subj","dummy",
				"sel_subj","%",
				"sel_title","",
				"sel_to_cred","",
				"term_in",term
        });
		
		//publish status
		setChanged();
		notifyObservers("Connected to " + target);
		
		//Build the dynamic course parser, initialized for parsing the complete course list
		BannerDynamicCourseParser bdcp = new BannerDynamicCourseParser();
		bdcp.initialize(
				new String[]{"TH", "TD"}, 
				new String[]{"ddtitle", "dddefault"});
		
		//publish status
		setChanged();
		notifyObservers("Parsing Course Data");
		
		//retrieve the parsed data set
		Collection<List<String>> courseQueues = bdcp.parse(in);
		
		//process the course data
		int courseNum = 1;
		for(List<String> course: courseQueues){
			try{
				//publish status
				setChanged();
				notifyObservers("Processing Course Data: " + courseNum++);
				
				processCourse(course);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return courseQueues;
	}
	
	/**
	 * Process the current course data to perform any subsequent data retrievals
	 * and additional course data parsing.
	 *
	 * @param course the course data set to use for processing
	 */
	protected void processCourse(List<String> course){
		//the list that will contain all additional course data
		List<String> courseExtensions = new ArrayList<String>();
				
		//process each course data entry
		for(String entry: course){
			Scanner entryScanner = new Scanner(entry);
			String tagProper = new String();
			
			if(entryScanner.hasNext()){
				tagProper = entryScanner.next();
				String remnant = new String();
				
				//check if there is additional text after the tag
				if(entryScanner.hasNextLine()){
					remnant = entryScanner.nextLine();
				}
				
				//only specific tags need any special processing
				switch(tagProper){
					case "A":{
						processCourseLink(courseExtensions, remnant);
					}
				}
			}
			//cleanup the scanner
			entryScanner.close();
		}
		
		//add the parsed data
		course.addAll(courseExtensions);
	}
	
	/**
	 * Process the course data link into the foundCourseData set. The foundCourseData
	 * list may already contain data. This method, and those called by this method, are
	 * defined to only add data to this list, never remove data from it. 
	 *
	 * @param foundCourseData the resulting data from the link
	 * @param linkString the tag text that contains the href link
	 */
	protected void processCourseLink(List<String> foundCourseData, String linkString){
		int reference = 0;
		for(String referenceLink: followLinkCharacteristic){
			if(linkString.contains(referenceLink)){
				retrieveLinkedCourseData(foundCourseData, linkString, followLinkParseOptions[reference]);
				break;
			}
			reference++;
		}
	}
	
	/**
	 * Retrieve additional course data for the course via the specified link string using the
	 * specified parsing options. The resulting data will be placed into the foundCourseData
	 * list.
	 *
	 * @param foundCourseData the resulting data from the link
	 * @param linkString the tag text that contains the href link
	 * @param linkParseOptions the BannerDynamicCourseParser options for processing the link
	 */
	protected void retrieveLinkedCourseData(List<String> foundCourseData, String linkString, String[][] linkParseOptions){
		//retrieve the link from the tag text and build full URL
		String url = prepareLinkFromHTML(linkString);
		
		//build parser and initialize with the appropriate parse options
		BannerDynamicCourseParser bdcp = new BannerDynamicCourseParser();
		bdcp.initialize(linkParseOptions[0], linkParseOptions[1]);
		
		InputStream in;
		try {
			//open the link directly (simple GET)
			in = new URL(url).openStream();
			
			//parse the course set from the page
			Collection<List<String>> courseQueues = bdcp.parse(in);
			
			//add the course set data to the current course dataset
			for(List<String> set: courseQueues){
				foundCourseData.addAll(set);
			}			
		} catch (IOException e) {
			// TODO CATCH STUB
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parse the tag text to find the link href. Build the full link by combining
	 * with the resource base, if applicable, and replacing HTML codes.
	 *
	 * @param linkSource the tag text that contains the href link
	 * @return the updated and fully functional link - unless the linkSource did not contain
	 * 		a valid href, in which case the result will be an empty string.
	 */
	protected String prepareLinkFromHTML(String linkSource){
		Scanner hrefScanner = new Scanner(linkSource);
		hrefScanner.useDelimiter("HREF=\"");
		
		//check for the href portion of the link
		if(hrefScanner.hasNext()){
			
			//make sure that we discard any text preceding the href
			if(!linkSource.startsWith("HREF")){
				hrefScanner.next();
			}
			
			//get the href text, including the closing quote
			String temp = hrefScanner.next();
			
			//look for the end of the href
			hrefScanner.close();
			hrefScanner = new Scanner(temp);
			hrefScanner.useDelimiter("\"");
			if(hrefScanner.hasNext()){
				String link = hrefScanner.next();
				String url;
				
				//perform necessary html code replacements
				for(String code: urlReplacementStrings.keySet()){
					link = link.replace(code, urlReplacementStrings.get(code));
				}
				
				//prepend the protocol, host, and port if necessary
				if(!link.contains("://")){
					url = requestBase + link;
				}else{
					url = link;
				}
				
				hrefScanner.close();
				return url;
			}
		}
		
		//should not get here unless the href was missing from the tag text - malformed
		hrefScanner.close();
		return "";
	}
}
