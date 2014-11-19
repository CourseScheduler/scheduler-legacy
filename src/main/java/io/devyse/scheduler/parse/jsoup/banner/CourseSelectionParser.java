/**
 * @(#) CourseSelectionParser.java
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

import io.devyse.scheduler.parse.jsoup.FormParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

/**
 * JSoup FormParser which processes a Banner course selection page and
 * submits the form to retrieve course data for the courses corresponding
 * to the inputs into the course selection form.
 * 
 * @author Mike Reinhold
 *
 */
public class CourseSelectionParser extends FormParser {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new CourseSelectionParser based on the specified document,
	 * which should contain a form from Banner for selecting course data.
	 */
	public CourseSelectionParser(Document document) {
		super(document);
	}

	//TODO refactor FormParser to use additional common methods
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try{
			parseCourseSelectionForm(this.getSource());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Process the Course Selection Form from the document to build the connection and extract
	 * form fields for use as key-value pair parameters
	 * 
	 * @param document the document containing the course selection form
	 * @throws IOException if there are issues executing the form submission
	 */
	private void parseCourseSelectionForm(Document document) throws IOException{
		//TODO remove debugging statements and log instead
		System.out.println("=== Subject Selection");
		FormElement form = (FormElement)document.select("form").first();
		Connection connection = processForm(form);
		
		Collection<Connection.KeyVal> data = new ArrayList<>();

		//for some reason the jsoup prepare builds out the sel_day fields when the web form does not when using a browser
		//unless this is run wide open it only seems to match a small set of courses, possibly due to "AND"-like behavior
		//on the day selection evaluation
		Collection<Connection.KeyVal> toRemove = new ArrayList<>();
		for(Connection.KeyVal entry: connection.request().data()){
			if(entry.key().equals("sel_day") && !entry.value().equals("dummy")){
				toRemove.add(entry);
			}
		}
		connection.request().data().removeAll(toRemove);
				
		//add in the subject selection options
		Elements subjects = form.select("select#subj_id option");
		for(Element subject: subjects){
			System.out.println("sel_subj: " + subject.attr("value") + " - " + subject.text());
			data.add(HttpConnection.KeyVal.create("sel_subj", subject.attr("value")));
		}
		
		//add in the hour restriction options
		//TODO pull these automatically
		data.add(HttpConnection.KeyVal.create("begin_hh", "0"));
		data.add(HttpConnection.KeyVal.create("begin_mi", "0"));
		data.add(HttpConnection.KeyVal.create("begin_ap", "a"));
		data.add(HttpConnection.KeyVal.create("end_hh", "0"));
		data.add(HttpConnection.KeyVal.create("end_mi", "0"));
		data.add(HttpConnection.KeyVal.create("end_ap", "a"));
		
		this.setRawResult(connection.data(data).execute().parse());
	}
}
