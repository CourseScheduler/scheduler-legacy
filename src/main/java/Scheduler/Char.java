/********************************************************
 * Copyright (C) 2008 Course Scheduler Team
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of 
 * 	the GNU General Public License as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * 	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * 	if not, write to:
 * 		Free Software Foundation, Inc.
 * 		59 Temple Place, Suite 330, 
 * 		Boston, MA 02111-1307 USA
********************************************************/

/*********************************************************
 * Course Scheduler
 * File: Char.java
 * 
 * Contains enumerator:
 * 
 * 		Char:
 * 
 * 			Purpose: To enumerate characters
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;						//define as member of Scheduler package


/*********************************************************
 * Enumerator Char:
 * 
 * @purpose Enumerate alphabet characters for rate my professor
 * 		downloads
*********************************************************/
public enum Char {
	a ("A"),
	b ("B"),
	c ("C"),
	d ("D"),
	e ("E"),
	f ("F"),
	g ("G"),
	h ("H"),
	i ("I"),
	j ("J"),
	k ("K"),
	l ("L"),
	m ("M"),
	n ("N"),
	o ("O"),
	p ("P"),
	q ("Q"),
	r ("R"),
	s ("S"),
	t ("T"),
	u ("U"),
	v ("V"),
	w ("W"),
	x ("X"),
	y ("Y"),
	z ("Z");

	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008043000002L;//object ID
	
	
	/*********************************************************
	 * The following are protected constants for the enum
	*********************************************************/
	protected final String value;			//the enum's char value
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose Constructs the enum
	 * 
	 * @param String val: the character represented by the enum
	*********************************************************/
	Char(String val){
		this.value = val;					//set the enum character value
	}
	
	
	/*********************************************************
	 * @purpose Returns the string value of the enum
	 * 
	 * @return String: the character value of the enum
	*********************************************************/
	@Override
	public String toString(){
		return this.value;					//return the char value of the enum
	}
}