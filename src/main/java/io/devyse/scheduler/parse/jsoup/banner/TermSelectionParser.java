/**
 * @(#) TermSelectionParser.java
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

import io.devyse.scheduler.model.BasicTerm;
import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.parse.jsoup.FormParser;
import io.devyse.scheduler.retrieval.TermSelector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

/**
 * JSoup Parser which parser the Banner term selection page, selects a term 
 * (either by prompting the user or via another mechanism), and submits the 
 * term selection form to the server.
 * 
 * @author Mike Reinhold
 *
 */
public class TermSelectionParser extends FormParser {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Selector mechanism which will be used to select term code during download
	 */
	private TermSelector selector;
	
	/**
	 * Create a new TermSelectionParser for the specified TermSelection document. Use
	 * the specified TermSelector mechanism to determine which term should be submitted
	 * in the TermSelection form.
	 * 
	 * @param document the term selection document which contains the available terms
	 * @param selector the term selection mechanism for deciding among available terms
	 */
	public TermSelectionParser(Document document, TermSelector selector) {
		super(document);
		
		this.selector = selector;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try{
			parseTermSelectForm(this.getSource());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Parse the Term Selection Form contained within the document
	 * 
	 * @param document the document containing the Term Selection form
	 * @throws IOException if there is an issue submitting the selection form
	 */
	//TODO convert the static strings to configurations
	private void parseTermSelectForm(Document document) throws IOException{
		//find the form within the document and prepare to submit the form
		FormElement form = (FormElement)document.select("form").first();
		Connection connection = processForm(form);

		Collection<Connection.KeyVal> data = new ArrayList<>();
				
		//TODO handle other form inputs?
		
		//find the form element which will hold the term selection and get the name
		Element termSelect = document.select("form select#term_input_id").first();
		String termParameter = termSelect.attr("name");		
		
		//find the form element which contains the available terms
		Elements terms = document.select("form select#term_input_id option");
		
		//extract the available term codes and names from the form field
		List<Term> termOptions = new ArrayList<>();
		System.out.println(termParameter + " selected from:");
		for(Element term: terms){
			String code = term.attr("value");
			if(code.compareTo("") != 0){
				Term found = new BasicTerm(code, term.text());
				termOptions.add(found);
				
				//TODO stop this debug printing
				System.out.println(found.getId() + ": " + found.getName());
			} else {
				
				//TODO stop debug printing
				//TODO log info
				System.out.println("Ignored empty entry: " + code);
			}
		}
		
		//select the term to download using the term selector and add it to the HTTP connection parameters
		data.add(HttpConnection.KeyVal.create(termParameter, selector.selectTerm(termOptions).getId()));
		
		//submit the form and parse the resulting document as the result of the task
		this.setRawResult(connection.data(data).execute().parse());
	}
}
