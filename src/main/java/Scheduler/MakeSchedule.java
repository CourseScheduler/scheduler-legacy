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
 * File: MakeSchedule.java
 * 
 * Contains class:
 * 
 * 		MakeSchedule:
 * 
 * 			Purpose: To display and edit course schedulers
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;


/*********************************************************
 * The Following imports are necessary for this class
*********************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*********************************************************
 * class MakeSchedule
 * 
 * @purpose to provide an interface for creating and viewing
 * 		schedules
 * 
 * @see JPanel, Tab (custom)
*********************************************************/
public class MakeSchedule extends JPanel implements Tab {
	
	/**
	 * Static logger
	 */
	private static Logger logger = LoggerFactory.getLogger(MakeSchedule.class);
	
	
	/*********************************************************
	 * The following are static constants for versioning
	*********************************************************/
	protected static final long versionID = 2013010900214L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.makeSchedule.id;				//serial version
	
	
	/*********************************************************
	 * The following are the data fields for the MakeSchedule object
	*********************************************************/
	protected Database local;						//local copy of the database
	protected JFrame owner;							//owning frame of this panel
	protected ArrayList<String> primary;			//list of primary courses
	protected int selectedSchedule;					//item in the schedules list that is selected
	protected LinkedCourses dependancy;				//the linked courses list for dependancies
	
	
	/*********************************************************
	 * The following are the visual items for the course lists
	*********************************************************/
	protected JLabel masterLabel;
	protected JLabel masterCounter;
	protected JPanel masterCounterPanel;
	protected TitleTipList masterClassList;			//the master course list, with tool tips
	protected JScrollPane masterClassPane;			//scroll bar for the master course list
	protected DefaultListModel masterClassModel;    //list model for the schedule list
	protected JLabel scheduleCourseLabel;
	protected JLabel scheduleCourseCounter;
	protected JPanel scheduleCourseCounterPanel;
	protected TitleTipList scheduleClassList;		//schedule course list, with tool tips
	protected DefaultListModel scheduleClassModel;	//list model for the schedule list
	protected JScrollPane scheduleClassPane;		//scroll pane for the schedule course list
	protected JPanel choosePanel;					//panel for the schedule lists
	protected GroupLayout chooseLayout;				//group layout for the lists
	protected keyListener keys;						//keyboard adapter for registering keyboard events
	protected JCheckBox undergrad;					//checkbox to enable filtering of undergrad courses
	protected JCheckBox gradCampus;					//checkbox to enable filtering of on campus grad courses
	protected JCheckBox gradDist;					//checkbox to enable filtering of off campus grad courses
		
	
	/*********************************************************
	 * 
	*********************************************************/
	protected JPanel selectionPanel;
	protected GroupLayout selectionLayout;
	protected JButton addCourse;
	protected JButton removeCourse;
	protected buttonListener buttons;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected Dimension buttonDim;
	protected JCheckBox autoRefresh;
	protected JCheckBox useClosed;
	protected JCheckBox useAll;
	protected JCheckBox reportingEnabled;
	protected JTextField minUse;
	protected JLabel minUseLbl;
	protected checkListener checker;
	protected JButton refreshSchedules;
	protected mouseListener mouse;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected JTabbedPane view;
	protected Dimension large;
	protected Dimension small;
	protected JLabel downloaded;
	protected JPanel linksDisplay;
	protected JTabbedPane viewInternal;
	protected GroupLayout viewLayout;
	protected DepenTipList linksList;
	protected DefaultListModel linksModel;
	protected JScrollPane linksScroll;
	protected JLabel linked;
	protected JLabel primaryCourses;
	protected JList primaryList;
	protected JScrollPane primaryScroll;
	protected DefaultListModel primaryModel;
	protected JButton removeLink;
	protected JButton removePrimary;
	protected JMenuItem remLink;
	protected JMenuItem remPrimary;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected SectionTipList schedules;
	protected ListListener selected;
	protected DefaultListModel schedulesModel;
	protected JLabel scheduleLabel;
	protected JLabel scheduleCounter;
	protected JPanel scheduleCounterPanel;
	protected JLabel conflictLabel;
	protected JLabel conflictCounter;
	protected JPanel conflictCounterPanel;
	protected JScrollPane schedulesPane;
	protected GroupLayout schedulesLayout;
	protected JPanel schedulesPanel;
	protected JPanel displayPanel;
	protected JPanel displayWrapperPanel;
	protected JScrollPane displayPane;
	protected GridBagLayout displayLayout;
	protected JLabel closedCourse;
	protected JLabel openCourse;
	protected JPanel courseDescription;
	//protected JSlider zoomChooser;
	//protected JXTransformer scalePanel;//ZoomPanel scalePanel;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected JMenuItem addClass;
	protected JMenuItem showDetails;
	protected JMenuItem removeClass;
	protected DetachTabItem detach;
	protected JMenuItem closeThis;
	protected JMenuItem closeOthers;
	protected JMenuItem printDetails;
	protected JMenuItem printVisual;
	protected JMenuItem migrateTab;
	protected JMenuItem flagPrimary;
	protected JMenuItem selectSection;
	protected JMenuItem unflagPrimary;
	protected JPopupMenu contextMenu;
	protected JMenuItem linkCourses;
	protected JMenuItem addCoursesLinked;
	protected JMenuItem addCoursePrimary;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected JPanel sectionPanel;
	protected JScrollPane sectionPanelScroller;
	protected GroupLayout sectionLayout;
	protected TreeMap<String, JPanel> sectionSelectionPanels;
	protected TreeMap<String, boolean[]> sectionSelections;
	protected JPanel sectionPlaceHolder;
	protected TitleTipList sectionList;
	protected DefaultListModel sectionModel;
	protected JScrollPane sectionScroller;	
	protected JPanel numberPanel;
	protected JScrollPane numberScroller;
	protected JPanel sectionWrapper;
	protected TreeMap<String, NumberComboBox> numberSelectionCombos;
	protected TreeMap<String, JPanel> numberSelectionPanels;
	protected TreeMap<String, Integer> numberSelections;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	protected JTabbedPane scheduleDisplay;
	protected JPanel infoPanel;
	protected JPanel infoWrapperPanel;
	protected JScrollPane infoPane;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	private final static int minGap = 10;
	private final static int listWidth = 100;
	private final static int sizeOffset = 440;//440
	private final static int hourVal = 60;
	private final static double scaleColor = 1.2;
	private final static double noScale = 1.0;
	private final static int scale = 1;
	@SuppressWarnings("unused")private double zoomInit = 1.0;//.75; //for later once scaling events are fixed
	@SuppressWarnings("unused")private final static double zoomMin = .5;
	@SuppressWarnings("unused")private final static double zoomMax = 1.0;
	private final static int conflictZOrder = 0;
	@SuppressWarnings("unused")private final static int classZOrder = 1;
	@SuppressWarnings("unused")private final static int backgroundZOrder = 2;
		
	private boolean enabled = true;
	
	private SortedMap<Integer, Component> basicComp;
	private SortedMap<Integer, GridBagConstraints> basicCont;
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public MakeSchedule(){
		super(new BorderLayout());
		SwingWorker<Void, Void> help = new SwingWorker<Void,Void>(){
			@Override
			public Void doInBackground(){
				MakeSchedule.this.buildBasic();
				return null;
			}
			
			@Override
			public void done(){
				MakeSchedule.this.setBasic();
			}
		};
		Main.threadExec.execute(help);
		
		dependancy = new LinkedCourses();
		
		primary = new ArrayList<String>();
		
		mouse = new mouseListener();
		selected = new ListListener();
		keys = new keyListener();
		checker = new checkListener();
		
		masterClassModel = new DefaultListModel();
		
		masterClassList = new TitleTipList(masterClassModel);
		masterClassList.setSelectionMode(
			ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		masterClassList.setDragEnabled(true);
		masterClassList.setTransferHandler(new ListTransferHandler());
		masterClassList.addMouseListener(mouse);
		masterClassList.addListSelectionListener(selected);
		masterClassList.addKeyListener(keys);
		masterClassPane = new JScrollPane(masterClassList);
		
		masterLabel = new JLabel("Master: ");
		masterCounter = new JLabel("0");
		masterCounterPanel = new JPanel();
		masterCounterPanel.add(masterLabel);
		masterCounterPanel.add(masterCounter);
		
		scheduleClassModel = new DefaultListModel();
		
		scheduleClassList = new TitleTipList(scheduleClassModel);
		scheduleClassList.setSelectionMode(
			ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scheduleClassList.setDropMode(DropMode.ON_OR_INSERT);
		scheduleClassList.setTransferHandler(new ListTransferHandler());
		scheduleClassList.addMouseListener(mouse);
		scheduleClassList.addKeyListener(keys);
		scheduleClassList.addListSelectionListener(selected);
		scheduleClassModel = new DefaultListModel();
		scheduleClassPane = new JScrollPane(scheduleClassList);
		scheduleClassList.setCellRenderer(new FlaggedCellRenderer());
		
		scheduleCourseLabel = new JLabel("Schedule: ");
		scheduleCourseCounter = new JLabel("0");
		scheduleCourseCounterPanel = new JPanel();
		scheduleCourseCounterPanel.add(scheduleCourseLabel);
		scheduleCourseCounterPanel.add(scheduleCourseCounter);
		
		undergrad = new JCheckBox("Undergrad");
		undergrad.setToolTipText("Show undergraduate courses in the master course list");
		undergrad.setSelected(Main.prefs.isDownloadUGrad());
		undergrad.setEnabled(Main.prefs.isDownloadUGrad());
		undergrad.addActionListener(checker);
		
		gradCampus = new JCheckBox("On Campus");
		gradCampus.setToolTipText("Show on-campus graduate courses in the master course list");
		gradCampus.setSelected(Main.prefs.isDownloadGrad());
		gradCampus.setEnabled(Main.prefs.isDownloadGrad());
		gradCampus.addActionListener(checker);
		
		gradDist = new JCheckBox("Off Campus");
		gradDist.setToolTipText("Show distance learning graduate courses in the master course list");
		gradDist.setSelected(Main.prefs.isDownloadGradDist());
		gradDist.setEnabled(Main.prefs.isDownloadGradDist());
		gradDist.addActionListener(checker);
				
		buttons = new buttonListener();

		addCourse = new JButton("Add Course  >>");
		addCourse.addActionListener(buttons);
		removeCourse = new JButton("<<  Remove Course");
		removeCourse.addActionListener(buttons);
		refreshSchedules = new JButton("Refresh Schedules");
		refreshSchedules.addActionListener(buttons);
		
		autoRefresh = new JCheckBox("Auto-refresh Schedules");
		autoRefresh.setSelected(true);
		useClosed = new JCheckBox("Allow Closed Courses");
		useClosed.addActionListener(checker);
		useAll = new JCheckBox("Use all specified courses");
		useAll.addActionListener(checker);
		minUse = new JTextField();
		minUseLbl = new JLabel("Minimum number of courses: ");
		useAll.setSelected(true);
		minUse.setEnabled(false);
		minUse.setText("4");
		minUse.setHorizontalAlignment(JTextField.CENTER);
		minUse.addKeyListener(keys);
		minUseLbl.setEnabled(false);
		minUseLbl.setLabelFor(minUse);
		reportingEnabled = new JCheckBox("Report Conflicts");
		reportingEnabled.setSelected(false);
		reportingEnabled.addActionListener(checker);
		
		buttonDim = new Dimension(143, 30);
		addCourse.setMinimumSize(buttonDim);
		removeCourse.setMinimumSize(buttonDim);
		downloaded = new JLabel();
		
		selectionPanel = new JPanel();
		selectionLayout = new GroupLayout(selectionPanel);
		
		selectionPanel.setLayout(selectionLayout);
				
		selectionLayout.setHorizontalGroup(selectionLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(downloaded)
			.addGroup(selectionLayout.createSequentialGroup()
				.addGap(minGap)
				.addGroup(selectionLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(addCourse)
					.addComponent(removeCourse)
				)
				.addGap(minGap)
			)
		);
		
		selectionLayout.setVerticalGroup(selectionLayout.createSequentialGroup()
			.addComponent(addCourse)
			.addGap(minGap)
			.addComponent(removeCourse)
			.addGap(minGap)
			.addComponent(downloaded)
		);
		
		choosePanel = new JPanel();
		chooseLayout = new GroupLayout(choosePanel);
		
		choosePanel.setLayout(chooseLayout);
		
		chooseLayout.setHorizontalGroup(chooseLayout.createSequentialGroup()
			.addGap(minGap)
			.addGroup(chooseLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(chooseLayout.createSequentialGroup()
					.addGroup(chooseLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(masterCounterPanel, listWidth, listWidth, listWidth)
						.addComponent(masterClassPane, listWidth, listWidth, listWidth)
					)
					.addGap(minGap)
					.addGroup(chooseLayout.createParallelGroup()
						.addComponent(scheduleCourseCounterPanel, listWidth, listWidth, listWidth)
						.addComponent(scheduleClassPane, listWidth, listWidth, listWidth)
						.addComponent(undergrad, listWidth, listWidth, listWidth)
						.addComponent(gradCampus, listWidth, listWidth, listWidth)
						.addComponent(gradDist, listWidth, listWidth, listWidth)
					)	
				)
				.addComponent(selectionPanel)
			)
			.addGap(minGap)
		);
		
		chooseLayout.setVerticalGroup(chooseLayout.createSequentialGroup()
			.addGroup(chooseLayout.createParallelGroup()
				.addGroup(chooseLayout.createSequentialGroup()
					.addComponent(masterCounterPanel, 3 * minGap, 3 * minGap, 3 * minGap)
					.addComponent(masterClassPane)
				)
				.addGroup(chooseLayout.createSequentialGroup()
					.addComponent(scheduleCourseCounterPanel, 3 * minGap, 3 * minGap, 3 * minGap)
					.addComponent(scheduleClassPane)
					.addGap(minGap)
					.addComponent(undergrad)
					.addComponent(gradCampus)
					.addComponent(gradDist)
				)
			)
			.addGap(minGap)
			.addComponent(selectionPanel)
			.addGap(minGap)
		);
		
		linksDisplay = new JPanel();
		
		linked = new JLabel("Linked Courses:");
		primaryCourses = new JLabel("Primary Courses:");
		linked.setEnabled(false);
		primaryCourses.setEnabled(false);
		
		primaryList = new TitleTipList(new DefaultListModel());
		primaryList.setCellRenderer(new CenteredCellRenderer());
		primaryList.addKeyListener(keys);
		primaryList.addMouseListener(mouse);
		primaryList.addListSelectionListener(selected);
		primaryScroll = new JScrollPane(primaryList);
		primaryModel = new DefaultListModel();
		
		linksList = new DepenTipList(dependancy.toArray());
		linksList.setCellRenderer(new CenteredCellRenderer());
		linksList.addKeyListener(keys);
		linksList.addMouseListener(mouse);
		linksList.addListSelectionListener(selected);
		linksModel = new DefaultListModel();
		linksScroll = new JScrollPane(linksList);
		
		removeLink = new JButton("Remove Course Link");
		removePrimary = new JButton("Remove Primary Course");
		removeLink.addActionListener(buttons);
		removePrimary.addActionListener(buttons);
		
		viewLayout = new GroupLayout(linksDisplay);
		linksDisplay.setLayout(viewLayout);
		
		viewLayout.setHorizontalGroup(viewLayout.createSequentialGroup()
			.addGap(2 * minGap)
			.addGroup(viewLayout.createParallelGroup()
				.addComponent(linked)
				.addGroup(viewLayout.createSequentialGroup()
					.addGap(minGap)
					.addGroup(viewLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(linksScroll, 2 * listWidth + minGap, 2 * listWidth + minGap, 2 * listWidth + minGap)
						.addComponent(primaryScroll, 2 * listWidth + minGap, 2 * listWidth + minGap, 2 * listWidth + minGap)
						.addComponent(removeLink)
						.addComponent(removePrimary)
					)					
				)
				.addComponent(primaryCourses)
				
			)
			
			.addGap(3 * minGap)
		);
		
		viewLayout.setVerticalGroup(viewLayout.createSequentialGroup()
			.addGap(minGap)
			.addComponent(linked)
			.addComponent(linksScroll)
			.addGap(minGap)
			.addComponent(removeLink)
			.addGap(2 * minGap)
			.addComponent(primaryCourses)
			.addComponent(primaryScroll)
			.addGap(minGap)
			.addComponent(removePrimary)
			.addGap(minGap)
		);
		
		sectionPanel = new JPanel();
		sectionLayout = new GroupLayout(sectionPanel);
		sectionPanel.setLayout(sectionLayout);
		sectionSelectionPanels = new TreeMap<String, JPanel>();
		sectionSelections = new TreeMap<String, boolean[]>();
		sectionPlaceHolder = new JPanel();
		sectionPanelScroller = new JScrollPane(sectionPlaceHolder);
		sectionModel = new DefaultListModel();
		sectionList = new TitleTipList(sectionModel);
		sectionScroller = new JScrollPane(sectionList);
		
		numberSelectionCombos = new TreeMap<String, NumberComboBox>();
		numberSelectionPanels = new TreeMap<String, JPanel>();
		numberSelections = new TreeMap<String, Integer>();
		numberPanel = new JPanel(new GridLayout(0,2));
		numberScroller = new JScrollPane(numberPanel);
		sectionWrapper = new JPanel(new GridLayout(2,1));
		
		sectionList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event){
				showSelectedSection();
			}
		});
		
		sectionLayout.setHorizontalGroup(sectionLayout.createParallelGroup()
			.addGroup(sectionLayout.createSequentialGroup()
				.addGap(1 * minGap)
				.addComponent(sectionScroller, listWidth, listWidth, listWidth)
				.addGap(minGap)
				.addComponent(sectionPanelScroller)
				.addGap(minGap)
			)
		);
		sectionLayout.setVerticalGroup(sectionLayout.createSequentialGroup()
			.addGap(minGap)
			.addGroup(sectionLayout.createParallelGroup()
				.addComponent(sectionScroller)
				.addComponent(sectionPanelScroller)
			)
			.addGap(minGap)
		);
		
		sectionWrapper.add(sectionPanel);//, BorderLayout.CENTER);
		sectionWrapper.add(numberScroller);//, BorderLayout.SOUTH);
				
		viewInternal = new TippedTabbedPane();
		viewInternal.addTab("Choose Courses", choosePanel);
		viewInternal.addTab("Dependancies", linksDisplay);
		viewInternal.addTab("Set Sections", sectionWrapper);
		
		view = new JTabbedPane(JTabbedPane.RIGHT);
		view.addTab("<<", viewInternal);
		
		view.addMouseListener(mouse);
		view.setToolTipText("Click to minimize the choose courses pane.");
		large = view.getSize();
		small = new Dimension(50,large.height);
		
		schedules = new SectionTipList(new DefaultListModel());
		schedules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		schedules.addListSelectionListener(selected);
		schedules.setCellRenderer(new ClosedCellRenderer());
		schedules.addMouseListener(mouse);
		schedulesModel = new DefaultListModel();
		schedulesPane = new JScrollPane(schedules);
		
		scheduleCounter = new JLabel("0");
		scheduleLabel = new JLabel("Schedules: ");
		scheduleCounterPanel = new JPanel();
		scheduleCounterPanel.add(scheduleLabel);
		scheduleCounterPanel.add(scheduleCounter);
		
		conflictCounter = new JLabel("0");
		conflictLabel = new JLabel("Conflicts: ");
		conflictCounterPanel = new JPanel();
		conflictCounterPanel.add(conflictLabel);
		conflictCounterPanel.add(conflictCounter);
		
		schedulesPanel = new JPanel();
		schedulesLayout = new GroupLayout(schedulesPanel);
		schedulesPanel.setLayout(schedulesLayout);
		
		displayLayout = new GridBagLayout();
		displayLayout.rowHeights = new int[(20*hourVal - sizeOffset)];
		Arrays.fill(displayLayout.rowHeights, 1);
		displayPanel = new JPanel(displayLayout);
		displayPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		closedCourse = new JLabel("Closed Course");
		closedCourse.setForeground(Color.RED.darker());
		
		openCourse = new JLabel("Open Course");
		
		courseDescription = new JPanel();
		courseDescription.add(closedCourse);
		courseDescription.add(openCourse);
		
		
		//scalePanel = new ZoomPanel(zoomInit, displayPanel);
		//scalePanel = new JXTransformer(displayPanel);
		
		//scalePanel.add(displayPanel);
		displayWrapperPanel = new JPanel();
		//displayWrapperPanel.add(scalePanel);
		displayWrapperPanel.add(displayPanel);
		//displayWrapperPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		displayPane = new JScrollPane(displayWrapperPanel);
		displayPane.getVerticalScrollBar().setUnitIncrement(
			displayPane.getVerticalScrollBar().getUnitIncrement() * 64);
		//zoomChooser = new JSlider(JSlider.HORIZONTAL, (int)(zoomMin * 100), (int)(zoomMax * 100), (int)(zoomInit * 100));
		
		//zoomChooser.setVisible(false);
		
		/*zoomChooser.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent event) {
				JSlider source = (JSlider)event.getSource();
				double scale = (double)(source.getValue())/100;
				if(!source.getValueIsAdjusting()){
					//try {
						//scalePanel.setZoom(scale);
						scalePanel.setTransform(getTransform(scale));
					//} catch (PropertyVetoException e){
					//	e.printStackTrace();
					//}
					scalePanel.invalidate();
					scalePanel.repaint();
					
					JScrollBar temp = displayPane.getHorizontalScrollBar();
					temp.setValue((temp.getMaximum() + temp.getMinimum())/2);
				}
			}
			
		});*/
		
		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		infoPanel.setLayout(new GridLayout(0, 3));
		
		setInfo((Schedule)null);
		
		infoWrapperPanel = new JPanel(new GridLayout(0,1));
		infoWrapperPanel.add(infoPanel);
		infoWrapperPanel.setBackground(Color.white);
		infoWrapperPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		infoPane = new JScrollPane(infoWrapperPanel);
		infoPane.getVerticalScrollBar().setUnitIncrement(
			infoPane.getVerticalScrollBar().getUnitIncrement() * 64);
		
		scheduleDisplay = new JTabbedPane();
		scheduleDisplay.addTab("View Schedule", displayPane);
		scheduleDisplay.addTab("View Details", infoPane);
		scheduleDisplay.addMouseListener(mouse);
		
		schedulesLayout.setHorizontalGroup(schedulesLayout.createSequentialGroup()
			.addGap(minGap)
			.addGroup(schedulesLayout.createParallelGroup()
				.addGroup(schedulesLayout.createSequentialGroup()
					.addGap(minGap)
					.addComponent(scheduleCounterPanel, (int)(1.42 * listWidth), (int)(1.42 * listWidth), (int)(1.42 * listWidth))
				)
				.addGroup(schedulesLayout.createSequentialGroup()
					.addGap(minGap)
					.addComponent(conflictCounterPanel, (int)(1.42 * listWidth), (int)(1.42 * listWidth), (int)(1.42 * listWidth))
				)
				.addGroup(schedulesLayout.createSequentialGroup()
					.addGap(minGap)
					.addComponent(schedulesPane, (int)(1.42 * listWidth), (int)(1.42 * listWidth), (int)(1.42 * listWidth))
				)
				.addGroup(schedulesLayout.createSequentialGroup()
					.addGap(minGap)
					.addComponent(refreshSchedules, (int)(1.42 * listWidth), (int)(1.42 * listWidth), (int)(1.42 * listWidth))
				)
				.addComponent(autoRefresh)
				.addComponent(useClosed)
				.addComponent(reportingEnabled)
				.addComponent(useAll)
				.addComponent(minUseLbl)
				.addGroup(schedulesLayout.createSequentialGroup()
					.addGap(3 * minGap)
					.addComponent(minUse, 3 * minGap, 3 * minGap, 3 * minGap)
				)
			)
			.addGap(minGap)
			.addGroup(schedulesLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(scheduleDisplay)
				//.addComponent(zoomChooser, 100, 100, 100)
				.addComponent(courseDescription)
			)
			.addGap(minGap)
		);
		
		schedulesLayout.setVerticalGroup(schedulesLayout.createSequentialGroup()
			.addGroup(schedulesLayout.createParallelGroup()
				.addGroup(schedulesLayout.createSequentialGroup()
					.addComponent(scheduleCounterPanel, 2 * minGap, 2 * minGap, 2 * minGap)
					.addComponent(conflictCounterPanel, 2 * minGap, 2 * minGap, 2 * minGap)
					.addComponent(schedulesPane)
					.addGap(minGap)
					.addComponent(refreshSchedules)
					.addGap(minGap)
					.addComponent(autoRefresh)
					.addGap(minGap)
					.addComponent(useClosed)
					.addGap(minGap)
					.addComponent(reportingEnabled)
					.addGap(minGap)
					.addComponent(useAll)
					.addGap(minGap)
					.addComponent(minUseLbl)
					.addGap(minGap)
					.addComponent(minUse, 3 * minGap, 3 * minGap, 3 * minGap)
				)
				.addGroup(schedulesLayout.createSequentialGroup()
					.addComponent(scheduleDisplay)
					//.addComponent(zoomChooser)
					.addComponent(courseDescription)
				)
			)
			.addGap(minGap)
		);
		
		schedulesPanel.setBorder(BorderFactory.createTitledBorder("Schedules Display"));
		
		addClass = new JMenuItem("Add course to schedule");
		addClass.addActionListener(buttons);
		
		removeClass = new JMenuItem("Remove course from schedule");
		removeClass.addActionListener(buttons);
		
		showDetails = new JMenuItem("Show course details");
		showDetails.addActionListener(buttons);
		
		detach = new DetachTabItem(this);
		detach.addActionListener(buttons);
		
		closeThis = new JMenuItem("Close This Tab");
		closeThis.addActionListener(buttons);
		
		closeOthers = new JMenuItem("Close Other Tabs");
		closeOthers.addActionListener(buttons);
		
		printDetails = new JMenuItem("Print Details");
		printDetails.addActionListener(buttons);
		
		printVisual = new JMenuItem("Print Schedule");
		printVisual.addActionListener(buttons);
		
		migrateTab = new JMenuItem("Migrate Tab");
		migrateTab.addActionListener(buttons);
		
		flagPrimary = new JMenuItem("Flag course as primary");
		flagPrimary.addActionListener(buttons);
		
		selectSection = new JMenuItem("Select allowed sections");
		selectSection.addActionListener(buttons);
		
		unflagPrimary = new JMenuItem("Remove primary flag");
		unflagPrimary.addActionListener(buttons);
		
		linkCourses = new JMenuItem("Link courses together");
		linkCourses.addActionListener(buttons);
		
		addCoursesLinked = new JMenuItem("Add courses as linked");
		addCoursesLinked.addActionListener(buttons);
		
		addCoursePrimary = new JMenuItem("Add course as primary");
		addCoursePrimary.addActionListener(buttons);
		
		remLink = new JMenuItem("Remove course link");
		remLink.addActionListener(buttons);
		
		remPrimary = new JMenuItem("Remove primary course");
		remPrimary.addActionListener(buttons);
		
		contextMenu = new JPopupMenu();
		contextMenu.add(printVisual);
		contextMenu.add(printDetails);
		contextMenu.add(new JSeparator());
		//contextMenu.add(migrateTab);
		//contextMenu.add(detach);
		contextMenu.add(new JSeparator());
		contextMenu.add(closeThis);
		contextMenu.add(closeOthers);
		
		while(!help.isDone()){}
		
		setDatabase(Main.terms.get(Main.prefs.getCurrentTerm()), true, true);
				
		owner = Main.master;
		
		addCourse.setEnabled(false);
		removeCourse.setEnabled(false);
		
		add(view, BorderLayout.WEST);
		add(schedulesPanel, BorderLayout.CENTER);
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void fixScheduleTypes(){
		Vector<Object> temp = new Vector<Object>();
		TreeMap<String, Integer> back = numberSelections;
		
		for(Object course: scheduleClassModel.toArray()){
			if(masterClassModel.contains(course)){
				temp.add(course);
			}
		}
		
		numberSelections = new TreeMap<String, Integer>();
		numberPanel.removeAll();
		scheduleClassModel.removeAllElements();
		sectionModel.removeAllElements();
		
		for(Object course: temp){
			scheduleClassModel.addElement(course);
			sectionModel.addElement(course);
			numberSelections.put((String)course, back.get(course));
			numberPanel.add(numberSelectionPanels.get(course));
		}
		
		scheduleClassList.setModel(scheduleClassModel);
		sectionList.setModel(sectionModel);
		showSelectedSection();		
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void fixMasterTypes(CourseType toShow, Database items){
		for(String course: items.getCourseList(toShow)){
			masterClassModel.addElement(course);
		}
		
		masterClassList.setModel(masterClassModel);
		updateMasterCount();
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void setDatabase(Database items, boolean ignorePopups, boolean allowBuild){
		local = items;
		
		if(local != null){
			undergrad.setEnabled(local.isUndergrad());
			gradCampus.setEnabled(local.isGradCampus());
			gradDist.setEnabled(local.isGradDist());
			undergrad.setSelected(local.isUndergrad());
			gradCampus.setSelected(local.isGradCampus());
			gradDist.setSelected(local.isGradDist());
					
			fillCourses(local);
			
			setDate();
		
			if(allowBuild){
				updateSchedules(ignorePopups);
			}
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public void setDate(){
		try{
			downloaded.setText("Downloaded: " + local.getCreation().getTime().toString());
		}
		catch(NullPointerException ex){
			downloaded.setText("Not Yet Downloaded");
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public void fillCourses(Database items){
		if (items != null){
			masterClassModel.clear();
			
			CourseType toShow = CourseType.getCourseType(undergrad.isSelected(), gradCampus.isSelected(), gradDist.isSelected());
			
			fixMasterTypes(toShow, items);
			fixScheduleTypes();
			fixPrimaryTypes();
			fixDependancyTypes();
		}
	}


	/*********************************************************
	 * 
	*********************************************************/
	@Override
	public void setEnabled(boolean enable){
		super.setEnabled(enable);
		this.enabled = enable;
		masterClassList.setEnabled(enable);
		masterClassPane.setEnabled(enable);
		scheduleClassList.setEnabled(enable);
		scheduleClassPane.setEnabled(enable);
		selectionPanel.setEnabled(enable);
		addCourse.setEnabled(enable);
		removeCourse.setEnabled(enable);
		choosePanel.setEnabled(enable);
		linksDisplay.setEnabled(enable);
		linksScroll.setEnabled(enable);
		linksList.setEnabled(enable);
		viewInternal.setEnabled(enable);
		useClosed.setEnabled(enable);
		useAll.setEnabled(enable);
		reportingEnabled.setEnabled(enable);
		
		if(enable){
			view.addMouseListener(mouse);
		}
		else{
			view.removeMouseListener(mouse);
		}
			
		view.setEnabled(enable);
		
		if(useAll.isSelected()){
			minUse.setEnabled(false);
		}
		else{
			minUse.setEnabled(enable);
		}
		
		for(String item: sectionSelectionPanels.keySet()){
			JPanel panel = sectionSelectionPanels.get(item);
			
			for(Component temp: panel.getComponents()){
				temp.setEnabled(enable);
			}				
		}
		
		sectionList.setEnabled(enable);
		
		linked.setEnabled(enable);
		primaryCourses.setEnabled(enable);
		primaryList.setEnabled(enable);
		primaryScroll.setEnabled(enable);
		removeLink.setEnabled(enable);
		removePrimary.setEnabled(enable);
		
		undergrad.setEnabled(enable);
		gradCampus.setEnabled(enable);
		gradDist.setEnabled(enable);
		
		refreshSchedules.setEnabled(enable);
		autoRefresh.setEnabled(enable);
		schedules.setEnabled(enable);
		schedulesPane.setEnabled(enable);
		schedulesPanel.setEnabled(enable);
		displayPanel.setEnabled(enable);
		displayPane.setEnabled(enable);
		
		flagPrimary.setEnabled(enable);
		unflagPrimary.setEnabled(enable);
		selectSection.setEnabled(enable);
		addClass.setEnabled(enable);
		showDetails.setEnabled(enable);
		removeClass.setEnabled(enable);
		detach.setEnabled(enable);
		closeThis.setEnabled(enable);
		closeOthers.setEnabled(enable);
		printDetails.setEnabled(enable);
		printVisual.setEnabled(enable);
		
		scheduleDisplay.setEnabled(enable);
		infoPanel.setEnabled(enable);
		infoPane.setEnabled(enable);
		
		int pos = Main.master.tabControl.indexOfComponent(this);
		
		try{
			Main.master.tabControl.getTabComponentAt(pos).setEnabled(enable);
		}
		catch(Exception ex){}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public void updateSchedules(boolean ignorePopups){
		Main.master.setModified(MakeSchedule.this, true);
		local = Main.terms.get(local.getTerm());
		Object[] items = scheduleClassModel.toArray();
		String[] courses = new String[items.length];
		Object[] temp;
		
		try {
			int sectionsRequested = 0;
			
			for(String key: numberSelections.keySet()){
				sectionsRequested += numberSelections.get(key).intValue();
			}
			
			int use = useAll.isSelected() ? sectionsRequested : Integer.parseInt(minUse.getText());
			

			int max = items.length;
			if(!useAll.isSelected()){
				if(max < use){
					minUse.setText(Integer.toString(max));
					use = max;
				}
			}
			else{
				minUse.setText(Integer.toString(max));
				use = max;
			}
			
			if(items.length != 0){
				for(int index = 0; index < items.length; index++){
					courses[index] = (String)(items[index]);
				}
				
				setEnabled(false);
						
				buildSchedules(courses, primary, useClosed.isSelected(), 
					use, this, !ignorePopups, CourseType.getCourseType(undergrad.isSelected(), gradCampus.isSelected(), gradDist.isSelected()), sectionSelections, numberSelections, reportingEnabled.isSelected());
			}
			else{
				schedulesModel = new DefaultListModel();
				temp = new Object[1];
				schedules.setListData(temp);
				updateScheduleCount();
				conditionalDisable();
				showSchedule((Schedule)null);
			}
			refreshSchedules.setForeground(null);
		} 
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(Main.master, 
				"Invalid entry for minimum number of courses.", 
				"Invalid Number",
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void updateScheduleCount(){
		int sched = 0, conf = 0;
		
		for(Object item: schedulesModel.toArray()){
			if(item instanceof Schedule) sched++;
			else if(item instanceof Conflict) conf++;
		}
		
		scheduleCounter.setText(Integer.toString(sched));
		conflictCounter.setText(Integer.toString(conf));
	}
	
	public void updateMasterCount(){
		masterCounter.setText(Integer.toString(masterClassModel.getSize()));
	}
	
	public void updateScheduleCourseCount(){
		scheduleCourseCounter.setText(Integer.toString(scheduleClassModel.getSize()));
	}
	

	/********************************************************
	 * @purpose Update the schedules display
	*********************************************************/
	public void buildSchedules(String[] classes, ArrayList<String> primary, boolean allowClosed, int useMin, MakeSchedule owner, boolean allowPopup, CourseType type, TreeMap<String, boolean[]> allowedSections, TreeMap<String, Integer> numberSelections, boolean reportingEnabled){
		BuildScheduleThread worker = new BuildScheduleThread();
		worker.setAllowClosed(allowClosed);
		worker.setClasses(classes);
		worker.setPrimary(primary);
		worker.setUseMin(useMin);
		worker.setOwner(owner);
		worker.setType(type);
		worker.setAllowPopup(allowPopup);
		worker.setAllowedSections(allowedSections);
		worker.setNumberSelections(numberSelections);
		worker.setReportingEnabled(reportingEnabled);
		Main.threadExec.execute(worker);
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void conditionalDisable(){
		if(masterClassList.getSelectedIndices().length == 0){
			addCourse.setEnabled(false);
		}
		if(scheduleClassList.getSelectedIndices().length == 0){
			removeCourse.setEnabled(false);
		}
		if(linksList.getSelectedIndices().length == 0){
			removeLink.setEnabled(false);
		}
		if(primaryList.getSelectedIndices().length == 0){
			removePrimary.setEnabled(false);
		}
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void maybeUpdateSchedules(){
		if(autoRefresh.isSelected()){
			updateSchedules(false);
		}
		else{
			refreshSchedules.setForeground(Color.RED);
		}
	}

	
	@SuppressWarnings("boxing")
	public void setBasic(){
		for(int item: basicComp.keySet()){
			displayPanel.add(basicComp.get(item), basicCont.get(item));
		}
	}
	
	/*********************************************************
	 * 
	*********************************************************/
	@SuppressWarnings("boxing")
	public void buildBasic(){
		basicComp =  Collections.synchronizedSortedMap(new TreeMap<Integer, Component>());
		basicCont =  Collections.synchronizedSortedMap(new TreeMap<Integer, GridBagConstraints>());
		
		int scaledHour = hourVal * scale;
		int scaledDay = 1 * scale;
		int zeroScale = 0 * scale;
		int halfHourScale = (hourVal/2) * scale;
		int count = 0;
		
		final String emptyString = "<html><pre>                       <br><br><br>                       </pre></html>";
		
		for(Day item: Day.values()){
			
			for (int hour = 8; hour < 20; hour++) {
				JLabel empty = new JLabel();
				empty.setText(emptyString);
				
				if(hour % 2 == 0){
					empty.setBackground(Color.white);
					empty.setOpaque(true);
				}
				empty.setForeground(empty.getBackground());				
				empty.setHorizontalAlignment(SwingConstants.CENTER);
				empty.setVerticalAlignment(SwingConstants.CENTER);
				empty.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
				
								
				GridBagConstraints daily = new GridBagConstraints();
				daily.gridy = (hour * hourVal - sizeOffset) * scale;
				daily.gridx = (item.value() + 1) * scale;
				daily.gridwidth = scaledDay;
				daily.gridheight = scaledHour;
				daily.fill = GridBagConstraints.BOTH;
				
				try{
					basicComp.put(count, empty);
					basicCont.put(count++, daily);
				} catch(ClassCastException ex){
					logger.error("Unable to build display constraints", ex);
				}
			}
			
			JLabel empty = new JLabel();
			empty.setText(item.toString());
			empty.setOpaque(false);
			empty.setHorizontalAlignment(SwingConstants.CENTER);
			empty.setVerticalAlignment(SwingConstants.BOTTOM);
			
			GridBagConstraints daily = new GridBagConstraints();
			daily.gridy = zeroScale;
			daily.gridx = (item.value() + 1) * scale;
			daily.gridwidth = scaledDay;
			daily.gridheight = halfHourScale;
			daily.fill = GridBagConstraints.BOTH;
			
			basicComp.put(count, empty);
			basicCont.put(count++, daily);
		}
		
		for(int hour = 8; hour < 20; hour++){
			JLabel empty = new JLabel();
			String text;
			
			if (hour == 12){
				text = new String(hour + " pm");
			}
			else if(hour < 12){
				text = new  String(hour + "am");
			}
			else {
				text = new String(hour-12 + "pm");
			}
			
			empty.setText(text);
			empty.setOpaque(false);
			empty.setHorizontalAlignment(SwingConstants.CENTER);
			empty.setVerticalAlignment(SwingConstants.NORTH);
			
			GridBagConstraints daily = new GridBagConstraints();
			daily.gridy = (hour * hourVal - sizeOffset) * scale;
			daily.gridx = zeroScale;
			daily.gridwidth = scaledDay;
			daily.gridheight = scaledHour;
			daily.fill = GridBagConstraints.BOTH;
			
			basicComp.put(count, empty);
			basicCont.put(count++, daily);
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public Color getColor(String course, String section){
		String ident;
		Scanner parse = new Scanner(course);
		
		ident = parse.next();
		
		Color temp = CourseColor.getColor(ident);
		
		if(section.length() != 2){
			float[] comps = temp.getColorComponents(null);
			
			comps[0] *= (comps[0] == noScale) ? noScale : scaleColor;
			comps[1] *= (comps[1] == noScale) ? noScale : scaleColor;
			comps[2] *= (comps[2] == noScale) ? noScale : scaleColor;
			
			temp = new Color(comps[0], comps[1], comps[2]);
			
		}
		return temp;
	}
	
	
	public void setInfo(Object schedule){
		if(schedule == null || schedule instanceof Schedule){
			setInfo((Schedule)schedule, true);
		}
		else if(schedule instanceof Conflict){
			setInfo((Conflict)schedule);
		}	
	}

	private void setInfo(Conflict schedule){
		setInfo(schedule.item, false);
		
		TreeMap<Section,ClassPanel> errs = new TreeMap<Section,ClassPanel>();
		
		
		
		for(Section one: schedule.conflicted.keySet()){
			ClassPanel panel;
			
			if(errs.keySet().contains(one)){
				panel = errs.get(one);
			}
			else{
				panel = getInfoPanel(one);
			}
			
			JPanel inner = (JPanel)panel.getComponent(0);
			JLabel conf = new JLabel();
			
			switch(schedule.conflictedTypes.get(one)){
				case minUse:{
					String sec = one.getDescription();
					conf = new JLabel(schedule.minUse.get(sec) + " [" + Conflict.ConflictType.minUse.toString() + "]");
					break;
				}
				case link:{
					String err[] = schedule.getLinkError(one);
					String links[] = err[0].split(":");
					
					for(int pos = 2; pos < links.length; pos++){
						links[1] += ", " + links[pos];
					}
					
					err[1] = "Conflicts" + err[1].split(" conflicts")[1] + " [" + Conflict.ConflictType.link.toString() + ": " + links[1] +  "]";
					conf = new JLabel(err[1]);
					break;
				}
				case number:{
					String[] err = schedule.getNumberError(one);
					err[0] = err[0].split(" times")[0];
					err[0] = err[0].substring(err[0].length()-2);
					conf = new JLabel(err[1] + " [" + Conflict.ConflictType.number + err[0] + "]");					
					break;
				}
				case primary:{
					String err = schedule.getPrimaryError(one);
					err = "C" + err.split(" it c")[1] + " [" + Conflict.ConflictType.primary + "]";					
					conf = new JLabel(err);					
					break;
				}
			}
			
			Font temp = conf.getFont();
			conf.setFont(temp.deriveFont(Font.BOLD, temp.getSize()-2)); 
			conf.setForeground(Color.red.darker());
			conf.setHorizontalAlignment(SwingConstants.CENTER);
			
			inner.add(conf);
			errs.put(one, panel);
		}
		
		for(Section key: errs.keySet()){
			infoPanel.add(errs.get(key));
		}
		
		
		
		int count = infoPanel.getComponentCount();
		
		for(; count % 3 != 0; count++){
			ClassPanel panel = getInfoPanel(null);
			infoPanel.add(panel);
		}
	}
	
	/*********************************************************
	 * 
	*********************************************************/
	private void setInfo(Schedule schedule, boolean pad){
		infoPanel.removeAll();
		
		int count = 0;
		if (schedule != null){
			for(Section item: schedule.getClassesObj()){
				ClassPanel panel = getInfoPanel(item);
				panel.addMouseListener(mouse);
				
				infoPanel.add(panel);
				count++;
			}
			if(pad){
				for(; count % 3 != 0; count++){
					ClassPanel panel = getInfoPanel(null);
					infoPanel.add(panel);
				}
			}
		}
		else{
			for(count = 0; count < 6; count++){
				ClassPanel panel = getInfoPanel(null);
				infoPanel.add(panel);
			}
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public ClassPanel getInfoPanel(Section section){
		ClassPanel outer = new ClassPanel(new BorderLayout());
		JPanel inner = new JPanel(new GridLayout(14,1));

		inner.setOpaque(true);
		inner.setBackground(Color.white);
		
		if (section != null){
			inner.add(new JLabel(Integer.toString(section.getCRN()), JLabel.CENTER));
			inner.add(new JLabel("<html><strong>" + section.getCourseID() + " " + section.getSection() + "</strong></html>", JLabel.CENTER));
			inner.add(new JLabel("<html><strong>" + section.getTitle() + "</strong></html>", JLabel.CENTER));
			if (section.getNotes().length() != 0){
				inner.add(new JLabel("<html><font color=\"red\">" + section.getNotes(), JLabel.CENTER));
			}
			inner.add(new JLabel("Course Rating: " + Double.toString(Math.round(section.getRating() * 10)/10.0), JLabel.CENTER));
			inner.add(new JLabel(section.getInstructor().getName(), JLabel.CENTER));
			if (!section.getInstructor().getName().equals("STAFF") && section.getInstructor().getRating() != 0.0){
				inner.add(new JLabel("Professor Rating: " + Double.toString(Math.round(section.getInstructor().getRating() * 10)/10.0), JLabel.CENTER));
			}
			inner.add(new JLabel(section.getCredit() + " Credits", JLabel.CENTER));
			inner.add(new JLabel(section.getDaysStr() + " " + section.getPeriodStr(), JLabel.CENTER));
			inner.add(new JLabel(section.getLocation(), JLabel.CENTER));
			
			JLabel seats = new JLabel(section.isClosed() ? "Course Closed" : "Open Seats: " + section.getSeats(), JLabel.CENTER);
			seats.setForeground(section.isClosed() ? Color.RED : seats.getForeground());
			inner.add(seats);
			
			if(section.hasSecondary()){
				inner.add(new JLabel(section.getSecDaysStr() + " " + section.getSecPeriodStr(), JLabel.CENTER));
				inner.add(new JLabel(section.getSecLocation(), JLabel.CENTER));
			}
		}
		else {
			for(int count = 0; count < 12; count++){
				inner.add(new JLabel("                                         "));
			}
		}
		outer.add(inner);
		
		return outer;
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private void showSchedule(Schedule sched){
		displayPanel.removeAll();
				
		if (sched != null){			
			Section[] sections = sched.getClassesObj().toArray(new Section[0]);
			
			for (Section section: sections){
				int[] define = new int[2];
				GridBagConstraints[] places = getConstraints(section, define);
				
				int current = 0;
				for(GridBagConstraints item: places){
					boolean secondary = (current >= define[0]); 
					ClassPanel holder = new ClassPanel(section, secondary);
					
					if(section.hasValidTime(secondary)){
						displayPanel.add(holder, item);
					}
					current++;
				}
			}
		}
		
		selectedSchedule = schedules.getSelectedIndex();
		
		setBasic();
		
		scheduleDisplay.validate();
		scheduleDisplay.repaint();
		Main.master.validate();
	}
	
	private void showSchedule(Conflict conf){
		showSchedule(conf.item);
		
		for(Section miss: conf.conflicted.keySet()){
			Section in = conf.conflicted.get(miss);
						
			Section conflict = Section.getConflictAsSection(miss, in);
			
			int[] define = new int[2];
			GridBagConstraints[] places = getConstraints(conflict, define);
			
			
			int current = 0;
			for(GridBagConstraints item: places){
				boolean secondary = (current >= define[0]); 
				ConflictPanel holder = new ConflictPanel(conflict, miss, in);
								
				if(miss.hasValidTime(secondary)){
					displayPanel.add(holder, item);
				}
				current++;
			}			
		}
		
		setZOrders();
	}
	
	public void setZOrders(){
		for(Component one: displayPanel.getComponents()){
			if(one instanceof ConflictPanel){
				displayPanel.setComponentZOrder(one, conflictZOrder);
			}
		}
	}
	
	public void showSchedule(Object item){
		if(item == null || item instanceof Schedule){
			showSchedule((Schedule)item);//display schedule
		}
		else if(item instanceof Conflict){
			showSchedule((Conflict)item);
		}
		
		setInfo(item);
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void addLinkedCourses(String[] courses){
		dependancy.addLinkedCourses(courses);
		
		linksModel = new DefaultListModel();
		
		for(CourseList item: dependancy){
			linksModel.addElement(item);
		}
		
		linksList.setModel(linksModel);
		
		maybeUpdateSchedules();
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void displayLinkedCourses(){
		linksModel.removeAllElements();

		for(CourseList item: dependancy){
			linksModel.addElement(item);
		}
		
		linksList.setModel(linksModel);
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void displayPrimaryCourses(boolean emptyFirst){
		if(emptyFirst){
			primaryModel.removeAllElements();
		}
		
		for(String item: primary){
			if(!primaryModel.contains(item)){
				primaryModel.addElement(item);
			}
		}
		
		primaryList.setModel(primaryModel);
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public void fixPrimaryTypes(){
		Vector<Object> temp = new Vector<Object>();
		
		for(Object item: primary){			
			if(masterClassModel.contains(item)){
				temp.add(item);
			}
		}
		
		primary.clear();
		
		for(Object item: temp){
			primary.add((String)item);
		}
		
		displayPrimaryCourses(true);
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void fixDependancyTypes(){
		Vector<String> toRemove = new Vector<String>();
		
		for(CourseList item: dependancy.links){
			for(String temp: item){
				if(!scheduleClassModel.contains(temp)){
					toRemove.add(temp);
				}
			}
		}
		
		for(String item: toRemove){
			for(CourseList found: dependancy.getLinks(item)){
				dependancy.remove(item, found);
			}
		}
		
		displayLinkedCourses();
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public GridBagConstraints[] getConstraints(Section item, int[] define){
		int numDays = 0;
		int regDays = 0;
		boolean[] days = item.getDaysBool();
		boolean[] secDays = item.getSecDaysBool();
		
		for(boolean day: days){
			if (day){
				numDays++;
			}
		}
		
		regDays = numDays;
		
		if(item.hasSecondary()){
						
			for(boolean day: secDays){
				if (day){
					numDays++;
				}
			}
		}
		
		define[0] = regDays;
		define[1] = numDays;
		
		GridBagConstraints[] results = new GridBagConstraints[numDays];
		int start = item.getPeriodPer().getStartTime().getHour();
		boolean am = item.getPeriodPer().getStartTime().getAm();
		
		start = (!am) ? (start == 12) ? start : start + 12 : start;
		start *= hourVal;
		start += item.getPeriodPer().getStartTime().getMinute();
		int duration = (int)item.getPeriodPer().getDurationMin();
		
		int secStart = item.getSecPeriodPer().getStartTime().getHour();
		boolean secAm = item.getSecPeriodPer().getStartTime().getAm();
		
		secStart = (!secAm) ? (secStart == 12) ? secStart : secStart + 12 : secStart;
		secStart *= hourVal;
		secStart += item.getSecPeriodPer().getStartTime().getMinute();
		int secDuration = (int)item.getSecPeriodPer().getDurationMin();
		
		int current = 0;
		int secCurrent = regDays;
		for(Day curr: Day.values()){
			
			boolean day = days[curr.value()];
			if (day){
				results[current] = new GridBagConstraints();
				results[current].gridx = (curr.value() + 1) * scale;
				results[current].gridy = (start - sizeOffset) * scale;
				results[current].gridwidth = 1 * scale;
				results[current].gridheight = duration * scale;
				results[current].fill = GridBagConstraints.BOTH;
				
				current++;
			}
			
			if(item.hasSecondary()){
				boolean secDay = secDays[curr.value()];
				if (secDay){
					results[secCurrent] = new GridBagConstraints();
					results[secCurrent].gridx = (curr.value() + 1) * scale;
					results[secCurrent].gridy = (secStart - sizeOffset) * scale;
					results[secCurrent].gridwidth = 1 * scale;
					results[secCurrent].gridheight = secDuration * scale;
					results[secCurrent].fill = GridBagConstraints.BOTH;
					
					secCurrent++;
				}	
			}
		}
		
		return results;
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public void toggleViewSize(){
		if (view.getTitleAt(0).compareTo("<<") != Compare.equal.value()){
			choosePanel.setMinimumSize(null);
			choosePanel.setMaximumSize(null);
			choosePanel.setPreferredSize(null);
			linksDisplay.setMinimumSize(null);
			linksDisplay.setMaximumSize(null);
			linksDisplay.setPreferredSize(null);
			viewInternal.setMinimumSize(null);
			viewInternal.setMaximumSize(null);
			viewInternal.setPreferredSize(null);			
			view.setTitleAt(0, "<<");
			view.setToolTipText("Click to minimize the choose courses pane.");
		}
		else{
			Dimension size = new Dimension(0,30);
			choosePanel.setMinimumSize(size);				
			choosePanel.setMaximumSize(size);
			choosePanel.setPreferredSize(size);
			linksDisplay.setMinimumSize(size);				
			linksDisplay.setMaximumSize(size);
			linksDisplay.setPreferredSize(size);
			viewInternal.setMinimumSize(size);				
			viewInternal.setMaximumSize(size);
			viewInternal.setPreferredSize(size);
			view.setTitleAt(0, ">>");
			view.setToolTipText("Click to maximize the choose courses pane.");
		}		
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	public boolean atLeastOneSelected(){
		return undergrad.isSelected() || gradCampus.isSelected() || gradDist.isSelected();
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public void showContextMenu(Component item, int x, int y){
		if(item.equals(masterClassList)){
			JPopupMenu show = new JPopupMenu();
			
			int index  = masterClassList.locationToIndex(new Point(x,y));

			show.add(addClass);
			show.add(addCoursePrimary);
			show.add(addCoursesLinked);
			//show.add(showDetails);
			
			addClass.setEnabled(this.enabled);
			addCoursePrimary.setEnabled(this.enabled);
			addCoursesLinked.setEnabled(this.enabled);
			showDetails.setEnabled(this.enabled);
			
			if (!masterClassList.isSelectedIndex(index)){
				masterClassList.setSelectedIndex(index);
			}
	
			if (masterClassList.getSelectedIndices().length != 1){
				showDetails.setEnabled(false);
				addClass.setText("Add courses to schedule");
				addCoursePrimary.setText("Add courses as primary");
			}
			else{
				addClass.setText("Add course to schedule");
				addCoursePrimary.setText("Add course as primary");
				addCoursesLinked.setEnabled(false);
			}
			
			if (masterClassList.getSelectedIndices().length < 1){
				addClass.setEnabled(false);
				addCoursePrimary.setEnabled(false);
				addCoursesLinked.setEnabled(false);
			}
			
			show.show(item, x, y);
		}
		else if(item.equals(scheduleClassList)){
			JPopupMenu show = new JPopupMenu();
			
			int index  = scheduleClassList.locationToIndex(new Point(x,y));

			show.add(removeClass);
			//show.add(showDetails);
			show.add(selectSection);
			show.add(flagPrimary);
			show.add(unflagPrimary);
			show.add(linkCourses);
			
			removeClass.setEnabled(this.enabled);
			showDetails.setEnabled(this.enabled);
			flagPrimary.setEnabled(this.enabled);
			selectSection.setEnabled(this.enabled);
			unflagPrimary.setEnabled(this.enabled);
			linkCourses.setEnabled(this.enabled);
			
			if (!scheduleClassList.isSelectedIndex(index)){
				scheduleClassList.setSelectedIndex(index);
			}
	
			if (scheduleClassList.getSelectedIndices().length != 1){
				showDetails.setEnabled(false);
				removeClass.setText("Remove courses from schedule");
				flagPrimary.setText("Flag courses as primary");
				unflagPrimary.setText("Remove primary flags from courses");
				selectSection.setEnabled(false);
			}
			else{
				removeClass.setText("Remove course from schedule");
				flagPrimary.setText("Flag course as primary");
				unflagPrimary.setText("Remove primary flag from course");
			}
			
			if (scheduleClassList.getSelectedIndices().length < 1){
				removeClass.setEnabled(false);
				flagPrimary.setEnabled(false);
				unflagPrimary.setEnabled(false);
				selectSection.setEnabled(false);
			}
			else if(scheduleClassList.getSelectedIndices().length < 2){
				linkCourses.setEnabled(false);
			}
			
			Object[] values = scheduleClassList.getSelectedValues();
			
			if(primary.containsAll(Arrays.asList(values))){
				flagPrimary.setEnabled(false);
			}
			
			boolean shouldEnable = false;
			
			for(Object one: values){
				shouldEnable |= primary.contains(one);
			}
			unflagPrimary.setEnabled(unflagPrimary.isEnabled() && shouldEnable);
			
			show.show(item, x, y);
		}
		else if(item.equals(scheduleDisplay)){
			JPopupMenu show = new JPopupMenu();
			
			try{
				if(scheduleDisplay.getComponentAt(scheduleDisplay.indexAtLocation(x, y))
						.equals(scheduleDisplay.getComponentAt(0))){
					show.add(printVisual);
					
					if(schedulesModel.size() != 0){
						show.show(item, x, y);
					}
				}
			
				if(scheduleDisplay.getComponentAt(scheduleDisplay.indexAtLocation(x, y))
						.equals(scheduleDisplay.getComponentAt(1))){
					show.add(printDetails);
					
					if(schedulesModel.size() != 0){
						show.show(item, x, y);
					}
				}
			}
			catch(IndexOutOfBoundsException ex){/*do nothing if bad index*/}
		}
		else if(item.equals(schedules)){
			JPopupMenu show = new JPopupMenu();
			
			show.add(printVisual);
			show.add(printDetails);
			
			schedules.setSelectedIndex(schedules.locationToIndex(new Point(x, y)));
			
			if(schedulesModel.size() != 0){
				show.show(item, x, y);
			}
		}
		else if(item.equals(linksList)){
			JPopupMenu show = new JPopupMenu();
			
			show.add(remLink);

			int index  = linksList.locationToIndex(new Point(x,y));
			
			if (!linksList.isSelectedIndex(index)){
				linksList.setSelectedIndex(index);
			}
			
			remLink.setEnabled(linksList.getSelectedIndices().length > 0);
			
			show.show(item, x, y);
		}
		else if(item.equals(primaryList)){
			JPopupMenu show = new JPopupMenu();
			
			show.add(remPrimary);
			
			int index  = primaryList.locationToIndex(new Point(x,y));
			
			if (!primaryList.isSelectedIndex(index)){
				primaryList.setSelectedIndex(index);
			}
						
			remPrimary.setEnabled(primaryList.getSelectedIndices().length > 0);
			
			show.show(item, x, y);
		}
		else if(item instanceof ClassPanel){
			((ClassPanel)item).showContextMenu(x, y);
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class buttonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();
			
			if(source.equals(addCourse) || source.equals(addClass)){
				addCourseAction(event);
			}
			else if(source.equals(addCoursesLinked)){
				addCoursesLinked(event);
			}
			else if(source.equals(addCoursePrimary)){
				addCoursesPrimary(event);
			}
			else if(source.equals(removeCourse) || source.equals(removeClass)){
				removeCourseAction(event);
			}
			else if(source.equals(refreshSchedules)){
				updateSchedules(false);
			}
			else if(source.equals(closeThis)){
				Main.master.tabControl.remove(view.getParent());
			}
			else if(source.equals(closeOthers)){
				for(Component comp: Main.master.tabControl.getComponents()){
					Tab item;
					try{
						item = (Tab)comp;

						if(view.getParent() != item){
							Main.master.tabControl.remove(comp);
						}	
					}
					catch(ClassCastException ex){}				
				}
			}
			else if(source.equals(detach)){
				Component comp = detach.getOwner();
				MakeSchedule mkSched = (MakeSchedule)comp;
				
				Popup item = new Popup(mkSched, Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(comp)));
				
				owner = item;
				Main.master.tabControl.remove(mkSched);
				Main.master.detached.add(item);
				
				item.setVisible(true);
			}
			else if(source.equals(printDetails)){
				PrintUtilities.printComponent(infoPanel, Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(MakeSchedule.this)) + " - Details");
			}
			else if(source.equals(printVisual)){
				PrintUtilities.printComponent(displayPanel,  Main.master.tabControl.getTitleAt(Main.master.tabControl.indexOfComponent(MakeSchedule.this)) + " - Display");
			}
			else if(source.equals(flagPrimary)){
				for(Object item: scheduleClassList.getSelectedValues()){
					if(!primary.contains(item)){
						primary.add((String)item);
					}
				}
				scheduleClassList.repaint();
				
				displayPrimaryCourses(false);

				maybeUpdateSchedules();
			}
			else if(source.equals(unflagPrimary)){
				for(Object item: scheduleClassList.getSelectedValues()){
					primary.remove(item);
				}
				scheduleClassList.repaint();
				
				displayPrimaryCourses(false);

				maybeUpdateSchedules();
			}
			else if(source.equals(linkCourses)){
				String [] link = new String[scheduleClassList.getSelectedIndices().length];
				int pos = 0;
				
				for(Object item: scheduleClassList.getSelectedValues()){
					link[pos++] = (String)item;
				}
				
				addLinkedCourses(link);
			}
			else if(source.equals(removeLink) || source.equals(remLink)){
				for(Object item: linksList.getSelectedValues()){
					dependancy.remove((CourseList)item);
				}
				
				displayLinkedCourses();

				maybeUpdateSchedules();
			}
			else if(source.equals(removePrimary) || source.equals(remPrimary)){
				for(Object item: primaryList.getSelectedValues()){
					primary.remove(item);
				}
				
				displayPrimaryCourses(true);

				maybeUpdateSchedules();
			}
			else if(source.equals(migrateTab)){
				
				
				
			}
			else if(source.equals(selectSection)){
				String item = (String)scheduleClassList.getSelectedValue();
				viewInternal.setSelectedIndex(viewInternal.indexOfComponent(sectionPanelScroller));
				sectionList.setSelectedValue(item, true);
			}
		}
		
		public void addCoursesLinked(ActionEvent event){
			boolean back = autoRefresh.isSelected();
			
			autoRefresh.setSelected(false);
			
			Object[] selected = masterClassList.getSelectedValues();
			addCourse.doClick();
			
			int[] indices = new int[selected.length];
			
			for(int pos = 0; pos < selected.length; pos++){
				indices[pos] = scheduleClassModel.indexOf(selected[pos]);
			}
			scheduleClassList.setSelectedIndices(indices);
			
			autoRefresh.setSelected(back);
			
			linkCourses.doClick();
		}
		
		public void addCoursesPrimary(ActionEvent event){
			boolean back = autoRefresh.isSelected();
			autoRefresh.setSelected(false);
			
			Object[] items = masterClassList.getSelectedValues();
			
			addCourse.doClick();
			
			int[] select = new int[items.length];
			
			for(int pos = 0; pos < items.length; pos++){
				select[pos] = scheduleClassModel.indexOf(items[pos]);
			}
			
			scheduleClassList.setSelectedIndices(select);
			
			autoRefresh.setSelected(back);
			flagPrimary.doClick();
		}
		
		
		public void addCourseAction(ActionEvent event){
			for(Object item: masterClassList.getSelectedValues()){
				if(!scheduleClassModel.contains(item)){
					scheduleClassModel.addElement(item);
					
					addSectionPanel((String)item);
					addNumberPanel((String)item);
				}
				else{
					JOptionPane.showMessageDialog(Main.master,
						"The course \"" + item + "\" has already been added"
						+ " to this schedule.", "Course Not Added",
						JOptionPane.INFORMATION_MESSAGE);
				}
			}
			Object[] temp = scheduleClassModel.toArray();
			Arrays.sort(temp);
			
			scheduleClassModel.clear();
			for(Object item: temp){
				scheduleClassModel.addElement(item);
			}
						
			scheduleClassList.setModel(scheduleClassModel);
			
			maybeUpdateSchedules();
			
			int[] clear = new int[]{masterClassList.getModel().getSize() + 1};
			masterClassList.setSelectedIndices(clear);
			
			sectionList.setModel(sectionModel);
			sectionList.setSelectedIndex(0);
			
			updateScheduleCourseCount();
			showSelectedSection();
		}
		
		
		public void removeCourseAction(ActionEvent event){
			for(Object item: scheduleClassList.getSelectedValues()){
				scheduleClassModel.removeElement(item);
				primary.remove(item);
				removeSectionPanel((String)item);
				removeNumberPanel((String)item);
			}
			Object[] temp = scheduleClassModel.toArray();
			Arrays.sort(temp);
			
			scheduleClassModel.clear();
			
			for(Object item: temp){
				scheduleClassModel.addElement(item);
			}
			
			scheduleClassList.setModel(scheduleClassModel);
			
			fixPrimaryTypes();
			fixDependancyTypes();
			
			if(autoRefresh.isSelected()){
				updateSchedules(false);
			}
			else{
				refreshSchedules.setForeground(Color.RED);
			}
			
			sectionList.setModel(sectionModel);
			sectionList.setSelectedIndex(0);
			
			updateScheduleCourseCount();
			showSelectedSection();
		}
		
	}
	
	public void addNumberPanel(String course){
		Course item = local.getCourse(course);
		JPanel pane = new JPanel();
		
		JLabel tempLabel = new JLabel(item.getPerceivedCourse());
		
		String[] values = new String[item.getNumOfSections()];
		
		for(int pos = 1; pos <= item.getNumOfSections(); pos++){
			values[pos-1] = Integer.toString(pos);
		}
		
		NumberComboBox tempCombo = new NumberComboBox(course, values);
		
		tempCombo.setToolTipText("Choose the number of sections of " + item.getPerceivedCourse() + " required");
		tempLabel.setToolTipText(item.getTitle());
		pane.add(tempLabel);
		pane.add(tempCombo);
		
		numberSelectionCombos.put(course, tempCombo);
		numberSelectionPanels.put(course, pane);
		
		numberPanel.add(pane);
		numberPanel.invalidate();
	}
	
	public void addSectionPanel(String course){
		Course item = local.getCourse(course);
		JPanel pane = new JPanel(new GridLayout(0,1));
		Section[] sections = item.getSectionsSObj();
		boolean[] flags = new boolean[sections.length];
				
		sectionSelections.put(course, flags);
		
		for(int pos = 0; pos < sections.length; pos++){
			Section one = sections[pos];
			SectionBox toAllow = new SectionBox(one.getCourseID() + " " + one.getSection(), course);
			toAllow.setToolTipText("<html><p>" + one.getDaysStr() + "<br>" + one.getPeriodStr() + "<br>" + one.getLocation() + "<br>" + one.getInstructor().toString() + (one.hasSecondary() ? 
				"<br>" + one.getSecDaysStr() + "<br>" + one.getSecPeriodStr() + "<br>" + one.getSecLocation() : ""));
			toAllow.setSelected(true);
			flags[pos] = true;
			toAllow.setPos(pos);
									
			pane.add(toAllow);
		}
		
		sectionSelectionPanels.put(course, pane);
		sectionModel.addElement(course);		
		sectionPlaceHolder.add(pane);
	}
	
	public void removeNumberPanel(String course){	
		JPanel removed = numberSelectionPanels.get(course);
		numberSelectionPanels.remove(course);
		numberPanel.remove(removed);
	}
	
	public void removeSectionPanel(String course){	
		sectionModel.removeElement(course);
		sectionSelectionPanels.remove(course);
		sectionSelections.remove(course);
	}

	public void setSectionSelections(TreeMap<String, boolean[]> newSelections){
		sectionSelections = newSelections;
		
		for(String key: sectionSelectionPanels.keySet()){
			JPanel panel = sectionSelectionPanels.get(key);
			
			for(Component one: panel.getComponents()){
				((SectionBox)one).resetValues();
			}		
		}
	}
	
	public void setNumberSelections(TreeMap<String, Integer> newSelections){
		numberSelections.putAll(newSelections);
		
		for(String key: numberSelectionCombos.keySet()){
			numberSelectionCombos.get(key).resetValues();
		}
	}

	public void showSelectedSection(){
		try{
			sectionPlaceHolder.removeAll();
			
			Object value = sectionList.getSelectedValue();
			
			if(value == null){
				value = sectionModel.firstElement();
			}
			
			JPanel panel = sectionSelectionPanels.get(value);
			
			boolean[] vals = sectionSelections.get(value);
			
			for(Component item: panel.getComponents()){
				SectionBox temp = (SectionBox)item;
				
				temp.setSelected(vals[temp.getPos()]);
			}
			
			sectionPlaceHolder.add(panel);
		}
		catch(NoSuchElementException ex){
			
		}
		finally{
			sectionPanelScroller.validate();
			sectionPlaceHolder.validate();
			sectionPlaceHolder.repaint();
		}
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	private class checkListener implements ActionListener{
		

		/*********************************************************
		 * 
		*********************************************************/
		public void actionPerformed(ActionEvent event){
			Object source = event.getSource();
			
			if (source.equals(useClosed)){
				maybeUpdateSchedules();
			}
			else if(source.equals(useAll)){
				minUse.setEnabled(!useAll.isSelected());
				minUseLbl.setEnabled(!useAll.isSelected());
				
				maybeUpdateSchedules();
			}
			else if(source.equals(reportingEnabled)){
				maybeUpdateSchedules();
			}
			else if(source.equals(undergrad)){
				if(!atLeastOneSelected()){
					undergrad.setSelected(true);
				}
				fillCourses(local);
				maybeUpdateSchedules();
			}
			else if(source.equals(gradCampus)){
				if(!atLeastOneSelected()){
					gradCampus.setSelected(true);
				}
				fillCourses(local);
				maybeUpdateSchedules();
			}
			else if(source.equals(gradDist)){
				if(!atLeastOneSelected()){
					gradDist.setSelected(true);
				}
				fillCourses(local);
				maybeUpdateSchedules();
			}
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class mouseListener implements MouseListener{
		private MouseEvent last;

		
		/*********************************************************
		 * 
		*********************************************************/
		public void mouseClicked(MouseEvent event){
			Object source = event.getSource();
			if (event.getButton() == MouseEvent.BUTTON1){
				if(event.getClickCount() == 2){
					if (source.equals(masterClassList)){
						addCourse.doClick();
					}
					else if(source.equals(scheduleClassList)){
						removeCourse.doClick();
					}
					else if(source.equals(view)){
						if(event != last){
							toggleViewSize();
						}
					}
				}
				else if(event.getClickCount() == 1){
					if(source.equals(view)){
						if (view.indexAtLocation(event.getPoint().x, event.getPoint().y) != -1){
							if(event != last){
								toggleViewSize();
							}
						}
					}
					
				}
			}
			last = event;
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		public void mouseEntered(MouseEvent event){}
		

		/*********************************************************
		 * 
		*********************************************************/
		public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }
		

		/*********************************************************
		 * 
		*********************************************************/
		public void mouseExited(MouseEvent event){}
		

		/*********************************************************
		 * 
		*********************************************************/
		public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }
		

		/*********************************************************
		 * 
		*********************************************************/
		private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            showContextMenu(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class keyListener extends KeyAdapter{
		

		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public void keyPressed(KeyEvent event){
			if(event.getSource().equals(scheduleClassList)){
				if(event.getKeyCode() == KeyEvent.VK_DELETE){
					removeCourse.doClick();
				}
			}
			else if(event.getSource().equals(linksList)){
				if(event.getKeyCode() == KeyEvent.VK_DELETE){
					removeLink.doClick();
				}
			}
			else if(event.getSource().equals(primaryList)){
				if(event.getKeyCode() == KeyEvent.VK_DELETE){
					removePrimary.doClick();
				}
			}
			else if(event.getSource().equals(minUse)){
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					maybeUpdateSchedules();
				}
			}
			else if(event.getSource().equals(masterClassList)){
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					addCourse.doClick();
				}
			}
		}
	}

	/*********************************************************
	 * 
	*********************************************************/
	private class DetachTabItem extends JMenuItem{
		

		/*********************************************************
		 * 
		*********************************************************/
		private MakeSchedule itemOwner;
		static final long serialVersionUID = 267000000;
		

		/*********************************************************
		 * 
		*********************************************************/
		public DetachTabItem(MakeSchedule owner){
			super("Detach Item");
			this.itemOwner = owner;
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		public MakeSchedule getOwner(){
			return itemOwner;
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public class TitleTipList extends JList{
		static final long serialVersionUID = 265000000;
		

		public TitleTipList(ListModel model){
			super(model);
		}
		
		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public String getToolTipText(MouseEvent event) {
			Point p = event.getPoint();
		    int location = locationToIndex(p);
		    ListModel model = getModel();
		    
		    try{
			    String item = ((String)model.getElementAt(location));
			    if (!(item.compareTo("No Data Model") == Compare.equal.value())){
			    	try{
			    		return local.getCourse(item).getTitle();
			    	}
			    	catch (NullPointerException ex){}
			    }
		    }
		    catch(ArrayIndexOutOfBoundsException ex){}
	    return null;
	    }
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class ListListener implements ListSelectionListener{
		

		/*********************************************************
		 * 
		*********************************************************/
		public void valueChanged(ListSelectionEvent event){
			if(event.getSource().equals(schedules)){
				showSchedule(schedules.getSelectedValue());
			}
			else if(event.getSource().equals(masterClassList)){
				addCourse.setEnabled(masterClassList.getSelectedIndices().length > 0);
			}
			else if(event.getSource().equals(scheduleClassList)){
				removeCourse.setEnabled(scheduleClassList.getSelectedIndices().length > 0);
			}
			else if(event.getSource().equals(linksList)){
				removeLink.setEnabled(linksList.getSelectedIndices().length > 0);
			}
			else if(event.getSource().equals(primaryList)){
				removePrimary.setEnabled(primaryList.getSelectedIndices().length > 0);
			}
		}
	}


	/*********************************************************
	 * 
	*********************************************************/
	public class ClassPanel extends JPanel{
		

		/*********************************************************
		 * 
		*********************************************************/
		static final long serialVersionUID = 266000000;


		public ClassPanel(Section item, boolean secondary){
			super(new BorderLayout());
			addMouseListener(mouse);
			
			JPanel inner = new JPanel(new GridLayout(6,1));
			
			Color back = getColor(item.getCourseID(), item.getSection());
			
			for(int pos = 0; pos <=5; pos++){
				String text = new String();
				
				switch(pos){
					case 0:{
						text = Integer.toString(item.getCRN());
						break;
					}
					case 1:{
						text = item.getCourseID() + " " + item.getSection();
						break;
					}
					case 2:{
						text = item.getTitle();
						break;
					}
					case 3:{
						text = item.getInstructor().toString();
						break;
					}
					case 4:{
						text = (!secondary) ? item.getLocation() : item.getSecLocation();
						break;
					}
					case 5:{
						text = (!secondary) ? item.getPeriodStr() : item.getSecPeriodStr();
						break;
					}
				}
				
				JLabel row = new JLabel(text);
				
				row.setVerticalAlignment(SwingConstants.CENTER);
				row.setHorizontalAlignment(SwingConstants.CENTER);
							
				row.setForeground(item.isClosed() ? Color.red.darker() : Color.black);
							
				//row.setMaximumSize(new Dimension(100,100));
				inner.add(row);
				
				inner.setBackground(back);
			}
			
			add(inner, BorderLayout.CENTER);
			setBorder(BorderFactory.createLineBorder(
				item.isClosed() ? Color.red.darker() : Color.black));
			
			setToolTipText("<html>" + item.getTitle() + "<BR>" + 
				"<font color=\"red\">" + item.getNotes() + "</font>" +
				(item.getNotes() == null || item.getNotes().compareTo("") == Compare.equal.value() 
					? "" : "<BR>") + item.getCredit() + " Credit Hours<BR>" +  
				(item.getRating() == 0.0 ? "" : "Course Rating: " + Math.round(item.getRating() * 10)/10.0 + "<BR>") +
				(item.getInstructor().getRating() == 0.0 ? "" : "Professor Rating: " + Math.round(item.getInstructor().getRating() * 10)/10.0));
		}
		
		/*********************************************************
		 * 
		*********************************************************/
		public ClassPanel(LayoutManager layout){
			super(layout);
			addMouseListener(mouse);
			
			/*addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					scalePanel.getZoomListen().mouseClicked(arg0);
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					scalePanel.getZoomListen().mouseEntered(arg0);
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					scalePanel.getZoomListen().mouseExited(arg0);
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
					scalePanel.getZoomListen().mousePressed(arg0);
				}
				@Override
				public void mouseReleased(MouseEvent arg0) {
					scalePanel.getZoomListen().mouseReleased(arg0);
				}
			});*/
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		public void selectCourse(){
			Component first = this.getComponent(0);
			JPanel holder = (JPanel) first;
			Component second = holder.getComponent(1);
			JLabel label = (JLabel)second;
			
			String detail = label.getText();
			
			Scanner parse = new Scanner(detail);
			
			String course = parse.next() + " " + parse.next();
			
			Section temp = new Section();
			temp.setCourseID(course);
			temp.setSection(detail);
						
			String perceivedCourse = temp.getPerceivedCourse();
			
			scheduleClassList.setSelectedValue(perceivedCourse, true);			
		}
		
		 public void showContextMenu(int x, int y){
			JPopupMenu show = new JPopupMenu();
			
			show.add(removeClass);
			
			removeClass.setEnabled(MakeSchedule.this.enabled);
			
			ClassPanel.this.selectCourse();
			
			show.show(ClassPanel.this, x, y);
	    }
	}
	
	public class ConflictPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public ConflictPanel(Section conf, Section miss, Section in){
			super(new GridLayout(6,1));
			
			setOpaque(true);
			setBackground(Color.red.darker());
			setBorder(BorderFactory.createLineBorder(Color.black));
			
			JLabel first = new JLabel("Conflict between: ");
			JLabel second = new JLabel(in.getCourseID() + " " + in.getSection());
			JLabel third = new JLabel(in.getTitle());
			JLabel fourth = new JLabel(miss.getCourseID() + " " + miss.getSection());
			JLabel fifth = new JLabel(miss.getTitle());
			JLabel sixth = new JLabel(conf.getPeriodStr());
			
			first.setHorizontalAlignment(JLabel.CENTER);
			second.setHorizontalAlignment(JLabel.CENTER);
			third.setHorizontalAlignment(JLabel.CENTER);
			fourth.setHorizontalAlignment(JLabel.CENTER);
			fifth.setHorizontalAlignment(JLabel.CENTER);
			sixth.setHorizontalAlignment(JLabel.CENTER);
			
			add(first);
			add(second);
			add(third);
			add(fourth);
			add(fifth);
			add(sixth);
			
			setToolTipText("<html>" + in.getTitle() + "<BR>" + 
					"<font color=\"red\">" + in.getNotes() + "</font>" +
					(in.getNotes() == null || in.getNotes().compareTo("") == Compare.equal.value() 
					? "" : "<BR>") + in.getCredit() + " Credit Hours<BR>" +  
					(in.getRating() == 0.0 ? "" : "Course Rating: " + Math.round(in.getRating() * 10)/10.0 + "<BR>") +
					(in.getInstructor().getRating() == 0.0 ? "" : "Professor Rating: " + 
					Math.round(in.getInstructor().getRating() * 10)/10.0) + "<br><br>Conflicts with<br><br>" + miss.getTitle() + "<BR>" + 
					"<font color=\"red\">" + miss.getNotes() + "</font>" +
					(miss.getNotes() == null || miss.getNotes().compareTo("") == Compare.equal.value() 
						? "" : "<BR>") + miss.getCredit() + " Credit Hours<BR>" +  
					(miss.getRating() == 0.0 ? "" : "Course Rating: " + Math.round(miss.getRating() * 10)/10.0 + "<BR>") +
					(miss.getInstructor().getRating() == 0.0 ? "" : "Professor Rating: " + Math.round(miss.getInstructor().getRating() * 10)/10.0));
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public JMenuItem getDetach() {
		return detach;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setDetach(DetachTabItem detach) {
		this.detach = detach;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public JMenuItem getCloseThis() {
		return closeThis;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setCloseThis(JMenuItem closeThis) {
		this.closeThis = closeThis;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public JMenuItem getCloseOthers() {
		return closeOthers;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setCloseOthers(JMenuItem closeOthers) {
		this.closeOthers = closeOthers;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public JFrame getOwner() {
		return owner;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setOwner(JFrame owner) {
		this.owner = owner;
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	public Database getDatabase(){
		return this.local;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public JMenuItem getMigrateTab() {
		return migrateTab;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setMigrateTab(JMenuItem migrateTab) {
		this.migrateTab = migrateTab;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public JPopupMenu getContextMenu() {
		return contextMenu;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setContextMenu(JPopupMenu contextMenu) {
		this.contextMenu = contextMenu;
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class FlaggedCellRenderer extends DefaultListCellRenderer{
		public static final long serialVersionUID = 267000000;
		

		/*********************************************************
		 * 
		*********************************************************/
		public FlaggedCellRenderer(){
			setOpaque(true);
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
			
			Component sup = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(primary.contains(value)){
				setForeground(Color.BLUE);
			}
			
			return sup;
		}
	}
	
	
	/*********************************************************
	 * 
	*********************************************************/
	private class ClosedCellRenderer extends DefaultListCellRenderer{
		public static final long serialVersionUID = 1L;
		

		/*********************************************************
		 * 
		*********************************************************/
		public ClosedCellRenderer(){
			setOpaque(true);
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
			
			Component sup = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if(value != null){
				if(value instanceof Schedule){
					if(((Schedule)value).hasClosedCourse()){
						setForeground(Color.RED.darker());
					}
				}
				else if(value instanceof Conflict){
					if(((Conflict)value).hasClosedCourse()){
						setForeground(Color.RED.darker());
					}
				}
			}
			
			return sup;
		}
	}
	

	/*********************************************************
	 * 
	*********************************************************/
	private class CenteredCellRenderer extends DefaultListCellRenderer{
		public static final long serialVersionUID = 268000000;
		

		/*********************************************************
		 * 
		*********************************************************/
		public CenteredCellRenderer(){
			setOpaque(true);
		}
		

		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
			
			JLabel sup = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			sup.setHorizontalAlignment(JLabel.CENTER);
			
			
			if(list.equals(primaryList)){
				try{
					Course found = local.getCourse((String)value);
					sup.setText(sup.getText() + " - " + found.getTitle());
				}
				catch(NullPointerException ex){
				}
			}
			
			return sup;
		}
	}

	
	/*********************************************************
	 * 
	*********************************************************/
	public class DepenTipList extends JList{
		static final long serialVersionUID = 265000000;
		

		/*********************************************************
		 * 
		*********************************************************/
		DepenTipList(Object[] array){
			super(array);
		}
		
		
		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public String getToolTipText(MouseEvent event) {
			Point p = event.getPoint();
		    int location = locationToIndex(p);
		    ListModel model = getModel();
		    
		    String item = model.getElementAt(location).toString();
		    if (!(item.compareTo("No Data Model") == Compare.equal.value())){
		    	Scanner parse = new Scanner(item);
		    	parse.useDelimiter(" :: ");
		    	try{
		    		String text = local.getCourse(parse.next()).getTitle();
		    		
		    		while(parse.hasNext()){
		    			Course found = local.getCourse(parse.next());
		    			text += " :: " + found.getTitle();		    			
		    		}
		    		
		    		return text;
		    	}
		    	catch (NullPointerException ex){}
		    }
	    return null;
	    }
	}


	/*********************************************************
	 * 
	*********************************************************/
	public ArrayList<String> getPrimary() {
		return primary;
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setPrimary(ArrayList<String> primary) {
		this.primary = primary;
		displayPrimaryCourses(true);
	}


	/*********************************************************
	 * 
	*********************************************************/
	public void setDependancy(LinkedCourses dependancy){
		this.dependancy = dependancy;
		try{
			displayLinkedCourses();
		}
		catch(Exception ex){
			this.dependancy = new LinkedCourses();
		}
	}
	
	public class ListTransferHandler extends TransferHandler{

		private static final long serialVersionUID = 269000000L;
		
		private int[] indices = null;
	    @SuppressWarnings("unused")
		private int addIndex = -1; //Location where items were added
	    @SuppressWarnings("unused")
		private int addCount = 0;  //Number of items added.
	            
	    /**
	     * We only support importing strings.
	     */
	    @Override
		public boolean canImport(TransferHandler.TransferSupport info) {
	        // Check for String flavor
	        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            return false;
	        }
	        return true;
	   }

	    /**
	     * Bundle up the selected items in a single list for export.
	     * Each line is separated by a newline.
	     */
	    @Override
		protected Transferable createTransferable(JComponent c) {
	        JList list = (JList)c;
	        indices = list.getSelectedIndices();
	        Object[] values = list.getSelectedValues();
	        
	        StringBuffer buff = new StringBuffer();

	        for (int i = 0; i < values.length; i++) {
	            Object val = values[i];
	            buff.append(val == null ? "" : val.toString());
	            if (i != values.length - 1) {
	                buff.append("\n");
	            }
	        }
	        
	        return new StringSelection(buff.toString());
	    }
	    
	    /**
	     * We support both copy and move actions.
	     */
	    @Override
		public int getSourceActions(JComponent c) {
	        return TransferHandler.COPY;
	    }
	    
	    /**
	     * Perform the actual import.  This demo only supports drag and drop.
	     */
	    @Override
		public boolean importData(TransferHandler.TransferSupport info) {
	        if (!info.isDrop()) {
	            return false;
	        }
	        
	        JList list = (JList)info.getComponent();
	        
	        if(list.equals(scheduleClassList)){
		        DefaultListModel listModel = (DefaultListModel)list.getModel();
		        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
		        int index = dl.getIndex();
		        boolean insert = dl.isInsert();

		        // Get the string that is being dropped.
		        Transferable t = info.getTransferable();
		        String data;
		        try {
		            data = (String)t.getTransferData(DataFlavor.stringFlavor);
		        } 
		        catch (Exception e) { return false; }
		                                
		        // Wherever there is a newline in the incoming data,
		        // break it into a separate item in the list.
		        String[] values = data.split("\n");
		        
		        addIndex = index;
		        addCount = values.length;
		        boolean anyUpdated = false;
		        
		        if(!insert){
                	String tmp = (String)listModel.get(index);
        			removeNumberPanel(tmp);
        			removeSectionPanel(tmp);
		        }
		        
		        // Perform the actual import.  
		        for (int i = 0; i < values.length; i++) {
		            if (insert) {
		            	
		            	//addCourse.doClick();
		            	//anyUpdated = true;
		            	
		            	if(!listModel.contains(values[i])){
		            		listModel.add(index++, values[i]);
		            		addNumberPanel(values[i]);
		            		addSectionPanel(values[i]);
		            		anyUpdated = true;
		            	}
		            } else {
		                // If the items go beyond the end of the current
		                // list, add them in.
		            	if(!listModel.contains(values[i])){
			                //addCourse.doClick();
		            		
		            		if (index < listModel.getSize()) {
		            			listModel.set(index++, values[i]);
			                    addNumberPanel(values[i]);
			            		addSectionPanel(values[i]);
			                } else {
			                    listModel.add(index++, values[i]);
			                    addNumberPanel(values[i]);
			            		addSectionPanel(values[i]);
			                }
			                anyUpdated = true;
		            	}
		            }
		        }
		        
		        if(anyUpdated){
		        	scheduleClassModel = sortList(listModel);
		        	scheduleClassList.setModel(scheduleClassModel);
		        	
		        	sectionModel = sortList(sectionModel);
		        	sectionList.setModel(sectionModel);
		        	
		        	MakeSchedule.this.showSelectedSection();
		        	MakeSchedule.this.updateScheduleCourseCount();
		        	maybeUpdateSchedules();
		        }
		        
		        return true;
	        }
	        return false;
	    }

	    /**
	     * Remove the items moved from the list.
	     */
	    @Override
		protected void exportDone(JComponent c, Transferable data, int action) {
	        JList source = (JList)c;
	        DefaultListModel listModel  = (DefaultListModel)source.getModel();

	        if (action == TransferHandler.MOVE) {
	            for (int i = indices.length - 1; i >= 0; i--) {
	                listModel.remove(indices[i]);
	            }
	        }
	        
	        indices = null;
	        addCount = 0;
	        addIndex = -1;
	    }
	}
	
	
	public class SectionBox extends JCheckBox{
		static final long serialVersionUID = 1L;
		
		private int pos;
		private String course;

		public SectionBox(String value, String course){
			super(value);
			
			this.course = course;
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sectionSelections.get(SectionBox.this.course)[pos] = SectionBox.this.isSelected();
					if(!SectionBox.this.validValues()){
						SectionBox.this.setSelected(true);
					}
					sectionSelections.get(SectionBox.this.course)[pos] = SectionBox.this.isSelected();
					numberSelectionCombos.get(SectionBox.this.getCourse()).validateValues();
					maybeUpdateSchedules();
				}});
		}
		
		public boolean validValues(){
			boolean atLeastOne = false;
			for(boolean item: sectionSelections.get(SectionBox.this.course)){
				atLeastOne |= item;
			}
			
			return atLeastOne;
		}
		
		public void resetValues(){
			setSelected(sectionSelections.get(course)[pos]);
		}

		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public String getCourse() {
			return course;
		}

		public void setCourse(String course) {
			this.course = course;
		}	
	}
	
	public class NumberComboBox extends JComboBox{
		private static final long serialVersionUID = 1L;
		private String course;
		
		@SuppressWarnings("boxing")
		public NumberComboBox(String course, String[] values){
			super(values);
			
			this.course = course;
			setEditable(false);
			addActionListener( new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					NumberComboBox.this.validateValues();
					maybeUpdateSchedules();
				}
			});

			numberSelections.put(course, 1);
		}
		
		@SuppressWarnings("boxing")
		public void validateValues(){
			int selected = new Scanner((String)this.getSelectedItem()).nextInt();
			
			String course = getCourse();
			
			boolean[] enabled = sectionSelections.get(course);
			
			int allowed = 0;
			
			for(boolean once: enabled){
				if(once) allowed++;
			}
			
			if(selected > allowed){
				this.setSelectedIndex(allowed-1);
			}	
			
			selected = new Scanner((String)this.getSelectedItem()).nextInt();
			
			numberSelections.remove(course);
			numberSelections.put(course, selected);
		}
		
		@SuppressWarnings("boxing")
		public void resetValues(){
			setSelectedIndex(numberSelections.get(course) - 1);
		}

		public String getCourse() {
			return course;
		}

		public void setCourse(String course) {
			this.course = course;
		}
	}
	
	public DefaultListModel sortList(DefaultListModel toSort){
		Object[] temp = toSort.toArray();
    	Arrays.sort(temp);
    	
    	DefaultListModel toReturn = new DefaultListModel();
    	for(Object item: temp){
    		toReturn.addElement(item);
    	}
    	
    	return toReturn;
	}
	
	static final String depenString = new String("View or remove variable building dependancies");
	static final String sectionString = new String("Specify or exclude sections or specify number of sections per course");
	static final String courseString = new String("Add or remove courses and create dependancies");
	static final String emptyString = new String("");
	
	protected class TippedTabbedPane extends JTabbedPane{
		
		private static final long serialVersionUID = 1L;

		public TippedTabbedPane(){
			setToolTipText(emptyString);
		}
		
		@Override
		public String getToolTipText(MouseEvent e){			
			
			int index = this.indexAtLocation(e.getX(), e.getY());

			if(index == this.indexOfTab("Dependancies")){
				return depenString;
			}
			else if(index == this.indexOfTab("Set Sections")){
				return sectionString;
			}
			else if(index == this.indexOfTab("Choose Courses")){
				return courseString;
			}
			
			return null;
		}
	}
	
	protected class SectionTipList extends JList{
		static final long serialVersionUID = 265000000;
		

		public SectionTipList(ListModel model){
			super(model);
		}
		
		/*********************************************************
		 * 
		*********************************************************/
		@Override
		public String getToolTipText(MouseEvent event) {
			Point p = event.getPoint();
		    int location = locationToIndex(p);
		    ListModel model = getModel();
		    
		    try {
				Object value = model.getElementAt(location);
				if (value instanceof Schedule) {
					Schedule item = (Schedule) value;

					String toReturn = new String("<html>");

					for (Section one : item.getClassesObj()) {
						boolean closed = one.isClosed();

						if (closed) {
							toReturn += "<font color=\"maroon\">";
						}
						toReturn += one.getCourseID() + " " + one.getSection()
								+ " " + one.getTitle() + " ["
								+ one.getInstructor() + "] ("
								+ one.getPeriodStr() + ")<br>";

						if (closed) {
							toReturn += "</font>";
						}
					}
					toReturn += "</html>";

					return toReturn;
				} else if (value instanceof Conflict) {
					Conflict item = (Conflict) value;

					String toReturn = item.toTipString();

					return toReturn;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				//do nothing
			}
		return null;
	    }
	}
}
