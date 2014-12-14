/********************************************************
 * Copyright (C) 2008 Course Scheduler Team
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of 
 * 	the GNU General Public License as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * 	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * 	if not, write to:
 * 		Free Software Foundation, Inc.
 * 		59 Temple Place, Suite 330, 
 * 		Boston, MA 02111-1307 USA
********************************************************/

/*********************************************************
 * Course Scheduler
 * File: Version.java
 * 
 * Contains enumerator:
 * 
 * 		Version:
 * 
 * 			Purpose: To store version information for 
 * 				program classes for serialization and updates
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//define member of package

import javax.swing.XTabComponent;
import javax.swing.JTextFieldFilter;


/*********************************************************
 * Enumerator: Version
 * 
 * @purpose Stores and returns version information for 
 * 		program classes for serialization and deserialization
 * 		as well as program updates.
*********************************************************/
public enum Version {
	
	
	/*********************************************************
	 * The following are the version enumerators and their
	 * 		field values
	*********************************************************/
	time 	    (10000000, Time.versionID, pad(new String("Time.java"))),
	period 	    (20000000, Period.versionID, pad(new String("Period.java"))),
	section     (30000000, Section.versionID, pad(new String("Section.java"))),
	course 	    (40000000, Course.versionID, pad(new String("Course.java"))),
	schedule    (50000000, Schedule.versionID, pad(new String("Schedule.java"))),
	database    (60000000, Database.versionID, pad(new String("Database.java"))),
	day 	    (70000000, Day.versionID, pad(new String("Day.java"))),
	compare     (80000000, Compare.versionID, pad(new String("Compare.java"))),
	parser      (90000000, Parser.versionID, pad(new String("Parser.java"))),
	main        (100000000, Main.versionID, pad(new String("Main.java"))),
	version     (110000000, Version.versionID, pad(new String("Version.java"))),
	httpclient  (120000000, ClientHttpRequest.versionID, pad(new String("ClientHttpRequest.java"))),
	term        (130000000, Term.versionID, pad(new String("Term.java"))),
	rate        (140000000, Rate.versionID, pad(new String("Rate.java"))),
	preferences (150000000, Preferences.versionID, pad(new String("Preferences.java"))),
	serial      (160000000, Serial.versionID, pad(new String("Serial.java"))),
	sectionComp (170000000, SectionComparator.versionID, pad(new String("SectionComparator.java"))),
	character   (180000000, Char.versionID, pad(new String("Char.java"))),
	prof        (190000000, Prof.versionID, pad(new String("Prof.java"))),
	mainFrame   (200000000, MainFrame.versionID, pad(new String("MainFrame.java"))),
	mainMenu    (210000000, MainMenu.versionID, pad(new String("MainMenu.java"))),
	chooseTerm  (220000000, ChooseTerm.versionID, pad(new String("ChooseTerm.java"))),
	parseThread (230000000, ParseThread.versionID, pad(new String("ParseThread.java"))),
	helpAbout   (240000000, HelpAboutFrame.versionID, pad("HelpAboutFrame.java")),
	options     (250000000, OptionsFrame.versionID, pad(new String("OptionsFrame.java"))),
	makeSchedule(260000000, MakeSchedule.versionID, pad(new String("MakeSchedule.java"))),
	buildSched  (270000000, BuildScheduleThread.versionID, pad(new String("BuildScheduleThread.java"))),
	courseColor (280000000, CourseColor.versionID, pad(new String("CourseColor.java"))),
	tab         (290000000, Tab.versionID, pad(new String("Tab.java"))),
	popup       (300000000, Popup.versionID, pad(new String("Popup.java"))),
	buttonTab   (310000000, XTabComponent.getVersionID(), pad(new String("XTabComponent.java"))),
	scheduleWrap(320000000, ScheduleWrap.versionID, pad(new String("ScheduleWrap.java"))),
	printUtility(330000000, PrintUtilities.versionID, pad(new String("PrintUtilities.java"))),
	rmpAssist	(340000000, ParseAssistThread.versionID, pad(new String("ParseAssistThread.java"))),
	profDatabase(350000000, ProfDatabase.versionID, pad(new String("ProfDatabase.java"))),
	printThread (360000000, PrintThread.versionID, pad(new String("PrintThread.java"))),
	buildAssist (370000000, BuildAssistThread.versionID, pad(new String("BuildAssistThread.java"))),
	scheduleVect(380000000, ScheduleVector.versionID, pad(new String("ScheduleVector.java"))),
	threadSynch (390000000, ThreadSynch.versionID, pad(new String("ThreadSynch.java"))),
	viewCourse  (400000000, ViewCourse.versionID, pad(new String("ViewCourse.java"))),
	courseLink	(410000000, LinkedCourses.versionID, pad(new String("LinkedCourses.java"))),
	courseList	(420000000, CourseList.versionID, pad(new String("CourseList.java"))),
	courseType  (430000000, CourseType.versionID, pad(new String("CourseType.java"))),
	textFilter  (440000000, JTextFieldFilter.getVersionID(), pad(new String("JTextFieldFilter.java"))),
	conflict    (460000000, Conflict.getVersionID(), pad(new String("Conflict.java"))),
	;
	
	
	/*********************************************************
	 * The following represents the release version number
	*********************************************************/
	private final static String releaseVersion = Main.version;//get version
	private final static long yearDiv = 1000000000L;//year divisor
	private final static long monthDiv = 10000000L;//month divisor
	private final static long dayDiv = 100000L;	//day divisor
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2003021300092L;	//file version
	
	
	/*********************************************************
	 * The following are the fields of the enumerator
	*********************************************************/
	private long value;								//file version
	private String rFileName;						//file name
	protected final long id;						//file ID
	
	/*********************************************************
	 * @purpose Creates a new Version enumerator
	 * 
	 * @param long value: the version value of the enum
	*********************************************************/
	Version( long id, long value, String name){
		this.value = value;			//set version value
		this.rFileName = name;		//set file name
		this.id = id;				//set file identifier
	}
	
	
	/*********************************************************
	 * The following are private static constants for use in 
	 * 		the switch in the padding methods
	*********************************************************/
	private final static int buildLength = 5;		//pad buildStr to 5
	private final static int padLength = 23;		//pad file names to 23
	
	
	/*********************************************************
	 * @purpose returns the value of the enum's version
	 * 
	 * @return long: the serial version of the class represented
	 * 		by the enum
	*********************************************************/
	protected long value(){
		return this.value;							//return value
	}
	
	
	/*********************************************************
	 * @purpose Returns the enum's value as a string
	 * 
	 * @return String: the filename, last modified date and the build number
	*********************************************************/
	@Override
	public String toString(){
		long year = (this.value/(yearDiv)) * yearDiv;//get year
		String yearStr = Long.toString(year/(yearDiv));//send year to string
		long month = ((this.value - year)/(monthDiv))*(monthDiv);//get month
		String monthStr = Long.toString(month/(monthDiv));//send month to string
		long day = ((this.value - year - month)/(dayDiv))*(dayDiv);//get day
		String dayStr = Long.toString(day/(dayDiv));//send day to string
		long build = (this.value - year - month - day);//get build number
		String buildStr = Long.toString(build);//send build number to string
					//above line find the last modified date for the file
		
		while (buildStr.length() < buildLength){
			buildStr = "0" + buildStr;			//pad 0's until buildLength is reached
		}
		
				//return the last modified date and the daily build number
		return new String(this.rFileName + " " +
				monthStr + "/" + dayStr + "/" + 
				yearStr + " " + buildStr);
	}
	
	
	/*********************************************************
	 * @purpose  Returns the release version of the program and
	 * 		the main program build number
	 * 
	 * @return String: the Release version and the build number
	*********************************************************/
	public static String version(){
		return new String(releaseVersion + " " + Main.buildNumber);
	}				//return the release version and build number from main
	
	
	/*********************************************************
	 * @purpose Returns the string with spaced padded to fill 25
	 * 		characters
	 * 
	 * @param String toPad: The string to pad to 25 characters
	 * 
	 * @return String: the padded string;
	*********************************************************/
	@SuppressWarnings("all")//suppress the parameter assignment warning
	public static String pad(String toPad){
		while (toPad.length() < padLength){			//pads the string with " "
			toPad += " ";
		}
		return toPad;								//return padded string
	}


	/*********************************************************
	 * @purpose Returns the file name associated with the class
	 * 
	 * @return String: the filename of the class
	*********************************************************/
	public String getFileName() {
		return rFileName;							//return the filename
	}
	
	
	/*********************************************************
	 * @purpose Returns the last modified date
	 * 
	 * @return String: the last modified date
	*********************************************************/
	public static String date(){
		long val = 0;								//initial long
		
		for(Version item: Version.values()){		//for each file
			val = Math.max(val, item.value);		//get last modified date
		}			
		
		long year = (val/(yearDiv)) * yearDiv;//get year
		String yearStr = Long.toString(year/(yearDiv));//get year as string
		long month = ((val - year)/(monthDiv))*(monthDiv);//get month
		String monthStr = Long.toString(month/(monthDiv));//get month as strin
		long day = ((val - year - month)/(dayDiv))*(dayDiv);//get day
		String dayStr = Long.toString(day/(dayDiv));//get day as string
		
		return new String(monthStr + "/" 	//return date last modified
				+ dayStr + "/" + yearStr);
	}
}
