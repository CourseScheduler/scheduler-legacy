/**
 * 
 */
package io.devyse.scheduler.model;

import java.time.OffsetDateTime;

/**
 * Basic implementation of a Version object for use in testing base and abstract functionality
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class SimpleVersion extends AbstractVersion {

	/**
	 * Create a new SimpleVersion using the specified retrieval time
	 * 
	 * @param retrievalTime the date and time of the
	 */
	protected SimpleVersion(OffsetDateTime retrievalTime) {
		super(retrievalTime);
	}


}
