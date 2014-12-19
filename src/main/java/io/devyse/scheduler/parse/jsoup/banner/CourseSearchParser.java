/**
 * @(#) CourseSearchParser.java
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
import io.devyse.scheduler.retrieval.CoursePersister;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Process the course search results page into separate sub-documents for each course
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class CourseSearchParser extends AbstractParser<Void> {
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(CourseSearchParser.class);
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The course data persister which will store the course into the data model
	 */
	private CoursePersister persister;
	
	/**
	 * @param document
	 */
	public CourseSearchParser(Document document, CoursePersister persister){
		super(document);
		
		this.persister = persister;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.parse.jsoup.AbstractParser#parse(org.jsoup.nodes.Document)
	 */
	protected void parse(Document document){
		Set<CourseParser> courseParsers = new HashSet<>();
		logger.debug("\n=== Section Listing ==============================");
		Elements sectionRows = document.select("table.datadisplaytable > tbody > tr:has(th.ddtitle, td.dddefault span)");
		
		logger.debug("Found {} Sections ({} Rows)", sectionRows.size()/2, sectionRows.size());
		
		for(Element row = sectionRows.first(); row != null; row = row.nextElementSibling()){
			// Section info is 2 table rows - 1 "header" table row and 1 "detail" table row, each with sub info			
			Element section = row.clone();
			row = row.nextElementSibling();
			Element sectionDetail = row.clone();
			
			Document sectionDocument = new Document(document.baseUri());
			sectionDocument.appendChild(section);
			sectionDocument.appendChild(sectionDetail);
			
			CourseParser courseParser = new CourseParser(sectionDocument);
			courseParsers.add(courseParser);
			courseParser.fork();
		}
		
		//TODO evaluate moving this into the CourseParser instead of here
		//may improve performance a bit since we don't have to wait for threads to join,
		//but may limit our ability to track progress
		int section = 0;
		for(CourseParser parser : courseParsers){
			Map<String, String> result = parser.join();

			logger.debug("\n---- Section {}", ++section);
			persister.persist(result);
		}
	}
}
