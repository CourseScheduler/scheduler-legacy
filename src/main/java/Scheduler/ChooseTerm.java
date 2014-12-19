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
 * Course Scheduler
 * File: ChooseTerm.java
 * 
 * Contains Classes:
 * 		
 * 		ChooseTerm:
 * 
 * 			Purpose: To provide an interface for choosing the
 * 				current term
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;						//declare as member of the scheduler package


/********************************************************
 * Import only the necessary visual classes instead of 
 * 		the entire awt and swing libraries.
********************************************************/
import javax.swing.JFormattedTextField; //formatted text field for input
import javax.swing.JLabel;				//import labels
import javax.swing.JComboBox;			//import comboboxes
import javax.swing.JPanel;				//import panels
import javax.swing.JFrame;				//import frames
import javax.swing.GroupLayout;			//import the group layout
import javax.swing.JButton;				//import buttons

import java.awt.Component;				//import component
import java.awt.Dimension;				//import dimensions
import java.awt.event.ActionListener;	//import action listener interface
import java.awt.event.ActionEvent;		//import the action event
import java.awt.event.KeyListener;		//import the key listener
import java.awt.event.KeyEvent;			//import the key event
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;		//import parse exception for use with MaskFormatter

import javax.swing.JOptionPane;			//import the joption pane message dialogs
import javax.swing.text.MaskFormatter;	

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
//import MaskFormatter for use with JFormattedTextField


/********************************************************
 * Class ChooseTerm
 * 
 * @purpose Provides an interface for selecting the current
 * 		term
 * 
 * @see JFrame
********************************************************/
public class ChooseTerm extends JFrame {
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(ChooseTerm.class);
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008120400069L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.chooseTerm.id;						//serial version
	
	
	protected String previous;				//previous term
	protected Database lastTerm;			//previous database
	
	
	/********************************************************
	 * The following are the private visual fields of the interface
	********************************************************/
	private JLabel prompt;					//the label that holds the prompt
	private JComboBox term;					//the combobox that specifies the term
	private JFormattedTextField year;		//the text field for the year
	private JPanel main;					//main panel
	private Dimension dim = new Dimension(200,160);//main panel's min dimensions
	private JButton ok;						//the apply term button
	private JPanel okPanel;					//the panel for the apply button to center it	
	
	
	/********************************************************
	 * @purpose Restores the previous term settings and database
	 * 		should a failure occur
	********************************************************/
	public void restoreTerm(){
		Main.prefs.setCurrentTerm(previous);
		Main.displayDate();						//display the date
		Main.displayTerm();						//display the term
		Main.terms.put(previous, lastTerm);		//reset database
		this.year.setText(previous.substring(0, 4));//set text in the chooser
		this.term.setSelectedItem(Term.getTerm(Integer.parseInt(
				previous.substring(5,6))));		//reset term in the chooser
	}
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Create a new ChooseTerm interface
	********************************************************/
	@SuppressWarnings("boxing")
	public ChooseTerm(){
		super("Select Term");				//create new frame with title Select Term
		
		super.setIconImage(Main.icon.getImage());		//set the icon
		
		addWindowListener(new WindowCloseResetListener());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//do nothing on clicking the x
				
		this.setMinimumSize(dim);			//set minimum size
		this.setResizable(false);			//disallow resize
		
		prompt = new JLabel("Choose the current term: ");//instantiate and set text for the prompt
		term = new JComboBox(Term.values());//build combobox with the corrent options
		term.setEditable(false);			//dropdown box only
		term.setSelectedIndex(Term.getTerm(	//select the correct term from the current term in the preferences
			Integer.parseInt(Main.prefs.getCurrentTerm().substring(4, 6))).ordinal());
		term.addKeyListener(new enterListener());//add new enter key listener
		
		try {
			year = new JFormattedTextField(new MaskFormatter("####"));//setup the year mask
		} catch (ParseException e) {		//to accept only 4 numbers
			logger.error("Unable to parse year", e);			
		}									//instantiate the text field
		
		year.setText(Main.prefs.getCurrentTerm().substring(0, 4));
		year.addKeyListener(new enterListener());//add new enter key listener
		
		ok = new JButton("Apply");			//create the apply button
		ok.addActionListener(new okListener());//set listener for the ok button
		ok.addKeyListener(new enterListener());//add enter key listener
		ok.setMnemonic('A');				//set the mnemonic
		
		okPanel = new JPanel();				//panel for the ok button
		okPanel.add(ok);					//add the ok button
				
		main = new JPanel();				//new panel for the frame
		GroupLayout group = new GroupLayout(main);//layout manager for the panel
		main.setLayout(group);				//set the layout
		
		group.setAutoCreateGaps(true);		//autocreate gaps 
		group.setAutoCreateContainerGaps(true);
		
		group.setHorizontalGroup(			//define the horizontal groups as
			group.createParallelGroup()		//a parallel group containing
				.addComponent(prompt)		//the prompt and the two sequential groups
				.addGroup(group.createSequentialGroup()//define the first sequential group
					.addGap(50)				//as a gap of 50
					.addComponent(term)		//the term component
					.addComponent(year)		//and the year component
				)							//end of first sequential group
				.addGroup(group.createSequentialGroup()//define the second sequential group
					.addComponent(okPanel)  //and the ok Panel
				)							//end of second sequential group
		);									//end of horizontal group
		
		group.setVerticalGroup(				//define vertical group as
			group.createSequentialGroup()	//a sequential group of
				.addComponent(prompt)		//the prompt, a gap of 10, a parallel group
				.addGap(10)					//a gap of 30, and the ok button
				.addGroup(group.createParallelGroup()//define the parallel group
					.addComponent(term)		//as the term and the year
					.addComponent(year)
				)							//end parallel group
				.addGap(20)
				.addComponent(okPanel)		//add ok panel
		);									//end vertical group
		
		this.add(main);						//add the panel to the frame
		this.setLocationRelativeTo(Main.master);//set it to center of MainFrame
	}
	
	
	/********************************************************
	 * @purpose forces the MainFram to be disabled and brings the
	 * 		ChooseTerm frame to the foreground as visible
	********************************************************/
	public void getTerm(){
		Main.master.setEnabled(false);		//disable the main frame
		this.setVisible(true);				//set this fram visible
	}
	
	
	/********************************************************
	 * Class okListener
	 * 
	 * @purpose Provides the listening function for the ok button
	 * 		on this frame
	 * 
	 * @see ActionListener
	********************************************************/
	private class okListener implements ActionListener{
		
		
		/********************************************************
		 * @purpose Provides the listening function for the ok button
		 * 
		 * @param ActionEvent event: the event that triggered this listener
		 * 
		 * @see ActionListener
		********************************************************/
		public void actionPerformed(ActionEvent event){
			ChooseTerm item = Main.master.mainMenu.termChooser;//get the instance of the choose term
			previous = new String(Main.prefs.getCurrentTerm());//get copy of the old
			lastTerm = Main.terms.get(previous);
			
			if (Preferences.validTermYear(year.getText())){//get if valid year
				Main.prefs.setCurrentTerm(year.getText() + 
						((Term)item.term.getSelectedItem()).toQueryStr());//set the current term to the 
				Main.master.setEnabled(true);		//values on the instance of ChooseTerm, reenable the master
				item.setVisible(false);				//and make this frame invisible
				
				String current = Main.prefs.getCurrentTerm();//get the current term
				
				try{								
					Main.terms.put(current, Database.load(	//try loading the terms database
							current));
				}
				catch (Exception ex){				//catch failed loads
					Main.terms.put(current, null);	//set to null to signal new download
				}
				
				Database check = Main.terms.get(current);//get current database from term
				Main.displayTerm();					//display the term
				Main.displayDate();					//display the date
													//check if the database needs to be downloaded
				int hresult = JOptionPane.YES_OPTION;//initialize the input result
				
				Main.termChanged = false;			//set term changed to false
				Main.master.mainMenu.newScheduleMenu.setEnabled(true);//make sure menu option is enabled
				if(check == null || check.shouldUpdate()){//check if it should update
					hresult = JOptionPane.showConfirmDialog(Main.master, //prompt user
							"The course info for this term may be outdated, would you like to update the course data?",
							"Outdated Course Info", JOptionPane.YES_NO_OPTION);
					if (hresult == JOptionPane.YES_OPTION){//check if yes
						Main.termChanged = true;	//set to changed
						Main.master.mainMenu.downloadFileMenu.doClick();//do the download
					}
					else{							//if no option
						if(check == null){			//only disable if null, not if old data
							Main.master.mainMenu.newScheduleMenu.setEnabled(false);
						}
					}
				}
				else{
					String newTerm = Main.prefs.getCurrentTerm();//get new term
					String term = Term.getTermString(newTerm);	//convert term to title
					
					boolean newMake = true;				//for deciding if we need a new make schedule tab
					
					for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){//for each tab
						Component com = Main.master.tabControl.getComponentAt(pos);//get the component there
						
						Class<?> comClass = com.getClass();//get that components class
						if(comClass.getSimpleName().equals("MakeSchedule")){//check the class
							String title = Main.master.tabControl.getTitleAt(pos);//get tab title
													
							if(title.compareTo(term) == Compare.equal.value()){//check if equal
								newMake = false;		//do not need new make
							}
						}
					}
					
					if(newMake && hresult != JOptionPane.NO_OPTION){
						int result = JOptionPane.showOptionDialog(Main.master, "Create a new schedule for " + 
							Term.getTermString(newTerm), "Create new Schedule?",//ask if we want a new make schedule tab
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
							null, null, null);
						
						if(result == JOptionPane.YES_OPTION){	//if result is yea
							Main.master.mainMenu.newScheduleMenu.doClick();//make tab
						}
					}
				}
				
				Main.termChanged = true;				//set term changed for download prompting
			}
			else{
				JOptionPane.showMessageDialog(
						Main.master.mainMenu.termChooser,
						"Invalid year entered, please enter a valid year.",
						"Error", JOptionPane.ERROR_MESSAGE);//display error
				year.requestFocusInWindow();		//request focus for the year input
			}
		}
	}
	
	
	/********************************************************
	 * Class enterListener
	 * 
	 * @purpose Provides the listening function for the enter key
	 * 
	 * @see KeyListener
	********************************************************/
	private class enterListener implements KeyListener{
		
		
		/********************************************************
		 * @purpose Unused, needed to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyTyped(KeyEvent event){}
		
		
		/********************************************************
		 * @purpose Makes the enter key simulate clicking the ok button
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyPressed(KeyEvent event){
			if (event.getKeyCode() == KeyEvent.VK_ENTER){
				Main.master.mainMenu.termChooser.ok.doClick();
			}
		}
		
		
		/********************************************************
		 * @purpose Unused, needed to implement KeyListener
		 * 
		 * @see KeyListener
		********************************************************/
		public void keyReleased(KeyEvent event){}
	}
	
	private class WindowCloseResetListener extends WindowAdapter{
		
		@Override
		public void windowClosing(WindowEvent event){
			term.setSelectedIndex(Term.getTerm(	//select the correct term from the current term in the preferences
					Integer.parseInt(Main.prefs.getCurrentTerm().substring(4, 6))).ordinal());
			
			year.setText(Main.prefs.getCurrentTerm().substring(0, 4));
			ChooseTerm.this.setVisible(false);
			Main.master.setEnabled(true);
		}
	}
}
