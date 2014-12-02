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
 * File: OptionsFrame.java
 * 
 * Contains class:
 * 
 * 		OptionsFrame:
 * 
 * 			Purpose: To provide an interface for specifying the
 * 				desired application options
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;								//define as member of scheduler package


/*********************************************************
 * The following are the necessary imports for correct function
********************************************************/
import io.devyse.scheduler.analytics.keen.KeenUtils;

import java.awt.BorderLayout;					//for main frame
import java.awt.Dimension;						//for frame min dimensions
import java.awt.FlowLayout;						//for flow layout creation
import java.awt.event.ActionListener;			//for writing action listeners
import java.awt.event.ActionEvent;				//for use in the action listener
import java.awt.event.WindowEvent;				//for the window events
import java.awt.event.FocusListener;			//for listening for focus events
import java.awt.event.FocusEvent;				//for detecting focus changes

import javax.swing.BorderFactory;				//for creating frame and panel borders
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;						//extended by this class
import javax.swing.JPanel;						//used to separate gui components
import javax.swing.JLabel;						//used to label other components
import javax.swing.JCheckBox;					//used extensively
import javax.swing.JTabbedPane;					//for separating options logically
import javax.swing.JTextField;					//for obtaining user input
import javax.swing.GroupLayout;					//used to arrange the layout
import javax.swing.ButtonGroup;					//used to define the radio buttons group
import javax.swing.JRadioButton;				//used for specific options
import javax.swing.JButton;						//used for the apply and cancel buttons
import javax.swing.JOptionPane;					//used for pop-up error messages
import javax.swing.text.NumberFormatter;		//used for number filtering

import java.text.DecimalFormat;					//used for number filtering
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;						//used to parse strings
import java.io.File;							//used for file manipulation
import java.io.FilenameFilter;					//used for finding the files	


/*********************************************************
 * Class OptionsFrame
 * 
 * @purpose Provides a GUI for setting the application
 * 		preferences
 * 
 * @see JFrame
********************************************************/
public class OptionsFrame extends JFrame {

	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010900031L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.options.id;				//serial version
	
	
	/*********************************************************
	 * The following are protected fields for the frame 
	********************************************************/
	protected Dimension dim = new Dimension(475,368);//the frame dimensions
	protected enableListener chkBox = new enableListener();//listener for checkbox enables
	protected buttonListener button = new buttonListener();//listener for button clicks
	protected textFocusListener focus = new textFocusListener();//listener for focus events on text fields
	protected JTabbedPane mainTabs;					//tab control for the different option tabs
	
	
	/*********************************************************
	 * The following are protected fields for the main portion of the frame
	********************************************************/
	protected JPanel settingsMain;			//main settings Panel
	protected JPanel settingsRatings;		//ratings settings Panel
	protected GroupLayout generalTabLayout;	//main group layout
	protected GroupLayout ratingsTabLayout;	//group layout for ratings
	protected JCheckBox enableRatings;		//to enable ratings
	protected JCheckBox enableUGrad;		//to enable undergraduate courses
	protected JCheckBox overrideURL;		//to enable URL override
	protected JCheckBox overrideSID;		//to enable SID ovverride
	protected JTextField newURL;			//enter the new url
	protected JTextField newSID;			//enter the new SID
	protected JCheckBox enableCampusGrad;   //to enable on campus grad courses
	protected JCheckBox enableDistGrad;		//to enable distance grad courses
	protected JCheckBox enableOverrideGrad; //to enable URL override
	protected JCheckBox enableOverrideDist;	//to enable URL override
	protected JTextField newGradURL;		//to input new URL
	protected JTextField newDistURL;		//to input new URL
	protected JTextField newCampus;			//to input new on campus course download url
	protected JTextField newDist;			//to input new distance course download url
	protected JCheckBox analyticsOptOut;	//to option out of the analytics reporting
	
	
	/*********************************************************
	 * The following are the protected fields for the button portion
	********************************************************/
	protected JButton apply;				//to apply the settings
	protected JButton cancel;				//to cancel the settings
	protected JButton clear;				//to clear the downloaded data
	protected JPanel buttonPanel;			//panel for the buttons
	
	
	/*********************************************************
	 * The following are the protected fields for the ratings panel
	********************************************************/
	protected JPanel ratings;				//for the ratings
	protected JCheckBox enableRMPRatings;	//for rate my professor ratings
	protected JFormattedTextField maxGap;	//for the max gap between classes
	protected JLabel maxGapLbl;				//to label to the max gap
	protected JFormattedTextField minGap;	//for the min gap between classes
	protected JLabel minGapLbl;				//to label the min gap
	protected JCheckBox dayOff;				//to enable a day off
	protected JLabel startTime;				//labels the start time field
	protected JFormattedTextField start;	//the start time field
	protected JLabel endTime;				//labels the end time field
	protected JFormattedTextField end;		//the end time field
	protected GroupLayout ratingsLayout;	//the layout for the ratings panel
	
	
	/*********************************************************
	 * The following are the protected fields for the days panel
	********************************************************/
	protected JPanel daysOff;				//to frame to hold the days off
	protected JRadioButton monday;			//to specify the day off
	protected JRadioButton tuesday;			//to specify the day off
	protected JRadioButton wednesday;		//to specify the day off
	protected JRadioButton thursday;		//to specify the day off
	protected JRadioButton friday;			//to specify the day off
	protected ButtonGroup daysGroup;		//to force single day off
	protected GroupLayout daysLayout;		//the layout for the days off panel
	
	
	/*********************************************************
	 * THe following are protected fields within the option pane
	********************************************************/
	protected boolean isHidden;				//a boolean for deciding when to update the components
	
	
	/*********************************************************
	 * The following are private constants for the layout
	********************************************************/
	private static int marginSpace = 20;	//the margin space between the edge of the frame and a control
	private static int horizSpace = 5;		//the horizontal space between controls
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates and sets up a new options frame
	********************************************************/
	public OptionsFrame(){
		super("Settings");						//create a frame with the title Settings
		
		super.setIconImage(Main.icon.getImage());//set the icon
		setLayout(new BorderLayout());			//set the main frame layout
		setMinimumSize(dim);					//set the minimum size
		setLocationRelativeTo(Main.master);		//set to display in the middle of the master 
		setResizable(false);					//do not allow resizing of the fram
		
		settingsMain = new JPanel();			//create new panel for whole frame
		generalTabLayout = new GroupLayout(settingsMain);//create grouplayout to manage the frame
		settingsMain.setLayout(generalTabLayout);//set the layout manager
		
		settingsRatings = new JPanel();			//create the panel
		ratingsTabLayout = new GroupLayout(settingsRatings);//create the layout
		settingsRatings.setLayout(ratingsTabLayout);//set the layout
		
		//no longer relevant
		enableUGrad = new JCheckBox("Enable Undergraduate Courses");//create the checkbox
		enableUGrad.addActionListener(chkBox);	//add the listener
		enableUGrad.setMnemonic('U');			//set the mnemonic
		enableUGrad.setToolTipText("Enable downloading of undergraduate courses.");
		enableUGrad.setVisible(false);
		
		enableRatings = new JCheckBox("Enable Ratings");//instantiate the checkbox
		enableRatings.addActionListener(chkBox);//add this frame's check box listener
		enableRatings.setMnemonic('E');			//set the mnemonic 
		enableRatings.setToolTipText("<html>Set if the courses " + 
			"and schedules are rated by the below settings.<br>" + 
			"These settings only alter the order in which schedules" + 
			" are dislayed.</html>");			//set the tool tip text
		
		enableCampusGrad = new JCheckBox("Enable On-Campus Grad Courses");//create the checkbox
		enableCampusGrad.addActionListener(chkBox);//add listener
		enableCampusGrad.setMnemonic('m');		//set mnemonic
		enableCampusGrad.setToolTipText("Enable downloading of on-campus graduate courses.");
		enableCampusGrad.setVisible(false);
		
		enableOverrideGrad = new JCheckBox("Enable On-Campus Download URL Override");//create checkbox
		enableOverrideGrad.addActionListener(chkBox);//add listener
		enableOverrideGrad.setMnemonic('n');	//set mnemonic
		enableOverrideGrad.setToolTipText("Override the default download URL for on-campus courses.");
		enableOverrideGrad.setVisible(false);
		
		enableDistGrad = new JCheckBox("Enable Distance Grad Courses");//create the checkbox
		enableDistGrad.addActionListener(chkBox);//add listener
		enableDistGrad.setMnemonic('D');		//set mnemonic
		enableDistGrad.setToolTipText("Enable downloading of distance learning graduate courses.");
		enableDistGrad.setVisible(false);
		
		enableOverrideDist = new JCheckBox("Enable Distance Download URL Override");//create checkbox
		enableOverrideDist.addActionListener(chkBox);//add listener
		enableOverrideDist.setMnemonic('i');	//set mnemonic
		enableOverrideDist.setToolTipText("Override the default download URL for distance learning courses.");
		enableOverrideDist.setVisible(false);
		
		overrideURL = new JCheckBox("Enable Course Download URL Override");//create new checkbox
		overrideURL.addActionListener(chkBox);	//add this frames checkbox listener
		overrideURL.setMnemonic('o');			//set the mnemonic for the checkbox
		overrideURL.setToolTipText("Set if the default URL for downloading course " +
			"information should be overridden with the specified URL.");
												//set the tooltip
		overrideSID = new JCheckBox("Enable Rate My Professer School ID Override");//instantiate
		overrideSID.addActionListener(chkBox);	//add this frame's checkbox listener
		overrideSID.setMnemonic('R');			//set the mnemonic for the checkbox
		overrideSID.setToolTipText("Set if the default School ID should be overridden" +
			" with the specified School ID.");	//set the tool tip
		
		analyticsOptOut = new JCheckBox("Opt out of anonymous data collection");
		analyticsOptOut.addActionListener(chkBox);
		analyticsOptOut.setMnemonic('a');
		analyticsOptOut.setToolTipText("Do not perform anonymous data collection");
		
		newURL = new JTextField(20);			//create the text field with min size
		newSID = new JTextField(20);			//create the text field with min size
		newCampus = new JTextField(20);			//create text field with min size
		newDist = new JTextField(20);			//create text field with min size
		newURL.addFocusListener(focus);			//add a focus listener for focus modifications
		newSID.addFocusListener(focus);			//add a focus listener for focus modifications
		newCampus.addFocusListener(focus);		//add focus listener
		newDist.addFocusListener(focus);		//add focus listener
		newURL.setToolTipText("The URL to download course information from.");//add tool tips
		newSID.setToolTipText("The School ID used by Rate My Professor.");//add tool tips
		newCampus.setToolTipText("The URL to download on-campus graduate courses from.");
		newDist.setToolTipText("The URL to download distance learning graduate courses from.");
		
		buildRatingsPanel();					//build the ratings panel
		
		apply = new JButton("Apply");			//make the apply button
		apply.addActionListener(button);		//add this frame's generic button listener
		apply.setMnemonic('A');					//set the mnemonic
		apply.setToolTipText("Apply the current settings and close the window.");//set tool tip
		
		cancel = new JButton("Cancel");			//make the cancel button
		cancel.addActionListener(button);		//add this frame's generic button listener
		cancel.setMnemonic('C');				//set the mnemonic
		cancel.setToolTipText("Cancel the current changes to the settings and close the window.");//set tool tip
		
		clear = new JButton("Clear Data");		//make the clear button
		clear.addActionListener(button);		//add this frame's generic button listener
		clear.setMnemonic('l');					//set the mnemonic
		clear.setToolTipText("Clear the application's data cache");//set tool tip
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));//new panel with flow layout and spaces
		buttonPanel.add(apply);					//add the apply button first to the panel
		buttonPanel.add(cancel);				//add the cancel button second
		buttonPanel.add(clear);					//add the clear button third
		
		generalTabLayout.setHorizontalGroup(generalTabLayout.createParallelGroup()//make the sequential groups defined below all horiz parallel
			//.addGroup(generalTabLayout.createSequentialGroup()//create sequential group
			//	.addGap(2 * horizSpace)			//add gap in front of component
			//	.addComponent(enableUGrad)		//add the enable checkbox
			//)
			.addGroup(generalTabLayout.createSequentialGroup()//first sequential group parallel to the others
				.addGap(2 * horizSpace)			//add twice the horiz space
				.addComponent(overrideURL)		//add the override URL check box
				.addGap(horizSpace)				//add the horizontal space
				.addComponent(newURL)			//add the url input field
				.addGap(2 * horizSpace)			//add space to ensure a gap on the irhgt
			)
			.addGroup(generalTabLayout.createSequentialGroup()//second sequential group parallel to the others
				.addGap(2 * horizSpace)			//add twice the horiz space
				.addComponent(overrideSID)		//add the override sid check box
				.addGap(horizSpace)				//add the horiz space
				.addComponent(newSID)			//add the sid input field
				.addGap(2 * horizSpace)			//add space to ensure gap on the right
			)
			.addGroup(generalTabLayout.createSequentialGroup()
				.addGap(2 * horizSpace)
				.addComponent(analyticsOptOut)
				.addGap(2*horizSpace)
			)
			//.addGroup(generalTabLayout.createSequentialGroup()//create sequential group
			//	.addGap(2 * horizSpace)			//add gap in front of component
			//	.addComponent(enableCampusGrad)	//add enable checkbox
			//)
			//.addGroup(generalTabLayout.createSequentialGroup()//create sequential group
			//	.addGap(2 * horizSpace)			//add gap in front of component
			//	.addComponent(enableOverrideGrad)//add override checkbox
			//	.addGap(horizSpace)				//add space between components
			//	.addComponent(newCampus)		//add text field
			//	.addGap(2 * horizSpace)			//add space
			//)
			//.addGroup(generalTabLayout.createSequentialGroup()//create sequential group
			//	.addGap(2 * horizSpace)			//add space before component
			//	.addComponent(enableDistGrad)	//add enable checkbox
			//)
			//.addGroup(generalTabLayout.createSequentialGroup()//create sequential group
			//	.addGap(2 * horizSpace)			//add space before component
			//	.addComponent(enableOverrideDist)//add override checkbox
			//	.addGap(horizSpace)				//add space between components
			//	.addComponent(newDist)			//add text field 
			//	.addGap(2 * horizSpace)			//add space after
			//)
		);
		
		generalTabLayout.setVerticalGroup(generalTabLayout.createSequentialGroup()//create vertical sequence of items 
			.addGap(2 * horizSpace)				//add gap before new row
			.addGroup(generalTabLayout.createParallelGroup()//create new row of items
				.addComponent(overrideSID)//add the override sid check box
				.addComponent(newSID)//add the sid input field
			)
			.addGap(2 * horizSpace)				//add vertical space for margin
			//.addComponent(enableUGrad)			//add enable checkbox
			.addGap(2 * horizSpace)				//add space between components
			.addGroup(generalTabLayout.createParallelGroup()//add a row of items
				.addComponent(overrideURL)//add the override url check box
				.addComponent(newURL)//add the url input field
			)
			.addGap(2 * horizSpace)
			.addComponent(analyticsOptOut)
			//.addGap(2 * horizSpace)				//add gap
			.addGap(36 * horizSpace)				//add space
			//.addComponent(enableCampusGrad)		//add enable checkbox
			//.addGap(2 * horizSpace)				//add gap
			//.addGroup(generalTabLayout.createParallelGroup()//add parallel group
			//	.addComponent(enableOverrideGrad)//add override checkbox
			//	.addComponent(newCampus)//add text field
			//)
			//.addGap(2 * horizSpace)				//add space
			//.addComponent(enableDistGrad)		//add enable checkbox
			//.addGap(2 * horizSpace)				//add space
			//.addGroup(generalTabLayout.createParallelGroup()//add parallel group
			//	.addComponent(enableOverrideDist)//add override checkbox
			//	.addComponent(newDist)//add text field
			//)
			//.addGap((int)(1.5 * horizSpace))	//add a gap of floor(1.5 * horizSpace) before next row
		);
		
		ratingsTabLayout.setHorizontalGroup(ratingsTabLayout.createParallelGroup()
			.addGroup(ratingsTabLayout.createSequentialGroup()//third sequential group parallel to the others
				.addGap(2 * horizSpace)			//add twice the horiz space
				.addComponent(enableRatings)	//add the ratings enable check box
			)
			.addGroup(ratingsTabLayout.createSequentialGroup()//fourth sequential group parallel to the others
				.addComponent(ratings)			//add the ratings panel
			)
		);
		
		ratingsTabLayout.setVerticalGroup(ratingsTabLayout.createSequentialGroup()
			.addGap(2 * horizSpace)				//add gap before next sequential item
			.addComponent(enableRatings)		//add the enable ratings check box, own row
			.addGap(2 * horizSpace)				//add gap before ratings panel
			.addComponent(ratings)				//add the ratings panel, own row
			.addGap((int)(1.5 * horizSpace))	//add a gap of floor(1.5 * horizSpace) before next row
		);
		
		mainTabs = new JTabbedPane();			//create tabbed pane
		mainTabs.addTab("General", settingsMain);//add general tab
		mainTabs.addTab("Ratings", settingsRatings);//add ratings tab
		
		add(mainTabs, BorderLayout.CENTER);		//put the settings main panel in the frame
		add(buttonPanel, BorderLayout.SOUTH);	//put the buttons in the pane
		
		updateSettingsFrame();					//update the frame's fields
		
		isHidden = true;						//set that the frame is hidden
		
		enableEvents(WindowEvent.WINDOW_ACTIVATED);//enable window activated event to manage directly
		this.pack();
	}
	
	
	/*********************************************************
	 * @purpose To provide a way to update the frame when necessary
	 * 		by adding onto the super's processWindowEvent method by
	 * 		doing what we need to do and then calling the super's 
	 * 		processWindowEvent method
	 * 
	 * @param WindowEvent event: the triggered window event
	********************************************************/
	@Override
	protected void processWindowEvent(WindowEvent event){
		if(event.getID() == WindowEvent.WINDOW_ACTIVATED && isHidden){//check if the frame should update contents
			updateSettingsFrame();				//only update when the frame was hidden and now is not
			isHidden = false;					//update frame then set to not hidden
		}
		super.processWindowEvent(event);		//allow super to process event
	}
	
	
	/*********************************************************
	 * @purpose To update the frames component values based
	 * 		on Main.prefs
	********************************************************/
	public void updateSettingsFrame(){
		Preferences prefs = Main.prefs;			//get main's prefs
		
		enableUGrad.setSelected(prefs.isDownloadUGrad());//set if under grad is enabled
		overrideURL.setSelected(prefs.isOverRideURL());//set the checkbox value
		newURL.setText(prefs.getURL());			//set url text
		newURL.setEnabled(prefs.isOverRideURL());//set if the url field is enabled
		
		overrideSID.setSelected(prefs.isOverRideSID());//set the checkbox value
		newSID.setText(prefs.getSID());			//set the sid text
		newSID.setEnabled(prefs.isOverRideSID());//set if the sid field is enabled
		
		enableCampusGrad.setSelected(prefs.isDownloadGrad());//set if on campus grad courses are downloaded
		enableDistGrad.setSelected(prefs.isDownloadGradDist());//set if off campus grad courses are downloaded
		enableOverrideDist.setSelected(prefs.isOverrideGradDist());//set if off campus override url should be used
		enableOverrideGrad.setSelected(prefs.isOverrideGrad());//set if on campus override url should be used
		newCampus.setText(prefs.getGradURL());//set the url text
		newCampus.setEnabled(prefs.isOverrideGrad());//set the url enable
		newDist.setText(prefs.getGradDistURL());//set the url text
		newDist.setEnabled(prefs.isOverrideGradDist());//set the url enable
		analyticsOptOut.setSelected(prefs.isAnalyticsOptOut());
		
		enableRatings.setSelected(prefs.isRatingsEnabled());//set if ratings enabled
		enableRatings(prefs.isRatingsEnabled());//set the ratings panel's enabled state
		
		enableRMPRatings.setSelected(prefs.isRateMyProfessorEnabled());//set the rmp enable checkbox
		
		boolean off = prefs.hasDayOff();		//make bool array for the selected day off
		dayOff.setSelected(off);				//set checkbox status
		
		enableDays(off && prefs.isRatingsEnabled());//set the enabled state for the days panel
		
		boolean monBool = prefs.getDaysOff()[Day.monday.value()];//get monday bool
		boolean tueBool = prefs.getDaysOff()[Day.tuesday.value()];//get tuesday bool
		boolean wedBool = prefs.getDaysOff()[Day.wednesday.value()];//get wednesday bool
		boolean thuBool = prefs.getDaysOff()[Day.thursday.value()];//get thursday bool
		boolean friBool = prefs.getDaysOff()[Day.friday.value()];//get friday bool
		
		monday.setSelected(monBool);			//set option button value
		tuesday.setSelected(tueBool);			//set option button value
		wednesday.setSelected(wedBool);			//set option button value
		thursday.setSelected(thuBool);			//set option button value
		friday.setSelected(friBool);			//set option button value
		
		if (!(monBool || tueBool || wedBool || thuBool || friBool)){//check if none should be selected
			daysGroup.clearSelection();			//set cleared if none should be selected
		}
		
		minGap.setText(Integer.toString((int)prefs.getShortestBreak()));//set min gap text
		maxGap.setText(Integer.toString((int)prefs.getLongestBreak()));//set max gap text
		
		minGap.setText((minGap.getText().compareTo("0.0") == Compare.equal.value()) ? "1.0" : minGap.getText());//fix the min gap text if necessary
		maxGap.setText((maxGap.getText().compareTo("0.0") == Compare.equal.value()) ? "1.0" : maxGap.getText());//fix the max gap text if necessary
		
		start.setText(prefs.getPreferred().getStartTime().toString());//set start time
		end.setText(prefs.getPreferred().getEndTime().toString());//set end time
		
		start.setText((start.getText().compareTo("0:00") == Compare.equal.value()) ? "8:00" : start.getText());//fix the start time text if necessary
		end.setText((end.getText().compareTo("0:00") == Compare.equal.value()) ? "6:00" : end.getText());//fix the end time text if necessary
	}
	
	
	/*********************************************************
	 * @purpose Enable or disable the days frame as specified
	 * 
	 * @param boolean val: if the controls should be enabled
	********************************************************/
	public void enableDays(boolean val){
		monday.setEnabled(val);					//set if the following controls are
		tuesday.setEnabled(val);				//enabled based on the parameter
		wednesday.setEnabled(val);				
		thursday.setEnabled(val);
		friday.setEnabled(val);
	}
	
	
	/*********************************************************
	 * @purpose Enable or disable the ratings panel as specified
	 * 
	 * @param boolean val: if the controls should be enabled
	********************************************************/
	public void enableRatings(boolean val){		//set if the following controls are enabled
		minGap.setEnabled(val);					//based on the parameter
		maxGap.setEnabled(val);
		start.setEnabled(val);
		end.setEnabled(val);
		enableRMPRatings.setEnabled(val);
		dayOff.setEnabled(val);
		
		ratings.setEnabled(val);
		
		maxGapLbl.setEnabled(val);
		minGapLbl.setEnabled(val);
		startTime.setEnabled(val);
		endTime.setEnabled(val);
		
		if(val && dayOff.isSelected()){			//special case for the days panel
			enableDays(true);					//which is inside the ratings panel
		}										//must also be enabled in the checkbox
		else{
			enableDays(false);
		}
	}
	
	
	/*********************************************************
	 * @purpose Builds the ratings panel and laysout its components
	********************************************************/
	public void buildRatingsPanel(){
		ratings = new JPanel();					//create the panel
		ratings.setBorder(BorderFactory.createTitledBorder("Ratings"));//add the titled border
		ratingsLayout = new GroupLayout(ratings);//create the layout manager
		ratings.setLayout(ratingsLayout);		//set the layout manager
		
		minGapLbl = new JLabel("Minimum Gap: ");//create the text label
		minGap = new JFormattedTextField(new NumberFormatter(new DecimalFormat("0")));		//create the text fields
		minGap.setToolTipText("The minimum number of minutes preferred " +
			"between classes.");				//set the tool tip
		minGapLbl.setToolTipText(minGap.getToolTipText());//set tool tip
		//minGap.addFocusListener(focus);			//add form's focus listener
													
		maxGapLbl = new JLabel("Maximum Gap: ");//create the text label
		maxGap = new JFormattedTextField(minGap.getFormatter());//create the text field
		maxGap.setToolTipText("The maximum number of minutes preferred " +
			"between classes.");				//set tool tip text
		maxGapLbl.setToolTipText(maxGap.getToolTipText());//set tool tip
		maxGap.addFocusListener(focus);			//add form's focus listener
	
		startTime = new JLabel("Earliest Start Time: ");//create new text label

		//try {
			start = new JFormattedTextField();//new MaskFormatter("*#:##"));
			//((MaskFormatter)start.getFormatter()).setInvalidCharacters("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		//} catch (ParseException e) {
		//	e.printStackTrace();
		//}
				
		start.setToolTipText("The preferred start time in the morning.");//set tool tip
		startTime.setToolTipText(start.getToolTipText());//set tool tip
		start.addFocusListener(focus);			//add form's focus listener
		
		endTime = new JLabel("Latest End Time: ");//create new text label
		end = new JFormattedTextField();		//create text field
		end.setToolTipText("The preferred end time in the afternoon.");//set tool tip
		endTime.setToolTipText(end.getToolTipText());//set tool tip
		end.addFocusListener(focus);			//add form's focus listener
		
		dayOff = new JCheckBox("Enable Preferred Day Off");//create new checkbox
		dayOff.addActionListener(chkBox);		//add form's action listener for chk boxes
		dayOff.setMnemonic('P');				//set control's mnemonic
		dayOff.setToolTipText("Set the preferred day for no classes " +
			"if possible.");					//set tool tip
		
		daysOff = new JPanel();					//new panel for the days off
		daysOff.setBorder(BorderFactory.createTitledBorder(""));//set the titled border
		daysOff.setToolTipText(dayOff.getToolTipText());//set the tool tip text
		
		daysLayout = new GroupLayout(daysOff);	//create a group layout for the days panel
		daysOff.setLayout(daysLayout);			//set the layout
		
		monday = new JRadioButton("Monday");	//create the days off radio buttons
		tuesday = new JRadioButton("Tuesday");	//create the days off radio buttons
		wednesday = new JRadioButton("Wednesday");//create the days off radio buttons
		thursday = new JRadioButton("Thursday");//create the days off radio buttons
		friday = new JRadioButton("Friday");	//create the days off radio buttons

		monday.setToolTipText(dayOff.getToolTipText());//set the days tool tip text
		tuesday.setToolTipText(dayOff.getToolTipText());//set the days tool tip text
		wednesday.setToolTipText(dayOff.getToolTipText());//set the days tool tip text
		thursday.setToolTipText(dayOff.getToolTipText());//set the days tool tip text
		friday.setToolTipText(dayOff.getToolTipText());//set the days tool tip text
		
		monday.setMnemonic('n');				//set the days mnemonic
		tuesday.setMnemonic('T');				//set the days mnemonic
		wednesday.setMnemonic('W');				//set the days mnemonic
		thursday.setMnemonic('h');				//set the days mnemonic
		friday.setMnemonic('F');				//set the days mnemonic
		
		daysGroup = new ButtonGroup();			//create button group for the days
		daysGroup.add(monday);					//add the days to the button group
		daysGroup.add(tuesday);					//add the days to the button group
		daysGroup.add(wednesday);				//add the days to the button group
		daysGroup.add(thursday);				//add the days to the button group
		daysGroup.add(friday);					//add the days to the button group
		
		enableRMPRatings = new JCheckBox("Enable Rate My Professor Ratings");//create new check box
		enableRMPRatings.setMnemonic('M');		//set mnemonic
		enableRMPRatings.setToolTipText("Set if professor ratings should" + 
			" be downloaded from ratemyprofessors.com");//set tool tip
		
												//for the days panel
		daysLayout.setHorizontalGroup(daysLayout.createSequentialGroup()//make a single horiz sequential group
				.addComponent(monday)			//add the days to the horizontal layout sequentially
				.addComponent(tuesday)			//add the days to the horizontal layout sequentially
				.addComponent(wednesday)		//add the days to the horizontal layout sequentially
				.addComponent(thursday)			//add the days to the horizontal layout sequentially
				.addComponent(friday)			//add the days to the horizontal layout sequentially
		);
		
		daysLayout.setVerticalGroup(daysLayout.createSequentialGroup()//make a single horiz group for the vertical layout
			.addGroup(daysLayout.createParallelGroup()//create a parallel row for the vertical layout as theonly row
				.addComponent(monday)			//add the days to the vertical layout in parallel
				.addComponent(tuesday)			//add the days to the vertical layout in parallel
				.addComponent(wednesday)		//add the days to the vertical layout in parallel
				.addComponent(thursday)			//add the days to the vertical layout in parallel
				.addComponent(friday)			//add the days to the vertical layout in parallel
			)
		);
		
												//for the ratings panel
		ratingsLayout.setHorizontalGroup(ratingsLayout.createParallelGroup()//create a single parallel horiz column
			.addGroup(ratingsLayout.createSequentialGroup()//make a sequential group of columns within that col
				.addGroup(ratingsLayout.createParallelGroup()//make a parallel group within that group of columns
					.addGroup(ratingsLayout.createSequentialGroup()//add these items to the first column
						.addGap(marginSpace)	//add item to the first column
						.addComponent(minGapLbl)//add item to the first column
						.addGap(horizSpace)		//add item to the first column
						.addComponent(minGap)//add item to the first column
					)
					.addGroup(ratingsLayout.createSequentialGroup()//create second group of sequential items for first column
						.addGap(marginSpace)	//add item to the first column
						.addComponent(startTime)//add item to the first column
						.addGap(horizSpace)		//add item to the first column
						.addComponent(start)//add item to the first column
					)
				)
				.addGap(2 * horizSpace)			//add space between columns
				.addGroup(ratingsLayout.createParallelGroup()//create parallel group for second column
					.addGroup(ratingsLayout.createSequentialGroup()//first group of items inside the column
						.addGap(marginSpace)	//add item to the second column
						.addComponent(maxGapLbl)//add item to the second column
						.addGap(horizSpace)		//add item to the second column
						.addComponent(maxGap)//add item to the second column	
					)
					.addGroup(ratingsLayout.createSequentialGroup()//second group of items to add to the second column
						.addGap(marginSpace)	//add item to the second column
						.addComponent(endTime)	//add item to the second column
						.addGap(horizSpace)		//add item to the second column
						.addComponent(end)//add item to the second column
					)
				)
			)
			.addGroup(ratingsLayout.createSequentialGroup()//add sequential group in the main column
				.addGap(marginSpace)			//add item to the column
				.addComponent(dayOff)			//add item to the column
			)
			.addGroup(ratingsLayout.createSequentialGroup()//create a new sequential group in the main column
				.addGap(marginSpace - marginSpace/3)//add item to the column
				.addComponent(daysOff)			//add item to the column
				.addGap(marginSpace)			//add item to the column
			)
			.addGroup(ratingsLayout.createSequentialGroup()//create new sequential group in the main column
				.addGap(marginSpace)			//add item to the column
				.addComponent(enableRMPRatings)	//add item to the column
			)
		);
		
		ratingsLayout.setVerticalGroup(ratingsLayout.createSequentialGroup()//create sequential vertical group for rows
			.addGroup(ratingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)//create first row
				.addComponent(minGapLbl)//add item to row specifying size
				.addComponent(minGap)//add item to row specifying size
				.addComponent(maxGapLbl)//add item to row specifying size
				.addComponent(maxGap)//add item to row specifying size
			)
			.addGap(2 * horizSpace)				//add gap between rows
			.addGroup(ratingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)//create second row
				.addComponent(startTime)//add item to row specifying size
				.addComponent(start)//add item to row specifying size
				.addComponent(endTime)//add item to row specifying size
				.addComponent(end)	//add item to row specifying size
			)
			.addGap(3 * horizSpace)				//add gap between rows
			.addComponent(dayOff)				//add single item row
			.addComponent(daysOff)				//add single item row
			.addGap(3 * horizSpace)				//add gap between rows
			.addComponent(enableRMPRatings)		//add single item row
		);
	}

	private void registerConfigEvent(){
		Map<String, Object> configEvent = new HashMap<>();
		KeenUtils.addNestedMapEntry(configEvent, "config.analytics.enabled", Main.prefs.isAnalyticsOptOut());
		KeenUtils.addNestedMapEntry(configEvent, "config.term.current", Main.prefs.getCurrentTerm());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.distance.url", Main.prefs.getGradDistURL());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.campus.url", Main.prefs.getGradURL());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.undergraduate.url", Main.prefs.getURL());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.enabled", Main.prefs.isDownloadGrad());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.distance.enabled", Main.prefs.isDownloadGradDist());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.campus.enabled", Main.prefs.isDownloadGrad());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.undergraduate.enabled", Main.prefs.isDownloadUGrad());
		KeenUtils.addNestedMapEntry(configEvent, "config.schedule.limit", Main.prefs.getGreyCodeLimit());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.campus.override", Main.prefs.isOverrideGrad());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.graduate.distance.override", Main.prefs.isOverrideGradDist());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.undergraduate.override", Main.prefs.isOverRideURL());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.ratemyprofessor.override", Main.prefs.isOverRideSID());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.ratemyprofessor.enabled", Main.prefs.isRateMyProfessorEnabled());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.enabled", Main.prefs.isRatingsEnabled());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.schedule.break.max", Main.prefs.getLongestBreak());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.schedule.break.min", Main.prefs.getShortestBreak());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.schedule.days.enabled", Main.prefs.hasDayOff());
		KeenUtils.addNestedMapEntry(configEvent, "config.rating.ratemyprofessor.sid", Main.prefs.getSID());
		KeenUtils.addNestedMapEntry(configEvent, "config.course.staleness.max", Main.prefs.getUpdateMin());
		
		for(Day day : Day.values()){
			KeenUtils.addNestedMapEntry(configEvent, "config.rating.schedule.days." + day.toString(), Main.prefs.getDaysOff()[day.ordinal()]);
		}
		
		Main.registerEvent(Main.KEEN_CONFIG, configEvent);
	}
	
	
	/*********************************************************
	 * @purpose To enable and disable items based on the status
	 * 		of the associated checkboxes
	 * 
	 * @see ActionListener
	********************************************************/
	private class enableListener implements ActionListener{
			
		
		/*********************************************************
		 * @pupose To enable or disable the appropriate sections
		 * 		based on the triggering checkbox
		 * 
		 * @param ActionEvent: the triggering ActionEvent
		 * 
		 * @see ActionListener
		********************************************************/
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();	//get the sourcing object
			
			if(source.equals(enableRatings)){	//check if is enableRatings checkbox
				enableRatings(enableRatings.isSelected());//set the ratings group's enabled status
				if (enableRatings.isSelected()){//if it is enabled
					minGap.requestFocusInWindow();//set the focus
				}
			}
			else if(source.equals(overrideURL)){//check if the overrideURL checkbox triggered the event
				newURL.setEnabled(overrideURL.isSelected());//set the newURL text field's enable status
				if (overrideURL.isSelected()){	//if it is enabled
					newURL.requestFocusInWindow();//set the focus to the new URL text field
				}
			}
			else if(source.equals(overrideSID)){//check if the overrideSID checkbox triggered the event
				newSID.setEnabled(overrideSID.isSelected());//set the newSID text field's enable status
				if (overrideSID.isSelected()){	//if it is enabled
					newSID.requestFocusInWindow();//set the focus to the new SID text field
				}
			}
			else if(source.equals(dayOff)){		//check if the dayOff checkbox triggered the event
				enableDays(dayOff.isSelected());//set the enable for the group
			}
			else if(source.equals(enableOverrideGrad)){//check if on campus override triggered
				newCampus.setEnabled(enableOverrideGrad.isSelected());//enable/disable the text field
				if (enableOverrideGrad.isSelected()){//if became selected
					newCampus.requestFocusInWindow();//focus in window
				}
			}
			else if(source.equals(enableOverrideDist)){//check if off campus override triggered
				newDist.setEnabled(enableOverrideDist.isSelected());//enable/disable the text field
				if (enableOverrideDist.isSelected()){//if became selectged
					newDist.requestFocusInWindow();	//request focus
				}
			}
		}
	}
	
	
	/*********************************************************
	 * @purpose Provides a single interface that handles all the button events
	 * 
	 * @see ActionListener
	********************************************************/
	private class buttonListener implements ActionListener{
		
		
		/*********************************************************
		 * @purpose Provides a single method for handling button events
		 * 
		 * @param ActionEvent event: the triggered event
		 * 
		 * @see ActionListener
		********************************************************/
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();	//get the source of teh event
			boolean updateRMP = false;	//boolean for downloading rmp
			
			if(source.equals(apply)){			//check if the source is the apply button
				Preferences prefs = Main.prefs;	//get the main form preferences
				
				double min = 0;					//create and initialize a double for the min gap
				double max = 0;					//create and initialize a double for the max gap
				boolean cont = true;			//create and initialise a boolean for error checking
				Period preferred = new Period();//create a new preferred period
				
				if (enableRatings.isSelected()){
					try{							//try to catch exceptions
						min = Double.parseDouble(minGap.getText() + ".0");//get the mingap val as a double
						
						if(min == 0){				//check for bounds errors with 0
							min = 1;				//fix bound error to 1
						}
					}
					catch(Exception ex){			//if invalid input
						JOptionPane.showMessageDialog(//show invalid input dialog
							Main.master.mainMenu.optionsFrame, 
							"Invalid value for minimum preferred gap between classes.", 
							"Invalid Entry", JOptionPane.ERROR_MESSAGE);
						cont = false;				//set continuation to false
						minGap.requestFocusInWindow();//request focus in mingap
					}
					
					try{							//try to catch exceptions
						max = Double.parseDouble(maxGap.getText() + ".0");//get max gap value as a double
						
						if(max == 0){				//check for bounds error when 0
							max = 1;				//correct bound error
						}
					}
					catch(Exception ex){			//catch invalid input
						JOptionPane.showMessageDialog(//show invalid input dialog
							Main.master.mainMenu.optionsFrame, 
							"Invalid value for maximum preferred gap between classes.", 
							"Invalid Entry", JOptionPane.ERROR_MESSAGE);
						if (cont){					//check if focus should vbe set
							maxGap.requestFocusInWindow();//set the focus to maxGap
						}
						
						cont = false;				//set to no continuation
					}
					
					try{							//try to catch errors
						String period = start.getText() + " am - " + end.getText();//make new period string
						
						Scanner parse = new Scanner(start.getText());//parse the end text for hour
						parse.useDelimiter(":");	//using the ":" delimiter
						int hour = parse.nextInt();	//get the hour
						hour = (hour == 12) ? 0 : hour;//correct hour if necessary
						
						if (hour > 12 || hour < 1){	//check for invalid 12 hour time hour
							throw new Exception();	//throw exception if invalid
						}						
						
						parse = new Scanner(end.getText());//parse the end text for hour
						parse.useDelimiter(":");	//using the ":" delimiter
						hour = parse.nextInt();	//get the hour
						hour = (hour == 12) ? 0 : hour;//correct hour if necessary
						
						if (hour > 12 || hour < 1){	//check for invalid 12 hour time hour
							throw new Exception();	//throw exception if invalid
						}
						
						period += (hour <= 8) ? " pm" : " am";//append the am/pm portion
						
						if (!preferred.setPeriod(period)){//check if setting the period fails
							throw new Exception();	//if fails, throw exception
						}
					}
					catch (Exception ex){			//if invalid input for period string
						JOptionPane.showMessageDialog(//show invalid input dialog
							Main.master.mainMenu.optionsFrame, 
							"Invalid value for preferred start or end time.", 
							"Invalid Entry", JOptionPane.ERROR_MESSAGE);
						if (cont){					//check if focus should be set
								start.requestFocusInWindow();//set focus to the start time
						}
							
						cont = false;				//set continuation to false
					}
					
					if (daysGroup.getSelection() == null && dayOff.isSelected()){//check if no day off specified
						JOptionPane.showMessageDialog(//show invalid day dialog
							Main.master.mainMenu.optionsFrame, 
							"Please select a preferred day off.", 
							"Invalid Entry", JOptionPane.ERROR_MESSAGE);
						
						if (cont){					//check if focus should be set
							monday.requestFocusInWindow();//set focus to monday
						}
						cont = false;				//set continuation to false
					}
				}
				else{
					min = 1;						//set back to default
					max = 480;						//set back to default
				}
				
				if(!(enableUGrad.isSelected() || enableCampusGrad.isSelected() || enableDistGrad.isSelected())){
					cont = false;
					JOptionPane.showMessageDialog(Main.master.mainMenu.optionsFrame, 
							"Please select at least one type of courses to download (ie. Undergrad, On-Campus Graduate, Distance Learning Graduate).",
							"Invalid Entry", JOptionPane.ERROR_MESSAGE);
				}
				
				if (cont){						//check if should continue, ie. no errors
					prefs.setDayOff(dayOff.isSelected());//set the day off boolean
					
					boolean[] days = new boolean[Day.values().length];//make bool array for daysOff
					days[Day.monday.value()] = monday.isSelected();//get if day off
					days[Day.tuesday.value()] = tuesday.isSelected();//get if day off
					days[Day.wednesday.value()] = wednesday.isSelected();//get if day off
					days[Day.thursday.value()] = thursday.isSelected();//get if day off
					days[Day.friday.value()] = friday.isSelected();//get if day off
					prefs.setDaysOff(days);		//set the days off array
					
					updateRMP = !prefs.isRateMyProfessorEnabled() && enableRMPRatings.isSelected();
					
					prefs.setRateMyProfessorEnabled(enableRMPRatings.isSelected());//set if RMP is enabled
					prefs.setRatingsEnabled(enableRatings.isSelected());//set if ratings are enabled
					
					prefs.setDownloadUGrad(enableUGrad.isSelected());//set the download undergrad
					prefs.setOverRideURL(overrideURL.isSelected());//set the url override
					prefs.setURL(newURL.getText());	//set the url override value
					
					prefs.setOverRideSID(overrideSID.isSelected());//set the sid override
					prefs.setSID(newSID.getText());	//set the sid override value
					
					prefs.setDownloadGrad(enableCampusGrad.isSelected());//set the enable
					prefs.setDownloadGradDist(enableDistGrad.isSelected());//set the enable
					prefs.setOverrideGrad(enableOverrideGrad.isSelected());//set the override
					prefs.setOverrideGradDist(enableOverrideDist.isSelected());//set the override
					prefs.setGradURL(newCampus.getText());//set the url
					prefs.setGradDistURL(newDist.getText());//set the url
					prefs.setAnalyticsOptOut(analyticsOptOut.isSelected());
					
					prefs.setShortestBreak(min);	//set the shortest break
					prefs.setLongestBreak(max);		//set the longest break
					
					prefs.setPreferred(preferred);	//set the preferred period
					
					updateSettingsFrame();			//update the settings frame
					
					if(Main.prefs.save()){			//try saving and if successful
						JOptionPane.showMessageDialog(Main.master.mainMenu.optionsFrame,
							"Settings successfully applied!",//display success
							"Success", JOptionPane.INFORMATION_MESSAGE);
						Main.master.mainMenu.optionsFrame.setVisible(false);//make frame invisible
						isHidden = true;			//set hidden indicator to true
						
						registerConfigEvent();
						
						if (updateRMP){				//check if rmp values need to be updated
							Main.master.setEnabled(false);//disable the main frame
							Main.updateAllForRMP();	//update all open tabs and the main if necessary
						}

						Main.master.requestFocusInWindow();//request focus in the master
					}
					else{
						JOptionPane.showMessageDialog(Main.master.mainMenu.optionsFrame,
							"Preferences were applied, but were unable to be saved.",//display partial error
							"Apply Success, Store Failure", JOptionPane.ERROR_MESSAGE);
					}
					
					Main.reRateAll();					
				}
			}
			else if(source.equals(cancel)){			//if the cancel
				Main.master.mainMenu.optionsFrame.setVisible(false);
				isHidden = true;					//set the hidden field
				Main.master.requestFocusInWindow();//request focus in the master
			}
			else if(source.equals(clear)){
				int result = JOptionPane.showConfirmDialog(Main.master.mainMenu.optionsFrame,
					"Are you sure you want to delete all downloaded course files" +
					" and all generated schedules?");//get confirmation
				if (result == JOptionPane.YES_OPTION){//if confirmed
					boolean success = true;	
					try{
						File dir = new File(Main.dataPath);//get the directory
						
						FilenameFilter filter = new FilenameFilter() {//make filter so only the right 
					        public boolean accept(File dir, String name) {//files are deleted
					            return (name.endsWith(Main.databaseExt) || //specifically, database files
					            	name.endsWith(Main.scheduleExt));		//and schedule files
					        }
					    };
					    
					    File[] children = dir.listFiles(filter);//array of matching items
					    
					    		//keep track of success for all or at least one failure
						for(File item: children){		//for each file
							success = item.delete() ? success : false;//delete it and keep track of sucess or at least single failure
						}
					}
					catch(Exception ex){
						success = false;
					}
					if (success){					//display complete success
						JOptionPane.showMessageDialog(Main.master.mainMenu.optionsFrame,
							"All cache items were successfully deleted.");
					}
					else{							//display at least single failure
						JOptionPane.showMessageDialog(Main.master.mainMenu.optionsFrame,
							"Some cache items were not deleted.");
					}
				}
			}
		}
	}
	
	
	/*********************************************************
	 * @purpose Selects all the text inside a text field when it
	 * 		gains focus
	 * 
	 * @see FocusListener
	********************************************************/
	private class textFocusListener implements FocusListener{
		
		
		/*********************************************************
		 * @purpose Selects all of the text inside the text field that
		 * 		triggered this event by gaining focus
		 * 
		 * @param FocusEvent event: the triggering event
		 * 
		 * @see FocusListener
		********************************************************/
		public void focusGained(FocusEvent event){
			((JTextField)event.getSource()).selectAll();//select all textfield text
		}
		
		
		/*********************************************************
		 * @purpose Unused in this listener, place holder only for
		 * 		FocusListener
		 * 
		 * @param FocusEvent event: the triggering event
		 * 
		 * @see FocusListener
		********************************************************/
		public void focusLost(FocusEvent event){}
	}
}
