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

import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.model.Terms;
import io.devyse.scheduler.parse.jsoup.FormParser;
import io.devyse.scheduler.retrieval.TermSelector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * JSoup Parser which parser the Banner term selection page, selects a term 
 * (either by prompting the user or via another mechanism), and submits the 
 * term selection form to the server.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class TermSelectionParser extends FormParser {

	/**
	 * Static logger 
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(TermSelectionParser.class);
	
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
	 * @see io.devyse.scheduler.parse.jsoup.FormParser#buildFormParameters(org.jsoup.nodes.FormElement, org.jsoup.Connection)
	 */
	@Override
	protected Collection<KeyVal> buildFormParameters(FormElement form, Connection connection){
		Collection<KeyVal> data = new ArrayList<>();
				
		//TODO handle other form inputs?
		
		//find the form element which will hold the term selection and get the name
		Element termSelect = form.select("select#term_input_id").first();
		String termParameter = termSelect.attr("name");		
		
		//find the form element which contains the available terms
		Elements terms = form.select("select#term_input_id option");
		
		//extract the available term codes and names from the form field
		List<Term> termOptions = new ArrayList<>();
		logger.debug("{} selected from:", termParameter);
		for(Element term: terms){
			String code = term.attr("value");
			if(code.compareTo("") != 0){
				Term found = Terms.newTerm(/*TODO*/null, code, term.text());
				termOptions.add(found);
				
				logger.debug("{}: {}", found.getTermIdentifier(), found.getName());
			} else {
				logger.debug("Ignored empty entry: {}", code);
			}
		}
		
		//select the term to download using the term selector and add it to the HTTP connection parameters
		data.add(HttpConnection.KeyVal.create(termParameter, selector.selectTerm(termOptions).getTermIdentifier()));
		
		return data;
	}
}
