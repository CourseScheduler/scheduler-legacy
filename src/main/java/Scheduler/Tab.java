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
 * File: Tab.java
 * 
 * Contains Interface:
 * 
 * 		Tab:
 * 
 * 			Purpose: To provide a common set of methods for tabs that can me 
 * 						detached, closed or close others.
 * 
 * @author Mike Reinhold
********************************************************/	
package Scheduler;								//declare as member of scheduler package


/*********************************************************
 * The following imports are necessary for this interface
********************************************************/
import javax.swing.JPopupMenu;
import javax.swing.JFrame;


/*********************************************************
 * Interface Tab
 * 
 * 	@purpose to provide a set of methods for all tabs that
 * 		can have contextmenu items of Detach, Close this tab,
 * 		and Close other tabs
********************************************************/
public interface Tab {
	public static final long versionID = 2008111900010L;//object ID
	
	public void setDatabase(Database data, boolean allowPopups, boolean allowBuild);//set the tabs database and specify if from settings
	public Database getDatabase();				//returns the database
	public void setOwner(JFrame owner);			//set the new owner after detaching
	public void setDate();						//set the date based on the current database
	public JPopupMenu getContextMenu();			//returns the context menu for the tab
}
