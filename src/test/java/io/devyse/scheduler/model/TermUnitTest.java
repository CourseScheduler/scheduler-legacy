/**
 * @(#) TermUnitTest.java
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

import io.devyse.scheduler.model.simple.SimpleTerm;
import io.devyse.scheduler.model.stub.StubUniversity;

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Unit tests for the Term and AbstractTerm interface and base
 * class to ensure default functionality has the appropriate
 * semantics
 *
 * @author Mike Reinhold
 * @since 4.13.0
 * 
 */
@Test(	groups = {"unit","interface","Term.basic"})
public class TermUnitTest {
	
	/**
	 * Simple values for comparisons
	 */
	private static final String less = "1";
	private static final String middle = "5";
	private static final String more = "9";
	
	/**
	 * Universities for use in testing
	 */
	private University lesserUni, middleUni, greaterUni;
	
	/**
	 * Term instances for testing basic function. Relationship is as follows:
	 * 
	 * 		instance = university, id
	 * 		
	 * 		t1 = middle, middle
	 * 		t2 = t1
	 * 		t3 = middle, middle
	 * 		t4 = less, middle
	 * 		t5 = more, middle
	 * 		t6 = middle, less
	 * 		t7 = middle, more
	 */
	private Term t1, t2, t3, t4, t5, t6, t7;
	
	/**
	 * Prepare the test instances and necessary stubs for use in the tests
	 *
	 */
	@BeforeClass
	public void setup() {
		lesserUni = StubUniversity.newStaticUniversity(1);
		middleUni = StubUniversity.newStaticUniversity(5);
		greaterUni = StubUniversity.newStaticUniversity(9);
		
		t1 = SimpleTerm.newTerm(middleUni, middle, middle);
		t2 = t1;
		t3 = SimpleTerm.newTerm(middleUni, middle, middle);
		t4 = SimpleTerm.newTerm(lesserUni, middle, middle);
		t5 = SimpleTerm.newTerm(greaterUni, middle, middle);
		t6 = SimpleTerm.newTerm(middleUni, less, less);
		t7 = SimpleTerm.newTerm(middleUni, more, more);
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
		
		eq.assertSame(t1, t2, "References to same instance should be same");
		eq.assertEquals(t1, t2, "References to same instance should be equal");
		eq.assertEquals(t1, t3, "Instances with same uniqueness fields should be equal");

		eq.assertNotEquals(t1, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(t1, t4, "Instances with varying universities should not be equal");
		eq.assertNotEquals(t1, t6, "Instances with varying term identifier should not be equal");
		
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
		
		hc.assertEquals(t1, t2, "References to same instance should have same hashcode");
		hc.assertEquals(t1, t3, "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				t1.hashCode() == t4.hashCode() &&
				t1.hashCode() == t5.hashCode() &&
				t1.hashCode() == t6.hashCode() &&
				t1.hashCode() == t7.hashCode()
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
				(Random r) -> {return SimpleTerm.newRandomTerm(r);}
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
				t1, t2, t3, t4, t5, t6, t7
		));
	}
	
	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. 
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		ct.assertEquals(t1.compareTo(t2), 0, "References to same instance should be equal");
		ct.assertEquals(t2.compareTo(t1), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(t1.compareTo(t3), 0, "Instances with same fields should be equal");
		ct.assertEquals(t2.compareTo(t1), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//check university ordering and reversability
		ct.assertEquals(Math.signum(t1.compareTo(t4)), 1.0f, "Positive result expected for instance with lesser university");
		ct.assertEquals(Math.signum(t4.compareTo(t1)), -1.0f, "Negative result expected for instance with greater university");
		ct.assertEquals(Math.signum(t1.compareTo(t5)), -1.0f, "Negative result expected for instance with greater university");
		ct.assertEquals(Math.signum(t5.compareTo(t1)), 1.0f, "Positive result expected for instance with lesser university");
		
		//check id ordering and reversability 
		ct.assertEquals(Math.signum(t1.compareTo(t6)), 1.0f, "Positive result expected for instance with lesser term id");
		ct.assertEquals(Math.signum(t6.compareTo(t1)), -1.0f, "Negative result expected for instance with greater term id");
		ct.assertEquals(Math.signum(t1.compareTo(t7)), -1.0f, "Negative result expected for instance with greater term id");
		ct.assertEquals(Math.signum(t7.compareTo(t1)), 1.0f, "Positive result expected for instance with lesser term id");
		
		//check transitivity on university and id
		ct.assertEquals(Math.signum(t4.compareTo(t1)), Math.signum(t1.compareTo(t5)), "Transitivity expected for instances varying on university");
		ct.assertEquals(Math.signum(t6.compareTo(t1)), Math.signum(t1.compareTo(t7)), "Transitivity expected for instances varying on term id");
		
		ct.assertAll();
	}
}
