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
 * File: Serial.java
 * 
 * Contains class:
 * 
 * 		Serial:
 * 
 * 			Purpose: To simplify the process of serializing
 * 				 and deserializing classes
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//define as member of the scheduler package


/*********************************************************
 * Import FileInputStream for opening a file input stream
 * Import FileOutputStream for opening a file output stream
 * Import ObjectInputStream for opening a file input stream
 * Import ObjectOutputStream for opening a file output stream
*********************************************************/
import java.io.FileInputStream;				//for opening file streams in
import java.io.FileOutputStream;			//for opening file streams out
import java.io.ObjectInputStream;			//for opening object streams in
import java.io.ObjectOutputStream;			//for opening object streams out
import java.io.File;						//for making file objects


/*********************************************************
 * Class Serial
 * 
 * @purpose Contains methods for serializing and deserializing
 * 		classes
*********************************************************/
public class Serial {

	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008051200008L; //object version
	
	
	/********************************************************
	 * @purpose Load a DataType T from the specified file
	 * 
	 * @param String fileName: the path and name of the file to 
	 * 		open and deserialize
	 * 
	 * @return T: the object read from the file in the specified 
	 * 		data type
	 * 
	 * @see SuppressWarnings("unchecked")
	*********************************************************/
	@SuppressWarnings("unchecked")
	public static<T> T load(String fileName){
		try{								
			ObjectInputStream in = new ObjectInputStream(//get new objec input stream
					new FileInputStream(new File(fileName)));//from the file input stream
			T result = (T)in.readObject();			//read in object from stream and cast to T
			in.close();								//close stream
			return result;							//return the result of the read and cast
		}
		catch (Exception ex){						//catch class cast exceptions
			System.out.println(ex.toString());		//print out error messages
			return null;							//return null object
		}
	}
	
	
	/********************************************************
	 * @purpose Saves the specified object of DataType T to the
	 * 		specified file by serialization
	 * 
	 * @param String fileName: the name of the file to serialize into
	 * @param T item: the object of DataType T to serialize
	 * 
	 * @return boolean: if the serialization was sucessful
	*********************************************************/
	public static<T> boolean save(String fileName, T item){
		try{
			ObjectOutputStream out = new ObjectOutputStream(//get new object output stream
				new FileOutputStream(new File(fileName)));//from a file output stream
			out.writeObject(item);					//write out the object
			out.close();							//close the stream and return true
			return true;							
		}
		catch (Exception ex){						//catch exceptions
			return false;							//return failure
		}
	}
}
