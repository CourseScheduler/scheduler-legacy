package Scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class LinkedCourses implements Serializable, Iterable<CourseList> {
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008082200001L;//serial ID
	protected static final long serialVersionUID =1L + 
			Version.courseLink.id;//serial ID
	
	ArrayList<CourseList> links;
	
	public LinkedCourses(){
		links = new ArrayList<CourseList>();
	}
	
	public LinkedCourses(String[] link){
		links = new ArrayList<CourseList>();
		links.add(new CourseList(link));
	}
	
	public void addLinkedCourses(String[] link){
		links.add(new CourseList(link));
	}

	
	public void remove(CourseList toRemove){
		links.remove(toRemove);
	}
	
	public void remove(String link, CourseList list){
		list.remove(link);
		
		if (list.size() == 1){
			links.remove(list);
		}
	}
	
	public CourseList[] getLinks(String course){
		ArrayList<CourseList> returnVal = new ArrayList<CourseList>();
		
		for(CourseList item: links){
			if (item.contains(course)){
				returnVal.add(item);
			}
		}
		
		return returnVal.toArray(new CourseList[0]);
	}
	
	public Iterator<CourseList> iterator(){
		return links.iterator();
	}
	
	public CourseList[] toArray(){
		CourseList[] item = new CourseList[links.size()];
		
		int pos = 0;
		for(CourseList each: links){
			item[pos++] = each;
		}
		
		return item;
	}
}
