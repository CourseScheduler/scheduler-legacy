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

		//Instructor is equal to another if the TermDataSets are equal and the names are equal
		eq.assertSame(i0, i0b, "References to the same instance should be the same");
		eq.assertEquals(i0, i0b, "References to the same instance should be equal");
		eq.assertEquals(i0, i0c, "Instances with same uniqueness fields should be equal");
		
		//Instructor is not equal under any other condition
		eq.assertNotEquals(i0, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(i0, i1, "Instances with varying date time blocks should not be equal");
		eq.assertNotEquals(i0, i3, "Instances with varying sections should not be equal");
		
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
		hc.assertEquals(i0.hashCode(), i0b.hashCode(), "References to same instance should have same hashcode");
		hc.assertEquals(i0.hashCode(), i0c.hashCode(), "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				i0.hashCode() == i1.hashCode() &&
				i0.hashCode() == i2.hashCode() &&
				i0.hashCode() == i3.hashCode() &&
				i0.hashCode() == i4.hashCode()
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

		//compareTo should be consistent with equals
		ct.assertEquals(i0.compareTo(i0b), 0, "References to same instance should be equal");
		ct.assertEquals(i0b.compareTo(i0), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(i0.compareTo(i0c), 0, "Instances with same fields should be equal");
		ct.assertEquals(i0c.compareTo(i0), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//compareTo by Term first and name second
		ct.assertEquals(Math.signum(i0.compareTo(i1)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(i1.compareTo(i0)), 1.0f, "Positive result expected for instance with lesser term");
		ct.assertEquals(Math.signum(i0.compareTo(i2)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(i2.compareTo(i0)), 1.0f, "Positive result expected for instance with lesser term");
		
		ct.assertEquals(Math.signum(i0.compareTo(i3)), -1.0f, "Negative result expected for instance with greater name");
		ct.assertEquals(Math.signum(i3.compareTo(i0)), 1.0f, "Positive result expected for instance with lesser name");
		ct.assertEquals(Math.signum(i3.compareTo(i4)), -1.0f, "Negative result expected for instance with greater name");
		ct.assertEquals(Math.signum(i4.compareTo(i3)), 1.0f, "Positive result expected for instance with lesser name");

		ct.assertEquals(Math.signum(i2.compareTo(i1)), Math.signum(-i1.compareTo(i2)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (lesser term)");
		ct.assertEquals(Math.signum(i0.compareTo(i1)), Math.signum(-i1.compareTo(i0)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (greater term)");
		ct.assertEquals(Math.signum(i4.compareTo(i3)), Math.signum(-i3.compareTo(i4)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (lesser name)");
		ct.assertEquals(Math.signum(i0.compareTo(i3)), Math.signum(-i3.compareTo(i0)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (greater name)");
		
		ct.assertEquals(Math.signum(i0.compareTo(i1)), Math.signum(i1.compareTo(i2)), "Transitivity expected for instances varying on term");
		ct.assertEquals(Math.signum(i0.compareTo(i3)), Math.signum(i3.compareTo(i4)), "Transitivity expected for instances varying on name");
		ct.assertAll();
	}
}
