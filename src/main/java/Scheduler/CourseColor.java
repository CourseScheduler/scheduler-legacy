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
 * File: BuildScheduleThread.java
 * 
 * Contains class:
 * 
 * 		CourseColor:
 * 
 * 			Purpose: To store course color information for
 * 				rendering the dynamic schedule
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//define as member of scheduler package


/*********************************************************
 * The following imports are necessary for the classes functionality
*********************************************************/
import java.io.Serializable;
import java.awt.Color;


/*********************************************************
 * Class CourseColor
 * 
 * @purpose Stores course color rendering information
 * 
 * @see Serializable
*********************************************************/
public class CourseColor implements Serializable{
	
	
	/*********************************************************
	 * The following are the static constants for versioning
	*********************************************************/
	protected static final long versionID =  2008052300016L;//object version
	protected static final long serialVersionUID = 10L 
		+ Version.courseColor.id;				//serial ID
	
	
	/*********************************************************
	 * The following are the color fields for storing render colors
	*********************************************************/
	protected Color ac;							//accounting
	protected Color bi;							//biology
	protected Color bu;							//business
	protected Color ce;							//computer engineering
	protected Color ch;							//chemistry
	protected Color co;							//communications
	protected Color cs;							//computer science
	protected Color ec;							//economics
	protected Color ee;							//electrical engineering
	protected Color fi;							//finace
	protected Color hi;							//history
	protected Color hu;							//humanities
	protected Color im;							//inter disciplinary design and manufacturing
	protected Color is;							//information systems
	protected Color li;							//literature
	protected Color ls;							//leadership seminar
	protected Color ma;							//math
	protected Color me;							//mechanichal engineering
	protected Color mg;							//management
	protected Color mr;							//marketing
	protected Color or;							//orientation
	protected Color pi;							//philosophy
	protected Color py;							//physics
	protected Color so;							//sociology
	protected Color ss;							//social science
	
	public Color oth;							//other courses
	
	
	/*********************************************************
	 * The default color scheme and general colors
	*********************************************************/
	final static Color mathScience = new Color(255, 204, 153, 255);		//default MATH/ science color
	final static Color computerScience = new Color(178, 128, 102, 255);	//default CS ISYS Color
	final static Color generalElec = new Color(204, 180, 255, 255);		//default GEN ED color
	final static Color ecEngineering = new Color(153, 180, 255, 255);	//default ECE color
	final static Color otherEngineering = new Color(102, 204, 102, 255);//default Other Engineering
	final static Color other = new Color(255, 204, 255, 255);			//default other color
	
	
	/*********************************************************
	 * (Constructor) 
	 * 
	 * @purpose constructs a CourseColor object with the default color scheme
	*********************************************************/
	public CourseColor(){
		ac = generalElec;						//sets course colors based on if they 
		bi = mathScience;						//are "general elective", "math or science",
		bu = generalElec;						//"electrical or computer engineering",
		ce = ecEngineering;						//"other engineering" or "computer science"
		ch = mathScience;						//this is basically my default color scheme
		co = generalElec;		
		cs = computerScience;
		ec = generalElec;
		ee = ecEngineering;
		fi = generalElec;
		hi = generalElec;
		hu = generalElec;
		im = otherEngineering;
		is = computerScience;
		li = generalElec;
		ls = generalElec;
		ma = mathScience;
		me = otherEngineering;
		mg = generalElec;
		mr = generalElec;
		or = generalElec;
		pi = generalElec;
		py = mathScience;
		so = generalElec;
		ss = generalElec;
		
		oth = other;
	}
	
	
	/*********************************************************
	 * @purpose sets the color for the identified string
	 * 
	 * @param String identifier: the course type to set the color
	 * @param Color color: the color to set the render to use
	*********************************************************/
	public void setColor(String identifier, Color color){
		CourseType type = CourseType.getType(identifier);//get the course type
		
		if (type == CourseType.ac){			//check if the course type is correct
			ac = color;						//then set the color
		}
		else if (type == CourseType.bi){
			bi = color;
		}
		else if (type == CourseType.bu){	//ditto above
			bu = color;
		}
		else if (type == CourseType.ce){
			ce = color;
		}
		else if (type == CourseType.ch){	//ditto above
			ch = color;
		}
		else if (type == CourseType.co){
			co = color;
		}
		else if (type == CourseType.cs){	//ditto above
			cs = color;
		}
		else if (type == CourseType.ec){
			ec = color;
		}
		else if (type == CourseType.ee){	//ditto above
			ee = color;	
		}
		else if (type == CourseType.fi){
			fi = color;
		}
		else if (type == CourseType.hi){	//ditto above
			hi = color;
		}
		else if (type == CourseType.hu){
			hu = color;
		}
		else if (type == CourseType.im){	//ditto above
			im = color;
		}
		else if (type == CourseType.is){
			is = color;
		}
		else if (type == CourseType.li){	//ditto above
			li = color;
		}
		else if (type == CourseType.ls){
			ls = color;
		}
		else if (type == CourseType.ma){	//ditto above
			ma = color;
		}
		else if (type == CourseType.me){
			me = color;
		}
		else if (type == CourseType.mg){	//ditto above
			mg = color;
		}
		else if (type == CourseType.mr){
			mr = color;
		}
		else if (type == CourseType.or){	//ditto above
			or = color;
		}
		else if (type == CourseType.pi){
			pi = color;
		}
		else if (type == CourseType.py){	//ditto above
			py = color;
		}
		else if (type == CourseType.so){
			so = color;
		}
		else if (type == CourseType.ss){	//ditto above
			ss = color;
		}
		else if (type == CourseType.oth){
			oth = color;
		}
	}
	
	
	/*********************************************************
	 * @purpose Returns the render color for the specified course
	 * 		identifier
	 * 
	 * @param String identifier: the course identifier to sue
	 * 
	 * @return Color: the render color for that course type
	********************************************************/
	public static Color getColor(String identifier){
		return CourseType.getType(identifier).getColor();
	}
	
	
	
	/*********************************************************
	 * enum CourseType
	 * 
	 * @purpose Enumerates the possible course identifiers  for color
	 * 		co-ordination
	********************************************************/
	private enum CourseType {
		
		/*********************************************************
		 * Enumeration of the course types and the specified color 
		********************************************************/
		ac ("ACCT", Main.prefs.getColors().ac),			//accounting
		bi ("BIOL", Main.prefs.getColors().bi),			//biology
		bu ("BUSN", Main.prefs.getColors().bu),			//business
		ce ("CE", Main.prefs.getColors().ce),			//computer engineering
		ch ("CHEM", Main.prefs.getColors().ch),			//chemistry
		co ("COMM", Main.prefs.getColors().co),			//communications
		cs ("CS", Main.prefs.getColors().cs),			//computer science
		ec ("ECON", Main.prefs.getColors().ec),			//economics
		ee ("EE", Main.prefs.getColors().ee),			//electrical engineering
		fi ("FINC", Main.prefs.getColors().fi),			//finance
		hi ("HIST", Main.prefs.getColors().hi),			//history
		hu ("HUMN", Main.prefs.getColors().hu),			//humanities
		im ("IME", Main.prefs.getColors().im),			//inter disciplinary design and manufacturing
		is ("ISYS", Main.prefs.getColors().is),			//information systems
		li ("LIT", Main.prefs.getColors().li),			//literature
		ls ("LS", Main.prefs.getColors().ls),			//leadership seminar
		ma ("MATH", Main.prefs.getColors().ma),			//math
		me ("MECH", Main.prefs.getColors().me),			//mechanical engineering
		mg ("MGMT", Main.prefs.getColors().mg),			//management
		mr ("MRKT", Main.prefs.getColors().mr),			//marketing
		or ("ORTN", Main.prefs.getColors().or),			//orientation
		pi ("PHIL", Main.prefs.getColors().pi),			//philosophy
		py ("PHYS", Main.prefs.getColors().py),			//physics
		so ("SOC", Main.prefs.getColors().so),			//sociology
		ss ("SSCI", Main.prefs.getColors().ss),			//social science
		
		oth ("OTH", Main.prefs.getColors().oth)			//other courses
		;
				
		
		/*********************************************************
		 * The following are the fields of the enumerator
		********************************************************/
		private final String prefix;				//to store course prefix
		private Color color;						//to store the color
		
		
		/*********************************************************
		 * (Constructor)
		 * 
		 * @param String prefix: the Course identifier perfix
		 * @param Color color: the color to associate with the prefix
		********************************************************/
		CourseType(String prefix, Color color){
			this.prefix = prefix;					//set the color and prefix
			this.color = color;
		}
		
		
		/*********************************************************
		 * @purpose Return the prefix for this course type
		 * 
		 * @return String: the prefix for this course type
		********************************************************/
		public String prefix(){
			return this.prefix;						//return the prefix
		}
		
		
		/*********************************************************
		 * @purpose Return the color associated with this course type
		 * 
		 * @return Color: the color for this course type
		********************************************************/
		public Color getColor(){	
			return this.color;						//return the color
		}
		
		
		/*********************************************************
		 * get the course type for the identifier
		********************************************************/
		public static CourseType getType(String identifier){
			for(CourseType item: CourseType.values()){
				if(identifier.compareTo(item.prefix()) == Compare.equal.value()){
					return item;					//return coures type that matches the identifier
				}
			}
			return oth;								//return other course type
		}
	}
}

