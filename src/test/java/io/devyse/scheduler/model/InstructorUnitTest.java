/**
 * @(#) InstructorUnitTest.java
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

import java.util.Random;

import io.devyse.scheduler.model.simple.SimpleInstructor;
import io.devyse.scheduler.model.stub.StubTermDataSet;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Unit tests for the AbstractInstructor base class to confirm the default
 * functionality has appropriate semantics.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
@Test(	groups = {"unit","interface","Instructor.basic"})
public class InstructorUnitTest {

	/**
	 * TermDataSet instances for use in testing the Instructor functionality
	 * 
	 * d0 < d1 < d2
	 */
	private TermDataSet d0, d1, d2;
	
	/**
	 * Instructor names for use in testing the Instructor functionality
	 * 
	 * n0 < n1 < n2
	 */
	private String n0, n1, n2;
	
	/**
	 * 
	 * i0 == i0b -> (d0)(n0)
	 * i0 equals i0c -> (d0)(n0)
	 * i0 < i1 < i2 -> (n0)(d0, d1, d2)
	 * i0 < i3 < i4 -> (d0)(n0, n1, n2)
	 * 
	 */
	private Instructor i0, i0b, i0c, i1, i2, i3, i4;
	
	/**
	 * Prepare the stub instances and the Simple instance for testing the Instructor functionality
	 */
	@BeforeTest
	public void setup(){
		//instructor names of increasing value
		n0 = "1";
		n1 = "5";
		n2 = "9";
		
		//TermDataSets of increasing value
		d0 = StubTermDataSet.newTermDataSet(1);
		d1 = StubTermDataSet.newTermDataSet(5);
		d2 = StubTermDataSet.newTermDataSet(9);
		
		//same uniqueness fields
		i0 = SimpleInstructor.newInstructor(d0, n0);
		i0b = i0;
		i0c = SimpleInstructor.newInstructor(d0, n0);
		
		//varying by TermDataSet
		i1 = SimpleInstructor.newInstructor(d1, n0);
		i2 = SimpleInstructor.newInstructor(d2, n0);
		
		//varying by instructor name
		i3 = SimpleInstructor.newInstructor(d0, n1);
		i4 = SimpleInstructor.newInstructor(d0, n2);
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
		
		//TODO equals tests
		
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
	
		//TODO hashcode tests
		
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
				(Random r) -> {return SimpleInstructor.newRandomInstructor(r);}
		);
	}

	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. 
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		//TODO compareTo tests
		
		ct.assertAll();
	}
}
