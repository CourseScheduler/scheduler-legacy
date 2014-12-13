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
 * File: MainMenu.java
 * 
 * Contains class:
 * 
 * 		MainMenu:
 * 
 * 			Purpose: To provide a simple method for implementing
 * 						a main menu bar for the application
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;							//declare member of the scheduler package


/********************************************************
 * The following are the imports necessary to facilitate
 * 		the interface's function, instead of importing all
********************************************************/
import javax.swing.XTabComponent;
import javax.swing.Icon;
import javax.swing.JMenu;					//used to make menu
import javax.swing.JMenuBar;				//extended by MainMenu
import javax.swing.JMenuItem;				//used inside the menus
import javax.swing.JScrollPane;
import javax.swing.JSeparator;				//used to separate items
import javax.swing.GroupLayout;				//used with the display term

import java.awt.BorderLayout;				//used to display groups

import javax.swing.JPanel;					//used for grouping items
import javax.swing.JLabel;					//used to display the current term
import javax.swing.JFileChooser;			//used to select files
import javax.swing.filechooser.FileNameExtensionFilter;//used to filter files
import javax.swing.filechooser.FileView;
import javax.swing.JOptionPane;				//import popup dialogs

import java.awt.event.ActionEvent;			//used for events
import java.awt.event.ActionListener;		//used to listen for events
import java.beans.PropertyChangeEvent;		//used for property change events
import java.beans.PropertyChangeListener;	//used to detect propterty change events
import java.awt.Font;						//used to define the font
import java.awt.Dimension;					//used to set sizes
import java.awt.GridLayout;					//used for layouts
import java.io.File;						//used to find files
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.Box;						//used for boxes

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/********************************************************
 * Class MainMenu
 * 
 * @purpose Provides a structure to create the main menu 
 * 		bar and all of its children and prepare them for use
 * 
 * @see JMenuBar
********************************************************/
public class MainMenu extends JMenuBar {
	
	/**
	 * Static Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(MainMenu.class);
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2009021600061L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.mainMenu.id;				//serial version
	
	
	/********************************************************
	 * The following are the protected objects for the file menu
	********************************************************/
	protected JMenu fileMenu;				//the file menu
	protected JMenuItem chooseTermFileMenu;	//the File -> Choose Current Term menu item
	protected JMenuItem downloadFileMenu;	//the File -> Download Course Set menu item
	protected JMenuItem showCoursesFileMenu;//the File -> Show Courses menu item
	protected JSeparator exitSepFileMenu;	//the separator inbetween Download and Exit
	protected JMenuItem exitFileMenu;		//the File -> Exit Scheduler menu item
	protected ChooseTerm termChooser;		//the Choose Term frame
	protected menuListener menus;			//the menu click listener
	
	
	/********************************************************
	 * The following are the objects for the current term display
	********************************************************/
	protected JMenu termMenu;				//the menu to make the term sho up on the right
	protected JLabel currentTerm;			//label to display the current term
	protected JLabel downloadDate;			//label to display the download date
	protected GroupLayout right;			//layout for the above panel
	protected static String mainMenuPad = new String("  ");//padding for the term display
	protected Font termFont;					//font for the term display label
	
	
	/********************************************************
	 * The following are the objects for the schedules menu
	********************************************************/
	protected JMenu scheduleMenu;			//the Schedule menu
	protected JMenuItem newScheduleMenu;	//the Schedule -> New Schedule menu item
	protected JSeparator otherSepScheduleMenu;//the separator inbetween new and save
	protected JMenuItem saveScheduleMenu;	//the Schedule -> Save Schedule menu item
	protected JMenuItem openScheduleMenu;	//the Schedule -> Open Schedule menu item
	protected JMenuItem deleteScheduleMenu;	//the Schedule -> Delete Schedule menu item
	protected JSeparator secondSepScheduleMenu;//separator inbetween open and print
	protected JMenuItem printScheduleMenu;	//the Schedule -> Print menu item
	protected JFileChooser select;			//file filechooser
	protected FileListener fileListen;		//file listener
	protected FileDescriptor descript;		//file description
	
	
	/********************************************************
	 * The following are the objects for the options item
	********************************************************/
	protected JMenu optionsMenu;			//the Options menu
	protected JMenuItem settingsOptionsMenu;//the Options -> Settings menu item
	protected OptionsFrame optionsFrame;	//the options frame
	
	
	/********************************************************
	 * The following are the objects for the help menu
	********************************************************/
	protected JMenu helpMenu;				//the Help menu
	protected JMenuItem aboutHelpMenu;		//the Help -> About menu item
	protected HelpAboutFrame aboutHelpFrame;//the frame that displays the Help -> About info
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Builds the menu bar by calling all of the build
	 * 		functions for the individual menus 
	 ********************************************************/
	public MainMenu(){
		menus = new menuListener();		//make the menu listener
		buildFileMenu();				//build File menu
		buildScheduleMenu();			//build Schedule menu
		buildOptionsMenu();				//build Options Menu
		buildHelpMenu();				//build Help Menu
		buildDispMenu();				//build the display menu item	
	}
	
	
	/********************************************************
	 * @purpose Builds the display menu item 
	 ********************************************************/
	public void buildDispMenu(){
		String term = Main.prefs.getCurrentTerm();//get current term in correct form
		currentTerm = new JLabel("Current Term: " + 
				Term.getTermString(term) +
				mainMenuPad, JLabel.TRAILING);//add spaces and set to right
		try{
			downloadDate = new JLabel("Downloaded: " + Main.terms.get(
					Main.prefs.getCurrentTerm()).getCreation().getTime().toString());
		}
		catch(NullPointerException ex){
			downloadDate = new JLabel("Not Yet Downloaded");
		}
		
		
		termFont = new Font(Font.SERIF, Font.BOLD, 16);//define the font
		currentTerm.setFont(termFont);	//set the font
		
		this.add(Box.createHorizontalGlue());//force to right side
		this.add(currentTerm);				//add the display panel to the menu
		this.add(downloadDate);				//add the download date to the menu
	}
		
	
	/********************************************************
	 * @purpose Builds the file menu and prepares it for use
	********************************************************/
	private void buildFileMenu(){
		fileMenu = new JMenu("File");	//create the menu
		fileMenu.setMnemonic('F');		//set menu mnemonic
		
		chooseTermFileMenu = new JMenuItem("Choose Current Term");//create menu item
		downloadFileMenu = new JMenuItem("Download Course Set");//create menu item
		showCoursesFileMenu = new JMenuItem("Show Course Set");//create menu item
		
		chooseTermFileMenu.setMnemonic('C');//set mnemonic
		downloadFileMenu.setMnemonic('D');//set mnemonic
		showCoursesFileMenu.setMnemonic('S');//set mnemonic
		
		
		exitSepFileMenu = new JSeparator();		//create new separator
		exitFileMenu = new JMenuItem("Exit Scheduler");//create new menu item
		
		exitFileMenu.setMnemonic('x');	//set mnemonic
		
		termChooser = new ChooseTerm();	//create the choose term frame
		
		chooseTermFileMenu.addActionListener(menus);//add listener			
		downloadFileMenu.addActionListener(menus);//add listener
		showCoursesFileMenu.addActionListener(menus);//add listener
		exitFileMenu.addActionListener(menus);//add listener
				
		fileMenu.add(chooseTermFileMenu);//add menu item to menu
		fileMenu.add(downloadFileMenu);	//add menu item to menu
		//fileMenu.add(showCoursesFileMenu);//add menu item to menu
		fileMenu.add(exitSepFileMenu);	//add menu item to menu
		fileMenu.add(exitFileMenu);		//add menu item to menu
		this.add(fileMenu);				//add menu to menu bar
	}
	
	
	/********************************************************
	 * 
	********************************************************/
	private void buildScheduleMenu(){
		scheduleMenu = new JMenu("Schedules");
		
		fileListen = new FileListener();
		descript = new FileDescriptor();
		
		scheduleMenu.setMnemonic('S');
		
		newScheduleMenu = new JMenuItem("New Schedule");
		otherSepScheduleMenu = new JSeparator();
		saveScheduleMenu = new JMenuItem("Save Schedule");
		deleteScheduleMenu = new JMenuItem("Delete Schedule");
		openScheduleMenu = new JMenuItem("Open Schedule");
		secondSepScheduleMenu = new JSeparator();
		printScheduleMenu = new JMenuItem("Print Schedule");
				
		saveScheduleMenu.setMnemonic('S');
		deleteScheduleMenu.setMnemonic('D');
		openScheduleMenu.setMnemonic('O');
		printScheduleMenu.setMnemonic('P');
		newScheduleMenu.setMnemonic('N');
		
		newScheduleMenu.addActionListener(menus);
		saveScheduleMenu.addActionListener(menus);
		deleteScheduleMenu.addActionListener(menus);
		openScheduleMenu.addActionListener(menus);
		printScheduleMenu.addActionListener(menus);
		
		scheduleMenu.add(newScheduleMenu);
		scheduleMenu.add(otherSepScheduleMenu);
		scheduleMenu.add(saveScheduleMenu);
		scheduleMenu.add(openScheduleMenu);
		scheduleMenu.add(deleteScheduleMenu);
		scheduleMenu.add(secondSepScheduleMenu);
		scheduleMenu.add(printScheduleMenu);
		
		select = new JFileChooser();
		select.setFileFilter(new FileNameExtensionFilter(
			"Schedule File", Main.scheduleExt.substring(1)));
		select.setAcceptAllFileFilterUsed(false);
		select.setCurrentDirectory(new File(Main.dataPath));
		select.setAccessory(descript);
		select.addPropertyChangeListener(fileListen);
		select.setFileView(new ImageFileView());
		
		saveScheduleMenu.setEnabled(false);
		printScheduleMenu.setEnabled(false);
		this.add(scheduleMenu);
	}
	
	
	/********************************************************
	 * 
	********************************************************/
	private void buildOptionsMenu(){
		optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic('O');
		
		settingsOptionsMenu = new JMenuItem("Settings");
		settingsOptionsMenu.setMnemonic('S');
		settingsOptionsMenu.addActionListener(menus);
				
		optionsFrame = new OptionsFrame();
				
		optionsMenu.add(settingsOptionsMenu);
		
		this.add(optionsMenu);
	}
	
	
	/********************************************************
	 * @purpose Builds the help menu and adds it to the main menu bar
	********************************************************/
	private void buildHelpMenu(){
		helpMenu = new JMenu("Help");			//create the menu
		aboutHelpMenu = new JMenuItem("About Scheduler");//create the menu item
		
		helpMenu.setMnemonic('H');				//set mnemonic for the menu
		aboutHelpMenu.setMnemonic('A');			//set the mnemonic for the menu item
		
		aboutHelpMenu.addActionListener(menus);//set listener for menu click
		
		aboutHelpFrame = new HelpAboutFrame();	//create new about help frame
		
		helpMenu.add(aboutHelpMenu);			//add the menu item to the menu
		this.add(helpMenu);						//add the menu to the menu bar
	}
	
	
	
	/********************************************************
	 * Class MainFileExitListener
	 * 
	 * @purpose Provides the response for actions to the Menu item
	 * 		File -> Exit Scheduler
	 * 
	 * @see ActionListener
	********************************************************/
	private class menuListener implements ActionListener{
		
		
		/********************************************************
		 * @purpose Provides the response for actions to the Menu items
		 * 
		 * @param ActionEvent event: The event that triggered this listener
		 * 
		 * @see ActionListener
		********************************************************/
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();
			
			if(source.equals(exitFileMenu)){
				System.exit(0);							//exit the application
			}
			else if(source.equals(chooseTermFileMenu)){
				termChooser.getTerm();					//prompt for and return the current term			
			}
			else if(source.equals(downloadFileMenu)){
				Main.master.setEnabled(false);			//disable main frame
				Main.updateDatabase(Main.prefs.getCurrentTerm());//update the database	
			}
			else if(source.equals(showCoursesFileMenu)){
				ViewCourse thisTerm = new ViewCourse();
				Main.master.tabControl.addTab(Term.getTermString(Main.prefs.getCurrentTerm()) + "*",
						thisTerm);				//add a schedule maker for the current term
				Main.master.makeButtonTab(Main.master.tabControl.indexOfComponent(thisTerm));
				Main.master.tabControl.setSelectedComponent(thisTerm);//select new tab				
			}
			else if(source.equals(aboutHelpMenu)){
				aboutHelpFrame.setVisible(true);		//make help about frame visible
			}
			else if(source.equals(settingsOptionsMenu)){
				optionsFrame.setVisible(true);			//make options frame visible
			}
			else if (source.equals(newScheduleMenu)){
				try{
					MakeSchedule thisTerm = new MakeSchedule();//make schedule creator
					Main.master.tabControl.addTab(Term.getTermString(Main.prefs.getCurrentTerm()) + "*",
						thisTerm);				//add a schedule maker for the current term
					Main.master.makeButtonTab(Main.master.tabControl.indexOfComponent(thisTerm));
					Main.master.tabControl.setSelectedComponent(thisTerm);//select new tab
				}
				catch(Exception ex){
					JOptionPane.showMessageDialog(Main.master, 
						"Unable to create a new schedule for this term. " + 
						"You may need to download course list for this term.",
						"Operation Cancelled", JOptionPane.ERROR_MESSAGE);
					Main.master.mainMenu.newScheduleMenu.setEnabled(false);//disable the new schedule menu item
				}
			}
			else if(source.equals(saveScheduleMenu)){
				try{
					MakeSchedule item = (MakeSchedule)Main.master.tabControl.getSelectedComponent();
					
					if(item != null){
						if (item.isEnabled()){
							String[] name = Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(item)).split("- ");
							String dispName = new String();
							
							for(int pos = 1; pos < name.length; pos++){
								if(pos > 1){
									dispName += "- ";
								}
								dispName += name[pos];
							}
							if(dispName.endsWith("*")){
								dispName = dispName.substring(0, dispName.length() - 1);
							}
							
							String[] courses = new String[item.scheduleClassModel.size()];
							
							for(int pos = 0; pos < courses.length; pos++){
								courses[pos] = (String)item.scheduleClassModel.get(pos);
							}
							
							int num;
							try{
								num = Integer.parseInt(item.minUse.getText());
							}
							catch(NumberFormatException ex){
								num = courses.length;
							}
							
							ScheduleWrap toSave = new ScheduleWrap(courses, item.primary, item.local, item.useClosed.isSelected(),
									item.useAll.isSelected(), item.autoRefresh.isSelected(), num, item.dependancy, item.sectionSelections, item.numberSelections, item.reportingEnabled.isSelected());
							
							select.setSelectedFile(new File(dispName));
							descript.setDefault(select.getSelectedFile());
							int hresult = select.showSaveDialog(Main.master);
							
							if(hresult == JFileChooser.APPROVE_OPTION){
								File file = select.getSelectedFile();
								String filePath = file.getCanonicalPath();
								String fileName = file.getName();
								filePath = (filePath.substring(filePath.length() - 4, filePath.length()).compareTo(Main.scheduleExt) == Compare.equal.value()) ? filePath : filePath + Main.scheduleExt;
								fileName = (fileName.substring(fileName.length() - 4, fileName.length()).compareTo(Main.scheduleExt) == Compare.equal.value()) ? fileName : fileName + Main.scheduleExt;
								
								if(toSave.save(filePath)){
									int pos = Main.master.tabControl.indexOfComponent(item);
									Main.master.tabControl.setTitleAt(pos, Term.getTermString(item.local.getTerm()) + " - " + fileName);
									((XTabComponent)Main.master.tabControl.getTabComponentAt(pos)).refresh();
									
									JOptionPane.showMessageDialog(Main.master, "Schedule successfully saved!",
										"Save Sucess", JOptionPane.INFORMATION_MESSAGE);
								}
								else{
									JOptionPane.showMessageDialog(Main.master, "Saving of schedule failed!",
											"Save Failure", JOptionPane.ERROR_MESSAGE);
								}							
							}
							else{
								JOptionPane.showMessageDialog(Main.master, "Saving was cancelled!",
										"Save Cancelled", JOptionPane.INFORMATION_MESSAGE);
							}						
						}
						else{
							JOptionPane.showMessageDialog(Main.master, 
									"Please wait until the build operation is finished before saving the schedules",
									"Please Wait", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				catch(Exception ex){}
			}
			else if(source.equals(openScheduleMenu)){
				descript.setDefault(select.getSelectedFile());
				int hresult = select.showOpenDialog(Main.master);
				
				if(hresult == JFileChooser.APPROVE_OPTION){
					try{
						ScheduleWrap found = ScheduleWrap.load(select.getSelectedFile().getCanonicalPath());
						
						addMadeSchedule(found, select.getSelectedFile().getName());
					}
					catch (Exception ex){
						JOptionPane.showMessageDialog(Main.master,
							"Unable to open the schedule file.", "Open Failed",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else if(source.equals(printScheduleMenu)){
				try {
					MakeSchedule item = (MakeSchedule)Main.master.tabControl.getSelectedComponent();
					
					if(item.isEnabled()){
						
						PrintUtilities.printComponent(item.displayPanel,  Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(item)) + " - Display");
												
						int hresult = JOptionPane.showConfirmDialog(Main.master,
								"Do you want to print the schedule details?", "Print Details",
								JOptionPane.YES_NO_OPTION);
						if (hresult == JOptionPane.YES_OPTION){
							PrintUtilities.printComponent(item.infoPanel,  Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(item)) + " - Details");
						}
					}
					else{
						JOptionPane.showMessageDialog(Main.master, 
								"Please wait until the build operation is finished before printing the schedules",
								"Please Wait", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					logger.error("Unable to print the schedule", e);
				}
			}
			else if(source.equals(deleteScheduleMenu)){
				descript.setDefault(select.getSelectedFile());
	
				int hresult = select.showDialog(Main.master, "Delete");
				
				if(hresult == JFileChooser.APPROVE_OPTION){
					try{
					
						File toDelete = select.getSelectedFile();
						
						String filePath = toDelete.getCanonicalPath();
						
						filePath = (filePath.substring(filePath.length() - 4, filePath.length()).compareTo(Main.scheduleExt) == Compare.equal.value()) ? filePath : filePath + Main.scheduleExt;
						
						toDelete = new File(filePath);
						
						boolean successful = toDelete.delete();
						
						if(successful){
							JOptionPane.showMessageDialog(Main.master,
								"Successfully deleted " + toDelete.getName() + ".",
								"Delete Successful", JOptionPane.INFORMATION_MESSAGE);
							
							for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){
								String text = Main.master.tabControl.getTitleAt(pos);
								
								Scanner titleScan = new Scanner(text);
								titleScan.useDelimiter(" - ");
								String orig = titleScan.next();
								titleScan.skip(titleScan.delimiter());
								titleScan.reset();
																
								if(titleScan.nextLine().equals(toDelete.getName())){
									Main.master.tabControl.setTitleAt(pos, orig);
									Main.master.setModified(false, pos, true);
								}
								
								titleScan.close();
							}
							
							for(int pos = 0; pos < Main.master.detached.size(); pos++){
								String text = Main.master.detached.get(pos).getTitle();
								
								Scanner titleScan = new Scanner(text);
								titleScan.useDelimiter(" - ");
								String orig = titleScan.next();
								titleScan.skip(titleScan.delimiter());
								titleScan.reset();
																
								if(titleScan.nextLine().equals(toDelete.getName())){
									Main.master.detached.get(pos).setTitle(orig);
									Main.master.setModified(true, pos, true);
								}
								titleScan.close();
							}
						}
						else{
							JOptionPane.showMessageDialog(Main.master,
									"Unable to delete " + toDelete.getName() + "!",
									"Delete Failed", JOptionPane.ERROR_MESSAGE);
						}
					}
					catch(Exception ex){
						JOptionPane.showMessageDialog(Main.master,
								"Unable to delete schedule!",
								"Delete Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
	
	public void addMadeSchedule(ScheduleWrap found, String name){
		MakeSchedule newOne = new MakeSchedule();
		
		Database open = Main.terms.get(found.data.getTerm());
		
		if(open == null){
			open = Database.load(found.data.getTerm());
		}
		
		if(open == null){
			Main.terms.put(found.data.getTerm(), found.data);
			found.data.save();
		}
		else if(found.data.isNewerThan(open)){
			Main.terms.remove(open.getTerm());
			Main.terms.put(found.data.getTerm(), found.data);
			found.data.save();
		}
		else{
			Main.terms.remove(found.data.getTerm());
			Main.terms.put(open.getTerm(), open);
			open.save();
		}
		
		newOne.setDatabase(Main.terms.get(found.data.getTerm()), true, true);
		
		Arrays.sort(found.courses);
		
		for(String item: found.courses){
			if(item != null){
				newOne.scheduleClassModel.addElement(item);
				//newOne.sectionModel.addElement(item);
				
				newOne.addSectionPanel(item);
				newOne.addNumberPanel(item);
			}
		}
		
		newOne.autoRefresh.setSelected(false);
		
		newOne.reportingEnabled.setSelected(found.findConflicts);
		newOne.setNumberSelections(found.getNumberSelections());
		newOne.setSectionSelections(found.allowed);
		newOne.showSelectedSection();						
		newOne.scheduleClassList.setModel(newOne.scheduleClassModel);
		newOne.sectionList.setModel(newOne.sectionModel);
		newOne.minUse.setText(Integer.toString(found.toUse));
		newOne.useAll.setSelected(found.useAll);
		newOne.useClosed.setSelected(found.allowClosed);
		newOne.setPrimary(found.primary);
		newOne.setDependancy(found.dependancy);
		newOne.updateScheduleCourseCount();
		
		open = Main.terms.get(found.data.getTerm());
		
		newOne.setDatabase(open, false, false);
		
		Main.master.tabControl.addTab(Term.getTermString(found.data.getTerm()) 
				+ " - " + name, newOne);
		int pos = Main.master.tabControl.indexOfComponent(newOne);
		Main.master.makeButtonTab(pos);
		Main.master.tabControl.setSelectedComponent(newOne);
		newOne.toggleViewSize();
		
		if (open.shouldUpdate()){
			int hresult1 = JOptionPane.showConfirmDialog(Main.master, 
				"This schedule may be outdated and inaccurate. \n Do you want"
				+ " to update the course list?", "Course Data Outdated", JOptionPane.YES_NO_OPTION);
			
			if(hresult1 == JOptionPane.YES_OPTION){
				Main.updateDatabase(found.data.getTerm());
			}
			else{
				newOne.updateSchedules(false);
			}
		}
		else{
			newOne.updateSchedules(false);
		}
		
		newOne.autoRefresh.setSelected(found.autoRefresh);
	}
	
	
	private class FileListener implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent event){
			String propName = event.getPropertyName();
			
			if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propName)){
				File file = (File)event.getNewValue();
				
				descript.update(file);
			}
		}
	}
	
	private class FileDescriptor extends JPanel{
		public static final long serialVersionUID = 211000000L;
		protected JPanel top;
		protected JLabel name;
		protected JLabel semester;
		protected JLabel schedDate;
		protected JLabel dataDate;
		protected JLabel courses;
		protected JScrollPane scroller;
		private Dimension size;
		
		public FileDescriptor(){
			super(new BorderLayout());
			
			top = new JPanel(new GridLayout(4,1));
			size = new Dimension(300, 250);
			
			name = new JLabel("", JLabel.CENTER);
			semester = new JLabel("", JLabel.CENTER);
			schedDate = new JLabel("", JLabel.CENTER);
			dataDate = new JLabel("", JLabel.CENTER);
			courses = new JLabel("", JLabel.CENTER);
			scroller = new JScrollPane(courses);
			scroller.setBorder(null);
			
			top.add(name);
			top.add(semester);
			top.add(schedDate);
			top.add(dataDate);
						
			add(top, BorderLayout.NORTH);
			add(scroller, BorderLayout.CENTER);
			
			setMinimumSize(size);
			//setPreferredSize(size);
			//setMaximumSize(size);
			
			setDefault(null);
		}
		
		public void update(File file){
			try{
				String name = file.getName();
				name = name.substring(name.length() - 4, name.length());
				
				if(name.compareTo(Main.scheduleExt) == Compare.equal.value()){
				
					ScheduleWrap item = ScheduleWrap.load(file.getCanonicalPath());
					
					this.name.setText("File Name: " + file.getName());
					semester.setText("Semester: " + Term.getTermString(item.data.getTerm()));
					schedDate.setText("Creation Date: " + item.date.getTime().toString());
					dataDate.setText("Download Date: " + item.data.getCreation().getTime().toString());
					
					String courseList = new String("");
					
					if(item.courses.length > 0){
						Course other = item.data.getCourse(item.courses[0]);
						courseList = "<html><pre>Courses: " + item.courses[0] + " " + other.getTitle();
					
						int pos;
						
						for(pos = 1; pos < item.courses.length; pos++){
							Course temp = item.data.getCourse(item.courses[pos]);
							courseList += "<br>     " + item.courses[pos] + " " + temp.getTitle();
						}
					}
					else{
						courseList = "<html><pre>No Courses";
					}					
					
					courseList += "</pre></html>";
					courses.setText(courseList);
				}
			}
			catch(Exception ex){
				setDefault(null);
				
				try{
					if(file.isDirectory()){
						dataDate.setText("Directory selected");
					}
					else{
						dataDate.setText("Unable to open schedule");
					}
				}
				catch(NullPointerException ex1){
					dataDate.setText("Directory selected");
				}
			}
		}
		
		public void setDefault(File first){
			if (first == null){
				name.setText("                                                  ");
				semester.setText("                                                  ");
				schedDate.setText("                                                  ");
				dataDate.setText("                                                  ");
				courses.setText("<html><pre>                                                  <br>" + 
						"                                                  <br>" + 
						"                                                  <br>" + 
						"                                                  <br>" +
						"                                                  <br>" + 
						"                                                  <br>" +
						"                                                  </pre>");
			}
			else{
				
			}
		}
	}
	
	
	private class ImageFileView extends FileView{
		@Override
		public Icon getIcon(File file){
			Icon toUse = null;
			String name = file.getName();
			
			if(!file.isDirectory() && name.substring(name.lastIndexOf('.')).compareTo(Main.scheduleExt) == Compare.equal.value()){
				toUse = Main.icon;
			}
			
			return toUse;
		}
	}
}

