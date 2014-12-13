/**
 * @(#) LegacyDataModelPersister.java
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
package Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.devyse.scheduler.retrieval.CoursePersister;

/**
 * A Course data persister implementation that processes course data and persists it
 * into the legacy data model
 * 
 * @author Mike Reinhold
 *
 */
public class LegacyDataModelPersister implements CoursePersister {

	/**
	 * Static logger
	 */
	private static Logger logger = LoggerFactory.getLogger(LegacyDataModelPersister.class);
	
	/**
	 * Legacy database class
	 */
	private Database database;
	
	/**
	 * Create a new instance of the legacy datamodel persister which will store persist the 
	 * course data into the specified database
	 * 
	 * @param database the legacy database which is the target for the persisting activity
	 */
	public LegacyDataModelPersister(Database database) {
		super();
		
		this.database = database;
	}

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.CoursePersister#persist(java.util.Map)
	 */
	@Override
	public void persist(Map<String, String> data) {
		//only go through the effort of outputting if the necessary logging level is enabled
		if(logger.isDebugEnabled()){
			List<String> keys = new ArrayList<>();
			keys.addAll(data.keySet());
			Collections.sort(keys);
			for(String key: keys){
				logger.debug("{}: {}", key, data.get(key));
			}
		}
		
		Section section = new Section();

		processHeaderFields(data, section);
		processSectionType(data, section);		

		String open = data.get("seating.Remaining");
		String credits = data.get("credit.Credit");
		section.setCredit(credits);
		section.setSeats(Integer.valueOf(open));
				
		//TODO get notes from a field somewhere
		String notes = "";
		
		processMeetingInformation(data, section, notes);
		
		//persist the parsed section
		database.addSection(section);
	}
	
	/**
	 * Process the section header and extract the relevant fields 
	 * 
	 * @param data map of the section data extracted from the source
	 * @param section the section instance into which the data should be persisted 
	 */
	private void processHeaderFields(Map<String, String> data, Section section){
		String header = data.get("header");
		Pattern headerPattern = Pattern.compile("(.*) - (\\d+) - ([\\w\\s]+) - ([\\S]+)");
		Matcher headerMatcher = headerPattern.matcher(header);
		headerMatcher.find();
		
		String title = headerMatcher.group(1); 
		String requestNum = headerMatcher.group(2);
		String courseID = headerMatcher.group(3);
		String sectionID = headerMatcher.group(4);

		section.setCRN(Integer.valueOf(requestNum));
		section.setCourseID(courseID);
		section.setSection(sectionID);
		section.setTitle(title);
	}
	
	/**
	 * Process the course data to extract the section type
	 * 
	 * @param data map of the section data extracted from the source
	 * @param section the section instance into which the data should be persisted
	 */
	private void processSectionType(Map<String, String> data, Section section){
		String levels = data.get("levels");
		String type = data.get("type");
		
		Pattern levelsPattern = Pattern.compile("((Non Degree)?(-)?(\\w+))");
		Matcher levelsMatcher = levelsPattern.matcher(levels);
		
		List<String> levelsList = new ArrayList<>();
		
		while(levelsMatcher.find()){
			boolean degreeProgram = levelsMatcher.group(2) == null;
			if(degreeProgram) levelsList.add(levelsMatcher.group(4));
		}
		
		// Legacy data model has limited ability to capture the different course types
		CourseType sectionType;
		if(levelsList.contains("Graduate")){
			if(type.contains("Internet")){
				sectionType = CourseType.campusAndDistance;
			}else{
				sectionType = CourseType.campusGrad;
			}	
		}else{
			sectionType = CourseType.undergrad;
		}
		
		section.setType(sectionType);
	}
	
	/**
	 * Process the meeting times from the course data and update the section meeting information
	 * 
	 * @param data map of the section data extracted from the source
	 * @param section the section instance into which the data should be persisted
	 * @param notes the section notes field containing additional details
	 */
	private void processMeetingInformation(Map<String, String> data, Section section, String notes){
		//check for meetings
		for(int meeting=0; data.get("meeting."+meeting) != null; meeting++){

			String period = data.get("meeting."+meeting+".Time");
			boolean[] days = buildDayArray(data.get("meeting."+meeting+".Days"));
			String location = data.get("meeting."+meeting+".Where");
			
			String instructorString = data.get("meeting."+meeting+".Instructors");
			List<String> instructorList = extractInstructorList(instructorString);
			
			if(meeting == 0){
				section.setPeriod(period);
				section.setDays(days);
				section.setLocation(location);
								
				section.setInstructorList(instructorList);
			}else if(meeting == 1){
				section.setSecondary(true);
				
				section.setSecPeriod(period);
				section.setSecDays(days);
				section.setSecLocation(location);
			}else if(meeting == 2){		// don't need to add this more than once
				notes += "\nThis course has more than 2 meeting times. /nPlease confirm that the additional meeting times do not impact your schedule.";
			}
		}

		section.setNotes(notes);
	}

	/**
	 * Extract the instructor list from the provided instructor string
	 * 
	 * @param instructorString the instructor string containing the instructor names
	 * @return the list of instructor names
	 */
	private List<String> extractInstructorList(String instructorString){
		List<String> instructorList = new ArrayList<>();
		Pattern instPattern = Pattern.compile("((?:[^\\(\\),]){2,}+)");
		Matcher instMatcher = instPattern.matcher(instructorString);
		
		while(instMatcher.find()){
			String instructor = instMatcher.group(1);
			if(instructor.compareTo("") != 0){
				instructorList.add(instructor.trim());
			}
		}
		return instructorList;
	}
	
	/**
	 * Build a day flag array based on the specified day character string
	 * 
	 * @param dayString a string of characters representing the days on which a meeting occurs (eg. MWF)
	 * @return a day flag array indicating the days
	 */
	private boolean[] buildDayArray(String dayString){
		boolean[] days = new boolean[Day.values().length];
		
		if(dayString.compareTo("\u00A0") != 0){
			for(char dayCode: dayString.toCharArray()){
				Day day = Day.getDay(new Character(dayCode).toString());
				days[day.value()] = true;
			}
		}
		return days;
	}
	
}
