package Scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CourseList implements Iterable<String>, Serializable{
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008082500002L;//serial ID
	protected static final long serialVersionUID =1L + 
			Version.courseList.id;//serial ID
	
	
	private ArrayList<String> list;
			
	public CourseList(String[] link){
		list = new ArrayList<String>(Arrays.asList(link));
	}
	
	public Iterator<String> iterator(){
		return list.iterator();
	}
	
	
	public void remove(Object o){
		list.remove(o);
	}
	
	public boolean contains(String course){
		return  list.contains(course);
	}
	
	public int size(){
		return list.size();
	}
	
	
	@Override
	public String toString(){
		String toReturn = list.get(0);
		
		for(int pos = 1; pos < list.size(); pos++){
			toReturn += " :: " + list.get(pos);
		}
		
		return toReturn;
	}
}
