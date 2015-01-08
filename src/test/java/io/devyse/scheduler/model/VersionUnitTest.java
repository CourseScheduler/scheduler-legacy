/**
 * @(#) VersionUnitTest.java
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

import io.devyse.scheduler.model.simple.SimpleVersion;

import java.time.OffsetDateTime;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Unit tests for the AbstractVersion base class to confirm the default
 * functionality has appropriate semantics.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
@Test(	groups = {"unit","interface","Version.basic"})
public class VersionUnitTest {

	/**
	 * Version instances for use by the Version tests
	 * 
	 * 	v0 = v0b = v0c
	 * 	v0 < v1 < v2
	 */
	Version v0, v0b, v0c, v1, v2;
	
	/**
	 * Prepare the class for unit testing
	 */
	@BeforeClass
	public void setup(){
		v0 = SimpleVersion.newVersionFromTimestamp(OffsetDateTime.MIN);
		v0b = v0;
		v0c = SimpleVersion.duplicateVersion(v0);
		v1 = SimpleVersion.newVersionFromTimestamp(OffsetDateTime.now());
		v2 = SimpleVersion.newVersionFromTimestamp(OffsetDateTime.MAX);
	}

	/**
	 * Confirm the semantics of reference and object equality.
	 * 
	 * Note:
	 * 	"Sameness" is via instance equality (via ==)
	 * 	"Equality" is object semantic equality (via equals)
	 */
	@Test
	public void confirmEquals(){
		SoftAssert eq = new SoftAssert();
		
		//Version is equal if the timestamps are equal
		eq.assertSame(v0, v0b, "References to same instance should be the same");
		eq.assertEquals(v0, v0b, "References to same instance should be equal");
		eq.assertEquals(v0, v0c, "Instances with same uniqueness fields should be equal");
		
		//Version is not equal under any other condition
		eq.assertNotEquals(v0, null, "Non-null instance should not be equal to null");
		eq.assertNotEquals(v0, v1, "Instances with varying timestamps should not be equal");
		
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
	public void confirmHashCode(){
		SoftAssert hc = new SoftAssert();
		
		//hashcode semantics should be consistent with equals()
		hc.assertEquals(v0.hashCode(), v0b.hashCode(), "References to same instance should have same hashcode");
		hc.assertEquals(v0.hashCode(), v0c.hashCode(), "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				v0.hashCode() == v1.hashCode() &&
				v0.hashCode() == v2.hashCode() 
		;
		hc.assertFalse(variety, "Hashcode should return a variety of values for instance with varying uniqueness fields");
				
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
				(Random r) -> {return SimpleVersion.newRandomVersion(r);}
		);
	}
	
	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. 
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		//compareTo should be consistent with equals
		ct.assertEquals(v0.compareTo(v0b), 0, "References to same instance should be equal");
		ct.assertEquals(v0b.compareTo(v0), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(v0.compareTo(v0c), 0, "Instances with same fields should be equal");
		ct.assertEquals(v0c.compareTo(v0), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//compareTo by timestamp
		ct.assertEquals(Math.signum(v0.compareTo(v1)), -1.0f, "Negative result expected for instance with greater date time stamp");
		ct.assertEquals(Math.signum(v1.compareTo(v0)), 1.0f, "Positive result expected for instance with lesser date time stamp");
		ct.assertEquals(Math.signum(v1.compareTo(v2)), -1.0f, "Negative result expected for instance with greater date time stamp");
		ct.assertEquals(Math.signum(v2.compareTo(v1)), 1.0f, "Positive result expected for instance with lesser date time stamp");
		
		//ensure directionality
		ct.assertEquals(Math.signum(v2.compareTo(v1)), Math.signum(-v1.compareTo(v2)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (lesser time stamp)");
		
		//ensure transitivity
		ct.assertEquals(Math.signum(v0.compareTo(v1)), Math.signum(v1.compareTo(v2)), "Transitivity expected for instances varying on time stamp");
				
		ct.assertAll();
	}
}
