package Scheduler;

import java.io.Serializable;

public enum CourseType implements Serializable{	
	undergrad ("Undergrad"),
	campusGrad ("On-Campus Grad"),
	distanceGrad ("Off-Campus Grad"),
	underAndCampus ("Undergrad or on-campus grad"),
	underAndDistance ("Undergrad or off-campus grad"),
	campusAndDistance ("On or off capmus grad"),
	all ("any"),
	none ("none");
	
	String name;
	
	CourseType(String name){
		this.name = name;
	}
	
	protected static final long versionID = 2008112000009L;//serial ID
	protected static final long serialVersionUID =1L + 
			Version.courseType.id;//serial ID
	
	public static CourseType getCourseType(boolean isUnderGrad, boolean isGradCamp, boolean isGradDist){
		if(isUnderGrad){
			if(isGradCamp){
				if(isGradDist){
					return all;
				}
				return underAndCampus;
			}
			else if(isGradDist){
				return underAndDistance;
			}
			return undergrad;
		}
		if(isGradCamp){
			if(isGradDist){
				return campusAndDistance;
			}
			return campusGrad;
		}
		if(isGradDist){
			return distanceGrad;
		}
		return none;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
