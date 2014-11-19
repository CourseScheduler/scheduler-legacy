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
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

/**
 * Abstract FormParser to provide basic functionality for processing documents containing
 * a form.
 * 
 * @author Mike Reinhold
 *
 */
public abstract class FormParser extends AbstractParser<Document> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

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

	/**
	 * Submit the form using the connection previously prepared from the form along
	 * with the specified key-value pair parameters
	 * 
	 * @param connection the connection previously prepared for the form referencing the target URL and submission method
	 * @param data the collection of key-value parameters that are the form contents being submitted
	 * @throws IOException if there are issues executing the form submission
	 */
	protected void submitForm(Connection connection, Collection<KeyVal> data) throws IOException{
		connection.data(data).execute().parse();
	}
	
	/**
	 * Process the form to determine the target URL and the submission method specified in the form.
	 * 
	 * @param form the form element from which the connection will be built
	 * @return a prepared connection based on the form action and method
	 */
	protected Connection processForm(FormElement form){
		String action = form.absUrl("action");
		String method = form.attr("method");

		//TODO Log this instead of print it
		System.out.println("Form submits to " + action + " via " + method);
		
		return form.submit();
	}
	
	//TODO is this used or even necessary anymore?
	/**
	 * Process the form hidden inputs and build corresponding key-value pair parameters
	 * 
	 * @param form the form element from which the hidden inputs should be converted into submission parameters
	 * @param data the current set of key-value parameters
	 * @return the updated collection of key-value parameters
	 */
	protected Collection<KeyVal> processFormHiddenInputs(FormElement form, Collection<Connection.KeyVal> data){
		Elements parameters = form.select("input[name][type=hidden]");

		for(Element parameter: parameters){
			System.out.println(parameter.attr("name") + ": " + parameter.attr("value"));
			data.add(HttpConnection.KeyVal.create(parameter.attr("name"), parameter.attr("value")));
		}
		
		return data;
	}
}
