/**
 * 
 */
package io.devyse.scheduler.model;

import java.util.Random;

/**
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class TermDataSetUnitTest {

	/**
	 * @param generator
	 * @return
	 */
	public static TermDataSet generateTermDataSet(Random generator){
		return new SimpleTermDataSet(
			TermUnitTest.generateTerm(generator),
			VersionUnitTest.generateVersion()
		);
	}

}
