/**
 * @(#) CourseParser.java
 *
 * This file is part of the Course Scheduler, an open source, cross platform
 * course scheduling tool, configurable for most universities.
 *
 * Copyright (C) 2010-2014 Devyse.io; All rights reserved.
 *
 * @license GNU General Public License version 3 (GPLv3)
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
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package io.devyse.scheduler.parse.jsoup.banner;

import io.devyse.scheduler.parse.jsoup.AbstractParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * A CourseParser parses a sub-document extracted from the main course search results document
 * in order to find the course data.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class CourseParser extends AbstractParser<Map<String, String>>{

	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(CourseParser.class);
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new CourseParser for parsing the Course document that is extracted
	 * from the main course search results page
	 * 
	 * @param document the course document
	 * @param timeout the socket connection timeout for any created connections
	 */
	public CourseParser(Document document, int timeout){
		super(document, timeout);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.parse.jsoup.AbstractParser#parse(org.jsoup.nodes.Document)
	 */
	protected void parse(Document document) throws IOException{
		Map<String, String> values = new HashMap<>();
		Element sectionHeaderElement = document.select("tr > th.ddtitle > a[href]").first();
		
		String sectionDetailURL = sectionHeaderElement.absUrl("href");
		String sectionHeaderText = sectionHeaderElement.text();
		values.put("header", sectionHeaderText);
		values.put("url", sectionDetailURL);
		
		Element termElement = document.select("tr > td.dddefault > span:containsOwn(Associated Term)").first();
		String term = termElement.nextSibling().toString();
		values.put("term", term);
		
		Element registrationElement = document.select("tr > td.dddefault > span:containsOwn(Registration Dates)").first();
		String registration = registrationElement.nextSibling().toString();
		values.put("registration", registration);
		
		Element levelsElement = document.select("tr > td.dddefault > span:containsOwn(Levels)").first();
		String levels = levelsElement.nextSibling().toString();
		values.put("levels", levels);
		
		Element campusElement = document.select("tr > td.dddefault > br + br").first();
		String campus = campusElement.nextSibling().toString();
		values.put("campus", campus);
				
		Element scheduleTypeElement = campusElement.nextElementSibling();
		String scheduleType = scheduleTypeElement.nextSibling().toString();
		values.put("type", scheduleType);
		
		Element creditElement = scheduleTypeElement.nextElementSibling();
		String credit = creditElement.nextSibling().toString();
		values.put("credit", credit);
		
		Element catalogEntryElement = document.select("tr > td.dddefault > a[href]").first();
		String catalogEntryURL = catalogEntryElement.absUrl("href");
		values.put("catalog", catalogEntryURL);
		
		parseCourseDetail(Jsoup.connect(sectionDetailURL).timeout(this.getTimeout()).get(), values);
		parseCatalogEntry(Jsoup.connect(catalogEntryURL).timeout(this.getTimeout()).get(), values);
	
		Elements meetingHeaderElements = document.select("table.datadisplaytable:has(caption:containsOwn(Scheduled Meeting Times)) th.ddheader");
		Elements meetingRowElements = document.select("table.datadisplaytable:has(caption:containsOwn(Scheduled Meeting Times)) tr:has(td.dddefault)");
		List<String> headers = new ArrayList<>();
		
		for(Element meetingHeader : meetingHeaderElements){
			headers.add(meetingHeader.text());
		}
		
		int row = 0;
		for(Element meetingRow : meetingRowElements){
			Elements meetingValues = meetingRow.select("td");
			
			int index=0;
			values.put("meeting."+row, Boolean.TRUE.toString());
			for(Element meetingValue: meetingValues){
				values.put("meeting."+row+"."+headers.get(index), meetingValue.text());
				index++;
			}
			row++;
		}
		
		this.setRawResult(values);
	}
	
	/**
	 * Parse the Catalog Entry page for a given course to retrieve the long description of the course, the credit 
	 * hour breakdown, and the department of the course
	 * 
	 * @param document the Catalog Entry page HTML document
	 * @param values the retrieved course data set, including the newly added Catalog Entry values
	 */
	private void parseCatalogEntry(Document document, Map<String, String> values){
		//Long description is in the first text node in the table
		Element longDetailElement = document.select("table.datadisplaytable td.ntdefault").first();
		String longDetail = longDetailElement.textNodes().get(0).toString();
		values.put("description", longDetail);
		
		//Credit hours are in TextNodes following the long description
		List<TextNode> creditNodes = longDetailElement.textNodes();
		for(TextNode creditNode : creditNodes){
			String text = creditNode.text();
			try(Scanner scanner = new Scanner(text);){
				scanner.useDelimiter(" ");
				if(text.contains("TO")){
					logger.debug("Found credit range entry, will attempt to use max value in range. Range: {}", text);
					//Some catalog entries use the "X.000 TO Y.000" Credits format for the credit hours
					//in almost all cases, X is 0, so we take Y as the credit count - skip "X.000" and "TO"
					scanner.next();
					scanner.next();
				} 
				
				if(scanner.hasNextDouble()){
					double value = scanner.nextDouble();
					String component = scanner.next();
					values.put("credit."+component, Double.toString(value));
					logger.debug("Found credit hour entry: {}={}", component, value);
				}else{
					logger.debug("Expected credit hour text node, found instead: {}", text);
				}
			}
		}
		
		//Department always seems to be 3rd text node from the end of the table
		String department = longDetailElement.textNodes().get(longDetailElement.textNodes().size()-3).toString();
		values.put("department", department);
	}
	
	/**
	 * Parse the Section Detail information page to retrieve the seating availability, registration restrictions,
	 * and prerequisites information
	 * 
	 * @param document the Section Detail page HTML document
	 * @param values the retrieved course data set, including the newly added Section Detail values
	 */
	private void parseCourseDetail(Document document, Map<String, String> values){
		Elements availabilityHeaders = document.select("caption:containsOwn(Registration Availability) + tbody th.ddheader span");
		Elements availabilityValues = document.select("caption:containsOwn(Registration Availability) + tbody td.dddefault");
				
		for(int pos = 0; pos < availabilityHeaders.size(); pos++){
			String header = availabilityHeaders.get(pos).text();
			String value = availabilityValues.get(pos).text();
			values.put("seating." + header, value);
		}
		
		Element restrictionElement = document.select("span:containsOwn(Restriction)").first();
		try{
			for(Node node = restrictionElement.nextSibling(); !(node instanceof Element && ((Element)node).tag().equals(Tag.valueOf("span"))); node = node.nextSibling()){
				logger.debug("Restriction: {}", node);
				///TODO handle the restrictions list - grouping of restrictions (or restriction list elements) indicated by indentation
			}
		}catch(NullPointerException e){
			//Not all courses will have restrictions and this element is only present if restrictions exist
			logger.debug("No restriction found", e);
		}
		
		Element prerequisiteElement = document.select("span:containsOwn(Prerequisite)").first();
		try{
			for(Node node = prerequisiteElement.nextSibling(); node != null; node = node.nextSibling()){
				logger.debug("Prereq: {}", node);
				//TODO handle the prerequisite list - can be AND-OR or OR-AND formatted (keywords 'and' 'or' present to indicate w/ parentheses for grouping
			}
		} catch(Exception e){
			//Not all courses will have prerequisites and this element is only present if prerequisites exist
			logger.debug("No prequisite found", e);
		}
	}
}
