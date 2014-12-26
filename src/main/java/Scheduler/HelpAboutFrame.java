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
 * File: HelpAboutFrame.java
 * 
 * Contains class:
 * 
 * 		HelpAboutFrame:
 * 
 * 			Purpose: To display the about information for the
 * 				application
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//define as member of shceduler package


/*********************************************************
 * Import the classes necessary to build and use the GUI
 * 		too many to list their purposes
*********************************************************/
import javax.swing.JEditorPane;
import javax.swing.JFrame;					//this extends JFrame
import javax.swing.JPanel;					//Uses numerous panels
import javax.swing.JLabel;					//uses numerous labels
import javax.swing.JTabbedPane;				//uses a tab pane
import javax.swing.JScrollPane;				//uses a scroll pane
import javax.swing.GroupLayout;				//uses a couple group layouts
import javax.swing.JButton;					//uses a button
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;					//for finding the year
import java.awt.BorderLayout;				//border layout is used
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;					//for defining the frame min size
import java.util.Scanner;					//for parsing the version
import java.awt.event.ActionListener;		//listens for actions
import java.awt.event.ActionEvent;			//the action event
import java.awt.event.KeyListener;			//listens for keyboard events
import java.awt.event.KeyEvent;				//the keyboard event
import java.awt.Font;						//font definitions


/*********************************************************
 * Class HelpAboutFrame
 * 
 * @purpose Displays the Help -> About information
 * 
 * @see JFrame
*********************************************************/
public class HelpAboutFrame extends JFrame {
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(HelpAboutFrame.class);
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2009021100070L;//file version
	protected final static long serialVersionUID = 1L +
			Version.helpAbout.id;			//serial version
	
	
	/********************************************************
	 * The following are the protected visual components of the frame
	********************************************************/
	protected JTabbedPane helpTabMain;		//the tab controller
	protected JScrollPane aboutScroller;	//scroll manager for the file list
	protected JPanel mainPanel;				//the main panel for the center
	protected JPanel filePanel;				//the panel for the file tab
	protected JPanel helpPanel;				//the panel for the frame
	protected GroupLayout group;			//the layout for the file list
	protected JLabel[] fileLabels;			//array of (filname) labels for the file list
	protected JLabel[] modLabels;			//array of (modified date) labels for the file list
	protected JLabel[] buildLabels;			//array of (build number) labels for the file list
	protected GroupLayout.SequentialGroup verSeq1;//file list vertical sequence 1
	protected GroupLayout.SequentialGroup horizSeq1;//file list horizontal sequence 1
	protected GroupLayout.ParallelGroup horizPar1;//file list horizontal parallel 1
	protected GroupLayout.ParallelGroup horizPar2;//file list horizontal parallel 2
	protected GroupLayout.ParallelGroup horizPar3;//file list horizontal parallel 3
	protected GroupLayout.ParallelGroup[] verParGroups;//array of parallel groups for files
	protected JButton ok;					//button for closing the frame
	protected JPanel okPanel;				//panel for the ok button 
	protected JLabel title;					//label for the title
	protected JLabel version;				//label for the version
	protected JLabel build;					//label for the build number
	protected JLabel date;					//label for the last modified date
	protected JLabel author;				//label for the author name
	protected JLabel maintain;				//label for the mainatiner's name
	protected JLabel email;					//label for maintainer's email
	protected JLabel credits;				//pane for the names of contributors
	protected JLabel copyright;				//label for copyright info
	protected JPanel generalPanel;			//panel for the general info
	protected JLabel ratingNotes;			//pane for the rating disclaimer
	protected GroupLayout generalLayout;	//grouplayout for the general tab
	protected JPanel titlePanel;			//the panel to hold the title
	protected int position;					//the scroll position					
	protected int vertInc = Version.values().length/24 * 10;//the vertical increment
	protected Dimension dim = new Dimension(550,450);//the frame dimensions
	protected JLabel jvm;					//label to display the jvm version
	protected JEditorPane policyPanel;
	
	
	/********************************************************
	 * @purpose create the Help -> About windows and prepare
	 * 		the data that it displays
	********************************************************/
	public HelpAboutFrame(){
		super("About Course Scheduler");	//create frame with title
		
		super.setIconImage(Main.icon.getImage());//set the icon
		
		this.setResizable(false);			//set not resizeable
		this.setMinimumSize(dim);			//set minimum size
		this.setLocationRelativeTo(Main.master);//set to center of mainFrame
		
		filePanel = new JPanel();			//make new panel for the file details
		
		group = new GroupLayout(filePanel);	//new group layout for the file details tab
		filePanel.setLayout(group);			//set the layout manager
		
		horizSeq1 = group.createSequentialGroup();//create horizontal sequential group
		horizPar1 = group.createParallelGroup();//create horizontal parallel group
		horizPar2 = group.createParallelGroup();//create horizontal parallel group
		horizPar3 = group.createParallelGroup();//create horizontal parallel group
		
		int size = Version.values().length;	//get number of classes for array size
		fileLabels = new JLabel[size];		//make array for file names
		modLabels = new JLabel[size];		//make array for modified dates
		buildLabels = new JLabel[size];		//make array for build numbers
		
		for(Version item: Version.values()){//for each class
			int ordinal = item.ordinal();	//get the ordinal value
			
			Scanner builder = new Scanner(item.toString());//create scanner on the class as a string
			fileLabels[ordinal] = new JLabel("    File:  " + builder.next() + "        ");//build file name info
			horizPar1.addComponent(fileLabels[ordinal]);//add componenet
			
			modLabels[ordinal] = new JLabel("Modified:  " + builder.next() + "            ");//build modified date info
			horizPar2.addComponent(modLabels[ordinal]);//add component
			
			buildLabels[ordinal] = new JLabel("Build:  " + builder.next() + "            ");//build build number info
			horizPar3.addComponent(buildLabels[ordinal]);//add component
			
			if(ordinal % 2 == 1){
				fileLabels[ordinal].setBackground(Color.white);
				modLabels[ordinal].setBackground(Color.white);
				buildLabels[ordinal].setBackground(Color.white);
				fileLabels[ordinal].setOpaque(true);
				modLabels[ordinal].setOpaque(true);
				buildLabels[ordinal].setOpaque(true);
			}
			
			builder.close();
		}
		
		//horizSeq1.addGap(10);				//pad 10 from the left edge
		horizSeq1.addGroup(horizPar1);		//add the file names
		//horizSeq1.addGap(20);				//pad 20 from the rightmost filename
		horizSeq1.addGroup(horizPar2);		//add the modified dates
		//horizSeq1.addGap(20);				//pad 20 from the rightmost modified date
		horizSeq1.addGroup(horizPar3);		//add the build numbers
		group.setHorizontalGroup(horizSeq1);//set as horizontal group
		
		verParGroups = new GroupLayout.ParallelGroup[size];//create the parallel groups
		
		verSeq1 = group.createSequentialGroup();//create vertical sequential group
		
		for(Version item: Version.values()){//for each class
			int ordinal = item.ordinal();	//get the ordinal
			
			verParGroups[ordinal] = group.createParallelGroup();//create a parallel group
			verParGroups[ordinal].addComponent(fileLabels[ordinal]);//add the file label
			verParGroups[ordinal].addComponent(modLabels[ordinal]);//add the modified label
			verParGroups[ordinal].addComponent(buildLabels[ordinal]);//add the build label
			
			verSeq1.addGap(2);				//add a gap of 2
			verSeq1.addGroup(verParGroups[ordinal]);//add the parallel group to the sequence
		}
		
		group.setVerticalGroup(verSeq1);	//set as vertical group
		
		group.linkSize(modLabels);
		group.linkSize(buildLabels);
		group.linkSize(fileLabels);
		
		mainPanel = new JPanel(new BorderLayout());//create new panel with border layout
		
		title = new JLabel("      Course Scheduler", Main.xlIcon, JLabel.CENTER);//set title text
		title.setFont(new Font("Serif", Font.BOLD, 24));//set font
			
		titlePanel = new JPanel();			//create new panel for title
		titlePanel.add(title);				//add title to the panel
		
		mainPanel.add(titlePanel, BorderLayout.NORTH);//add panel to parent panel in NORTH sector
		
		generalPanel = new JPanel();		//make new panel
		generalLayout = new GroupLayout(generalPanel);//make grouplayout for panel
		generalPanel.setLayout(generalLayout);//set layout manager
		
		Scanner items = new Scanner(Version.version());//create scanner on the version
		
		version = new JLabel(" Release Version: " + items.next());//get release version text
		build = new JLabel(" Build Number: " + items.next());//get build number text
		date = new JLabel(" Last Modified: " + Version.date());//get last modified date
		author = new JLabel("Author: " + Main.author);//get author name
		email = new JLabel("Email: " + Main.email);//get maintainer email
		maintain = new JLabel("<html>Maintainer: " + Main.maintain + "</html>");//get maintainer name
		ratingNotes = new JLabel();		//create pane for the rating comment
		ratingNotes.setText("<html>Professor ratings are courtesy " +
				"of ratemyprofessors.com. The ratings are " + 
				"not the view of the author or maintainers.</html>");//built rating comment
		ratingNotes.setBackground(author.getBackground());//set background color
		ratingNotes.setFont(author.getFont());//set font
		
		items.close();
		
		credits = new JLabel();			//make pane for credits
		credits.setText("Contributers: " + Main.contributers);//set contributers text
		credits.setBackground(mainPanel.getBackground());//set background color
		credits.setFont(author.getFont());	//set font
		
		Calendar calendar = Calendar.getInstance();	//make new date for today
		copyright = new JLabel(" Copyright " +//build copyright string 
			"\u00a9" + " " + (calendar.get(Calendar.YEAR)));//with this year
		jvm = new JLabel(Main.jvm);
		
		//horizontal layout definition
		generalLayout.setHorizontalGroup(generalLayout.createParallelGroup()//set horizontal group to new parallel group
			.addGroup(generalLayout.createSequentialGroup()//add a sequential group to the parallel group
				.addGap(10)					//add a gap of 10 to the sequence
				.addGroup(generalLayout.createParallelGroup()//add a parallel group to the sequence
					.addComponent(version)	//add the version, build, and date to the parallel group
					.addComponent(build)
					.addComponent(date)
					.addComponent(jvm)
				)
				.addGap(60)					//add a gap of 60 to the sequence
				.addGroup(generalLayout.createParallelGroup()//add a parallel group to the sequence
					.addComponent(author)	//add the author, email, and maintainers to the parallel group
					.addComponent(email)
					.addComponent(maintain)
				)
			)
			.addGroup(generalLayout.createSequentialGroup()//add another sequential group to the parallel group
				.addGap(10)					//add a gap of 10 to the sequence
				.addGroup(generalLayout.createParallelGroup()//add a parallel group to the sequence
					.addComponent(credits)	//add credits, rating notes, and copyright info to the 
					.addComponent(ratingNotes)//parallel group
					.addComponent(copyright)
				)
			)
		);
		
		//vertical layout definition
		generalLayout.setVerticalGroup(generalLayout.createSequentialGroup()//set vertical group to new sequential group
			.addGroup(generalLayout.createParallelGroup()//add a parallel group to the sequence
				.addComponent(version)		//add version and author to the parallel group
				.addComponent(author)
			)
			.addGroup(generalLayout.createParallelGroup()//add another parallel group to the sequence
				.addComponent(build)		//add build and maintainers to the parallel group
				.addComponent(maintain)
			)
			.addGroup(generalLayout.createParallelGroup()//add another parallel group to the sequence
				.addComponent(date)			//add date and email to the parallel group
				.addComponent(email)
			)
			.addComponent(jvm)
			.addGap(30)						//add a gap of 30 to the sequence
			.addGroup(generalLayout.createSequentialGroup()//add a sequential group to the sequence
				.addComponent(copyright)	//add the copyright to the inner sequence
				.addGap(5)					//add a gap of 5 to the inner sequence
				.addComponent(credits)		//add the credits to the inner sequence
				.addGap(5)
				.addComponent(ratingNotes)	//add the rating comments to the inner sequence
			)
		);
				
		mainPanel.add(generalPanel, BorderLayout.CENTER);//add the general panel to the main panel center region
				
		aboutScroller = new JScrollPane(filePanel);//add the filePanel to the scroll pane
		aboutScroller.setAutoscrolls(true);	//allow auto scrolling
		aboutScroller.setWheelScrollingEnabled(true);//allow mouse wheel scrolling
		aboutScroller.getVerticalScrollBar().setUnitIncrement(vertInc);//set the vertical increment
		
		policyPanel = new JEditorPane();
		policyPanel.setEditable(false);
		URL policy = null;
		
		try{
			policy = Main.loader.getResource("Privacy.html");
		}catch(Exception e){
			logger.warn("Unable to load privacy policy", e);
		}
		try {
			policyPanel.setContentType("text/html");
			policyPanel.setPage(policy);
		} catch (IOException e) {
			logger.warn("Unable to display privacy policy", e);
		}		
		
		policyPanel.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    logger.debug("Hyperlink activated {}", hle.getURL());
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        logger.error("Unable toopen hyperlink in external application", ex);
                    }
                }
            }
        });
		
		JScrollPane privacyPanel = new JScrollPane(policyPanel);
		
		helpTabMain = new JTabbedPane();	//create the tab control structure
		helpTabMain.addTab("General", mainPanel);//add the main panel as the General tab
		helpTabMain.addTab("Classes", aboutScroller);//add the scroll pane (on the filePanel) as the Classes tab
		helpTabMain.addTab("Privacy", privacyPanel);
		
		helpTabMain.setMnemonicAt(0, KeyEvent.VK_G);//set the mnemonic for the general tab
		helpTabMain.setMnemonicAt(1, KeyEvent.VK_C);//set the menemonic for the classes tab
		
		helpTabMain.addKeyListener(new scrollListener());//add a key listener for the tab control to scroll the scroll pane
		
		ok = new JButton("OK");				//create the ok button to close the form
		ok.addActionListener(new okHelpListener());//add the listener
		ok.setMaximumSize(new Dimension(50,20));//set max size of the button
		ok.setMnemonic('O');				//set mnemonic for the button
		ok.addKeyListener(new enterListener());//add listener for the enter key
		
		okPanel = new JPanel();				//create panel for the ok button to preserve size and location
		okPanel.add(ok);					//add the button to the panel
		
		helpPanel = new JPanel();			//make new panel for the whole frame
		
		helpPanel.setLayout(new BorderLayout());//set layout to new border layout
		
		helpPanel.add(helpTabMain, BorderLayout.CENTER);//add the tab control structure to the center region
		helpPanel.add(okPanel, BorderLayout.SOUTH);//add the ok panel to the south region
		
		this.add(helpPanel);				//add the help panel to the frame
	}
	
	
	/********************************************************
	 * Class okHelpListener
	 * 
	 * @purpose closes the frame when the action is seen
	 * 
	 * @see ActionListener
	********************************************************/
	private class okHelpListener implements ActionListener{
		
		
		/********************************************************
		 * @purpose closes the frame when the ok button sources an event
		 * 
		 * @see ActionEvent, ActionListener
		********************************************************/
		public void actionPerformed(ActionEvent event){
			Main.master.mainMenu.aboutHelpFrame.setVisible(false);//make frame invisible
			HelpAboutFrame.this.helpTabMain.setSelectedIndex(0);	//reset the tab index
		}
	}
	
	
	/********************************************************
	 * Class enterListener
	 * 
	 * @purpose listens for the enter key to be pressed and 
	 * 		programmatically "clicks" the ok button to close the frame
	 * 
	 * @see KeyListener
	********************************************************/
	private class enterListener implements KeyListener{
		
		
		/********************************************************
		 * @purpose unused but required to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyTyped(KeyEvent event){}		//not used
		
		
		/********************************************************
		 * @purpose programatically "clicks" the ok button when
		 * 		the enter key is pressed, any other key does nothing
		 * 
		 * @see KeyListener, KeyEvent
		********************************************************/
		public void keyPressed(KeyEvent event){
			if (event.getKeyCode() == KeyEvent.VK_ENTER){//check if enter key
				Main.master.mainMenu.aboutHelpFrame.ok.doClick();//do ok button click
			}
		}
		
		
		/********************************************************
		 * @purpose unused but required to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyReleased(KeyEvent event){}	//not used
	}
	
	
	/********************************************************
	 * Class scrollListener
	 * 
	 * @purpose Allows the use of arrow keys to scroll the scroll pane
	 * 
	 * @see KeyListener
	********************************************************/
	private class scrollListener implements KeyListener{
		
		
		/********************************************************
		 * @purpose unused but required to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyTyped(KeyEvent event){}		//not used
		
		
		/********************************************************
		 * @purpose checks which key is pressed and appropriately
		 * 		scrolls the scroll pane
		 * 
		 * @see KeyListener, KeyEvent
		********************************************************/
		public void keyPressed(KeyEvent event){
			HelpAboutFrame current = Main.master.mainMenu.aboutHelpFrame;//get help frame
			current.position = current.aboutScroller.getVerticalScrollBar().getValue();//get current position
			
			if (event.getKeyCode() == KeyEvent.VK_UP){//check if up arrow key
				current.position -= Math.max(current.vertInc, //calculate new position as the max of current - increment
					current.aboutScroller.getVerticalScrollBar().getMinimum());//and the minimum value on the scroll bar
				
				current.aboutScroller.getVerticalScrollBar().setValue(current.position);//set new position
			}
			else if(event.getKeyCode() == KeyEvent.VK_DOWN){//check if down arrow key
				current.position += Math.min(current.vertInc, //calculate new position as the min of current + increment
					current.aboutScroller.getVerticalScrollBar().getMaximum());//and the maximum valueon the scroll bar
				
				current.aboutScroller.getVerticalScrollBar().setValue(current.position);//set the new position
			}
		}
		
		
		/********************************************************
		 * @purpose unused but required to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyReleased(KeyEvent event){}	//not used
	}
}
