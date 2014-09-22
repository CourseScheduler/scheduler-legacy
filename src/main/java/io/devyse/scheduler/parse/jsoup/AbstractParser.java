/**
 * 
 */
package io.devyse.scheduler.parse.jsoup;

import java.util.concurrent.ForkJoinTask;

import org.jsoup.nodes.Document;

/**
 * @author Mike Reinhold
 *
 */
public abstract class AbstractParser<V> extends ForkJoinTask<V> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The document which will be parsed by this task class
	 */
	private Document document;
	
	/**
	 * Result value which will be returned from this task
	 */
	private V result;

	/**
	 * @param document
	 */
	public AbstractParser(Document document){
		super();
		
		this.setDocument(document);
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
	 * @return the document
	 */
	protected Document getDocument() {
		return this.document;
	}

	/**
	 * @param document the document to set
	 */
	private void setDocument(Document document) {
		this.document = document;
	}
}
