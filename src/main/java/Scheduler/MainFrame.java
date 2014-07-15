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

/********************************************************
 * 
********************************************************/
package Scheduler;


/********************************************************
 * 
********************************************************/
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;


/********************************************************
 * 
********************************************************/
public class MainFrame extends JFrame {
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008103000019L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.mainFrame.id;						//serial version
	
	
	/********************************************************
	 * The following are private static constants for use in this form
	********************************************************/
	private static final String title = new String("Course Scheduler");
	
	
	/********************************************************
	 * The following are the objects for the main panel
	********************************************************/
	protected Dimension mainFrameDim = new Dimension(800,600);
	protected JPanel mainPanel;
	protected JTabbedPane tabControl;
	protected tabListener tabs;
	protected ArrayList<JFrame> detached;
	
	
	/********************************************************
	 * The following are the objects for the Main menu
	********************************************************/
	protected MainMenu mainMenu;
	
	
	protected mouseListener mouse;
	
	
	/********************************************************
	 * 
	********************************************************/
	public MainFrame(){
		super(title);
		
		super.setIconImage(Main.icon.getImage());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(mainFrameDim);
		this.setLocationRelativeTo(null);
		
		detached = new ArrayList<JFrame>();
		
		mainPanel = new JPanel(new BorderLayout());
		
		mouse = new mouseListener();
		tabs = new tabListener();
		
		mainMenu = new MainMenu();
		mainPanel.add(mainMenu, BorderLayout.NORTH);
		
		tabControl = new JTabbedPane();
		tabControl.addMouseListener(mouse);
		tabControl.addChangeListener(tabs);
		
		
		mainPanel.add(tabControl, BorderLayout.CENTER);
			
		
		
		
		this.add(mainPanel);	
	}	
	
	public void makeButtonTab(int pos){
		tabControl.setTabComponentAt(pos,
            new XTabComponent(tabControl));
	}
	
	public void showContextMenu(Component item, int x, int y){
		if(item.equals(tabControl)){
			int pos = tabControl.indexAtLocation(x,y);
		
			if (pos != -1){
				Tab tab = (Tab)tabControl.getComponentAt(pos);
				JPopupMenu show = tab.getContextMenu();
				
				show.show(item, x, y);
			}
		}
		
		
		
		
		
		
		
		
	}
	
	
	public void setModified(MakeSchedule item, boolean modified){
		int pos = Main.master.tabControl.indexOfComponent(item);
		
		if (pos != -1){
			setModified(false, pos, modified);
		}
		else{
			for(JFrame each: Main.master.detached){
				Popup temp = (Popup)each;
				
				if(temp.getComponent().equals(item)){
					setModified(true, Main.master.detached.indexOf(each), modified);
				}
			}
		}
		
	}
	
	public void setModified(boolean detached, int pos, boolean modified){
		if(!detached){
			String title = Main.master.tabControl.getTitleAt(pos);
			
			boolean isModified = (title.substring(title.length() - 1, title.length()).compareTo("*") == Compare.equal.value());
			
			if(modified && !isModified){
				Main.master.tabControl.setTitleAt(pos, title + "*");
			}
			else if(!modified && isModified){
				Main.master.tabControl.setTitleAt(pos, title.substring(0, title.length() - 2));
			}
			
			((XTabComponent)Main.master.tabControl.getTabComponentAt(pos)).refresh();
		}
		else{
			String title = Main.master.detached.get(pos).getTitle();
			
			boolean isModified = (title.substring(title.length() - 1, title.length()).compareTo("*") == Compare.equal.value());
			
			if(modified && !isModified){
				Main.master.detached.get(pos).setTitle(title + "*");
			}
			else if(!modified && isModified){
				Main.master.setTitle(title.substring(0, title.length() - 2));
			}
		}
	}
	
	
	private class mouseListener implements MouseListener{
		public void mouseClicked(MouseEvent event){}
		public void mouseEntered(MouseEvent event){}
		public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }
		public void mouseExited(MouseEvent event){}
		public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }
		private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            showContextMenu(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	}
	
	private class tabListener implements ChangeListener{
		public void stateChanged(ChangeEvent event){
			try{
				MakeSchedule item = (MakeSchedule)tabControl.getSelectedComponent();
				if (item != null){
					Main.master.mainMenu.saveScheduleMenu.setEnabled(true);
					Main.master.mainMenu.printScheduleMenu.setEnabled(true);
				}
				else{
					throw new ClassCastException("No Item Selected");
				}
			}
			catch(ClassCastException ex){
				Main.master.mainMenu.saveScheduleMenu.setEnabled(false);
				Main.master.mainMenu.printScheduleMenu.setEnabled(false);
			}
		}
	}
}
