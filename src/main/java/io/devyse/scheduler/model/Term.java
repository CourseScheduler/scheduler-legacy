/**
 * 
 */
package io.devyse.scheduler.model;

/**
 * @author mreinhold
 *
 */
public interface Term {

	/**
	 * The unique term identifier for this term as defined by
	 * the university. Often a numeric representation of the
	 * year and semester (201402)
	 *
	 * @return the term identifier
	 */
	public String getId();
	
	/**
	 * The common name of the term as defined by the university.
	 * Often a "plain language" representation of the year and
	 * semester (Spring 2014)
	 *
	 * @return the term name
	 */
	public String getName();
}
