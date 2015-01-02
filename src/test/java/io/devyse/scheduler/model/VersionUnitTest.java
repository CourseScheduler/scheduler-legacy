/**
 * 
 */
package io.devyse.scheduler.model;

import java.time.OffsetDateTime;

/**
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class VersionUnitTest {

	/**
	 * @return
	 */
	public static Version generateVersion(){
		return new SimpleVersion(OffsetDateTime.now());
	}

}
