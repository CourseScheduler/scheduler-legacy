/**
 * @(#) UniversityUnitTest.java
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
package io.devyse.scheduler.model;

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Test the basic functionality and default implementations
 *
 * @author Mike Reinhold
 *
 */
@Test(	groups = {"unit","interface","University.basic"}, 
		dependsOnGroups = {}
)
public class UniversityUnitTest {
	
	/**
	 * Faux university names for testing ordering
	 */
	private String less = "1";
	private String middle = "5";
	private String more = "9";
	
	/**
	 * Instances for unit testing the University interface
	 * 
	 * 		u1 = middle
	 * 		u2 = u1
	 * 		u3 = middle
	 * 		u4 = less
	 * 		u5 = more
	 */
	private University u1, u2, u3, u4, u5;

	/**
	 * Prepare the test instances and necessary stubs for use in the tests
	 *
	 */
	@BeforeClass
	public void setup() {
		u1 = new SimpleUniversity(middle);
		u2 = u1;
		u3 = new SimpleUniversity(middle);
		u4 = new SimpleUniversity(less);
		u5 = new SimpleUniversity(more);
	}

	/**
	 * Confirm the semantics of reference and object equality.
	 * 
	 * Note:
	 * 	"Sameness" is via instance equality (via ==)
	 * 	"Equality" is object semantic equality (via equals)
	 */
	@Test
	public void confirmEquals() {
		SoftAssert eq = new SoftAssert();
		
		eq.assertSame(u1, u2, "References to same instance should be same");
		eq.assertEquals(u1, u2, "References to same instance should be equal");
		eq.assertEquals(u1, u3, "Instances with same uniqueness fields should be equal");

		eq.assertNotEquals(u1, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(u1, u4, "Instances with varying names should not be equal");
		
		eq.assertAll();
	}
	
	/**
	 * Confirm hashCode semantics is consistent with equals()
	 * 
	 * Note: 
	 * 	It is not required that "unequal" objects return different hashcodes,
	 * 	however "equal" and "same" objects must return the same hashcode. That said,
	 * 	including a test for "variety" between hashcodes so that all instances don't 
	 * 	return the same value (would kill performance of data structures that use the
	 * 	hashcode)
	 */
	@Test
	public void confirmHashCode() {
		SoftAssert hc = new SoftAssert();
		
		hc.assertEquals(u1, u2, "References to same instance should have same hashcode");
		hc.assertEquals(u1, u3, "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				u1.hashCode() == u4.hashCode() &&
				u1.hashCode() == u5.hashCode() 
		;
		hc.assertFalse(variety, "Hashcode should return a variety of values for instances with varying uniqueness fields");
		
		hc.assertAll();
	}

	
	/**
	 * Confirm the quality of the hashCode method meets some minimum standards - 
	 * will avoid some too many instances hashing to the same value for a single
	 * hash as well as that the average number of collisions per hash is under
	 * a specified value.
	 */
	@Test
	public void confirmHashCodeQuality_RandomData(){
		HashCodeQualityHelper.confirmHashCodeQuality( 
				(Random r) -> {return UniversityUnitTest.generateUniversity(r);}
		);
	}
	
	/**
	 * Confirm the quality of the hashCode method meets some minimum standards - 
	 * will avoid some too many instances hashing to the same value for a single
	 * hash as well as that the average number of collisions per hash is under
	 * a specified value.
	 */
	@Test
	public void confirmHashCodeQuality_RealisticData(){
		HashCodeQualityHelper.confirmHashCodeQuality(Arrays.asList(
				u1, u2, u3, u4, u5
		));
	}
	
	/**
	 * Generate a University based on the current state of a Random
	 *
	 * @param generator a Random for use in building the University
	 * @return the next University
	 */
	public static University generateUniversity(Random generator){
		return new SimpleUniversity(
					Long.toHexString(generator.nextLong())
		);
	}
	
	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. 
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		ct.assertEquals(u1.compareTo(u2), 0, "References to same instance should be equal");
		ct.assertEquals(u2.compareTo(u1), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(u1.compareTo(u3), 0, "Instances with same fields should be equal");
		ct.assertEquals(u2.compareTo(u1), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//check name ordering and reversability
		ct.assertEquals(Math.signum(u1.compareTo(u4)), 1.0f, "Positive result expected for instance with lesser name");
		ct.assertEquals(Math.signum(u4.compareTo(u1)), -1.0f, "Negative result expected for instance with greater name");
		ct.assertEquals(Math.signum(u1.compareTo(u5)), -1.0f, "Negative result expected for instance with greater name");
		ct.assertEquals(Math.signum(u5.compareTo(u1)), 1.0f, "Positive result expected for instance with lesser name");
		
		//check transitivity on university and id
		ct.assertEquals(Math.signum(u4.compareTo(u1)), Math.signum(u1.compareTo(u5)), "Transitivity expected for instances varying on name");
		
		ct.assertAll();
	}
}
