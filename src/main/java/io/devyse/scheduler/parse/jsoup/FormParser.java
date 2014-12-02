/**
 * @(#) FormParser.java
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
package io.devyse.scheduler.parse.jsoup;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;

/**
 * Abstract FormParser to provide basic functionality for processing documents containing
 * a form.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public abstract class FormParser extends AbstractParser<Document> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default form selector - very simple. Just find form tags
	 */
	public static final String DEFAULT_FORM_SELECTOR = "form";
	
	/**
	 * The document resulting from submitting the form
	 */
	private Document result;
	
	/**
	 * Create a new FormParser which will parse the specified document and retrieve 
	 * form fields and response parameters.
	 * 
	 * @param document the document which contains the form that will be parsed
	 */
	public FormParser(Document document) {
		super(document);
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#getRawResult()
	 */
	@Override
	public Document getRawResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#setRawResult(java.lang.Object)
	 */
	@Override
	protected void setRawResult(Document value) {
		this.result = value;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.parse.jsoup.AbstractParser#parse(org.jsoup.nodes.Document)
	 */
	@Override
	protected void parse(Document document) throws IOException {
		//parse the form from the document
		FormElement form = parseForm(document);
		
		//prepare a connection from the form
		Connection connection = prepareForm(form);
		
		//build the form parameters
		Collection<KeyVal> data = buildFormParameters(form, connection);
		
		//submit the form and store the resulting document
		submitForm(connection, data);
	}
	
	/**
	 * Parse the first form from the document and return it for further processing.
	 * 
	 * This method is called by the {@link #parse(Document)} method.
	 * 
	 * @param document the document containing the form
	 * @return the first form found in the document 
	 */
	protected FormElement parseForm(Document document){
		return parseForm(document, DEFAULT_FORM_SELECTOR); 
	}
	
	/**
	 * Parse the first form found in the document matching the specified selector and return it for further processing.
	 * 
	 * This method is called by {@link #parseForm(Document)} using the following selector:
	 * {@value #DEFAULT_FORM_SELECTOR}
	 * 
	 * @param document the document containing the form
	 * @param selector the css selector for finding the form
	 * @return the first form found in the document matching the selector
	 */
	protected FormElement parseForm(Document document, String selector){
		FormElement form = (FormElement)document.select(selector).first();
		return form;
	}
	
	/**
	 * Process the form to determine the target URL and the submission method specified in the form. Prepare
	 * a connection for performing the form action.
	 * 
	 * @param form the form element from which the connection will be built
	 * @return a prepared connection based on the form action and method
	 */
	protected Connection prepareForm(FormElement form){
		String action = form.absUrl("action");
		String method = form.attr("method");

		//TODO Log this instead of print it
		System.out.println("Form submits to " + action + " via " + method);
		
		return form.submit();
	}
	
	/**
	 * Build the Key-value pair parameters from the form content for use in executing the form submission.
	 * 
	 * FormParser implementations should implement this method in order to ensure the form request parameters 
	 * are properly formed.
	 * 
	 * @param form the form element which the parameters should be built from and for
	 * @param connection the prepared connection for the form
	 * @return the collection of key-value pairs which are input into the form
	 */
	protected abstract Collection<KeyVal> buildFormParameters(FormElement form, Connection connection);
	
	/**
	 * Submit the form using the connection previously prepared from the form along
	 * with the specified key-value pair parameters
	 * 
	 * @param connection the connection previously prepared for the form referencing the target URL and submission method
	 * @param data the collection of key-value parameters that are the form contents being submitted
	 * @throws IOException if there are issues executing the form submission
	 */
	protected void submitForm(Connection connection, Collection<KeyVal> data) throws IOException{
		this.setRawResult(connection.data(data).execute().parse());
	}
}
