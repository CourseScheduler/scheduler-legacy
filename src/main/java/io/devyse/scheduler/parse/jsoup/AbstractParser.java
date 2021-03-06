/**
 * @(#) AbstractParser.java
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

import java.util.concurrent.ForkJoinTask;

import org.jsoup.nodes.Document;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Abstract JSoup Parser which provides some basic functionality for parsing a source
 * document and returning some result object.
 * 
 * This AbstractParser is modeled as a ForkJoinTask and should be executed in a ForkJoinPool.
 * The AbstractParser may spawn additional ForkJoinTasks as necessary.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public abstract class AbstractParser<V> extends ForkJoinTask<V> {

	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(AbstractParser.class);
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The document which will be parsed by this task class
	 */
	private Document source;
	
	/**
	 * Result value which will be returned from this task
	 */
	private V result;

	/**
	 * The connection timeout which should be used for any connections
	 * created or consumed by this AbstractParser
	 */
	private int timeout;
	
	/**
	 * Create a new AbstractParser to parse the specified document.
	 * 
	 * @param document the document which will be parsed by the AbstractParser
	 * @param timeout the connection timeout
	 */
	public AbstractParser(Document document, int timeout){
		super();
		
		this.setSource(document);
		this.setTimeout(timeout);
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#getRawResult()
	 */
	@Override
	public V getRawResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#setRawResult(java.lang.Object)
	 */
	@Override
	protected void setRawResult(V result) {
		this.result = result;
	}
	
	/**
	 * @return the source document used by the AbstractParser
	 */
	protected Document getSource() {
		return this.source;
	}

	/**
	 * @param source the source document which will be processed by the AbstractParser
	 */
	private void setSource(Document source) {
		this.source = source;
	}
	
	/**
	 * Change the socket connection timeout for the form submission
	 * 
	 * @param timeout the desired socket timeout for the connection to the form target
	 */
	protected void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	/**
	 * @return the current connection timeout in milliseconds
	 */
	protected int getTimeout(){
		return this.timeout;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try {
			parse(this.getSource());
			return true;
		} catch (Exception e) {
			logger.error("Unable to parse the source document: {}", source, e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Parse the specified document and process the data contained within as appropriate.
	 * 
	 * Abstract parser implementations should implement this method as necessary to handle the 
	 * document form and contents.
	 * 
	 * @param document the document which should be parsed by the AbstractParser
	 * @throws Exception in the event there is an issue parsing the document
	 */
	protected abstract void parse(Document document) throws Exception;
}
