package Scheduler;

import io.devyse.scheduler.analytics.keen.KeenEngine;
import io.devyse.scheduler.logging.Logging;
import io.devyse.scheduler.logging.LoggingUncaughtExceptionHandler;
import io.devyse.scheduler.persist.FlywayEngine;
import io.devyse.scheduler.security.Encryption;
import io.devyse.scheduler.startup.Parameters;
import io.devyse.scheduler.startup.SingleInstanceController;
import io.devyse.scheduler.swing.handlers.DefaultBrowserHyperlinkListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.sql.DataSource;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.XTabComponent;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.beust.jcommander.JCommander;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Properties;


public class Main {
	
	/**
	 * Initialize logging as early as possible
	 */
	static {	
		Logging.initialize();
	}
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(Main.class);
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2014122621520L;//object id
	protected static final long buildNumber = 1582L;//build number
	protected static final String version = new String("4.13.0");
	protected static final int policyVersion = 1;
	
	
	protected static final String author = new String("Mike Reinhold");
	protected static final String maintain = new String("Mike Reinhold");
	protected static final String email = new String("contact@coursescheduler.io");
	protected static final String contributers = new String(
		"Aaron Simmons, Phil DeMonaco, Alex Thomson, Ryan Murphy, Vlad Patryshev, Rob MacGrogan");
	
	protected static final String folderName = new String(System.getProperty("user.home") + "/Scheduler");
	protected static final String dataName = new String("Data");
	protected static final String dataPath = new String(folderName + "/" + dataName);
	protected static final String dataFolder = new String(dataPath + "/");
	protected static final String databaseExt = new String(".sdb");
	protected static final String scheduleExt = new String(".ssf");
	protected static final String preferencesExt = new String(".spf");
	protected static final String smLogo = new String("Images/logo-small.png");
	protected static final String logo = new String("Images/logo.png");
	protected static final String xIconStr = new String("Images/xIcon.png");
	protected static final String xIconPStr = new String("Images/xIconP.png");
	protected static final String xIconRStr = new String("Images/xIconR.png");
	protected static final String rmp = new String("Profs.txt");
	protected static final String fixRMP = new String(dataFolder + rmp);
	protected static final String jarFixRMP = new String(dataName + "/" + rmp);
	protected static InputStream fixRMPFile;
	
	private static final int buffers = 1;
	
	protected static String defURL = new String("https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject");
	protected static String defSID = new String("366");
	
	protected static ClassLoader loader;
	
	protected static Preferences prefs;
	protected static MainFrame master;
	protected static ScheduledThreadPoolExecutor threadExec;
	protected static TreeMap<String, Database> terms;
	protected static boolean termChanged = false;
	protected static ImageIcon icon;// = new ImageIcon(smLogo);
	protected static ImageIcon xlIcon;// = new ImageIcon(logo);
	protected static ImageIcon xIcon;
	protected static ImageIcon xIconP;
	protected static ImageIcon xIconR;
		
	protected static int availProcs = 0;
	protected static String windowSystem;
	protected static String os;
	protected static String jvm;
	
	protected static boolean nimbus = false;
	protected static boolean conflictDebugEnabled = false;
	
	public static final String KEEN_STARTUP = "start";
	public static final String KEEN_DOWNLOAD = "download";
	public static final String KEEN_SCHEDULE = "compute";
	public static final String KEEN_CONFIG = "config";
	
	
	public static void main(String[] args) throws Exception{	
		
		//Register a default uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler());
		
		//Register a SingleInstanceListener to handle secondary invocation
		SingleInstanceController.register();
		
		//process the command line arguments
		Parameters parameters = new Parameters();
		new JCommander(parameters, args);
		
		//make sure that the required SSL/TLS protocols are enabled for use in HTTPS
		Encryption.configureHttpsProtocols(parameters.getHttpsProtocols());
		
		//initialize the database
		
		//TODO change this to jooq initialization which will initialize the data source and flyway in the background
		EmbeddedDataSource source = new EmbeddedDataSource();
		source.setDatabaseName("Data/scheduler.derby");
		source.setCreateDatabase("create");
		source.setConnectionAttributes("upgrade=true");
		new FlywayEngine().initializeDataStore(source);
		
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            nimbus = true;
		            break;
		        }
		    }
		} 
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.warn("Unable to set look and feel", e);
		} 
		
		Properties systemProps = System.getProperties();
		
		windowSystem = systemProps.getProperty("sun.desktop");
		os = systemProps.getProperty("os.name");
		availProcs = Runtime.getRuntime().availableProcessors();
		jvm = System.getProperty("java.vendor") + " " + System.getProperty("java.version");
				
		threadExec = new ScheduledThreadPoolExecutor(32 * availProcs);
		threadExec.setKeepAliveTime(1500, TimeUnit.MILLISECONDS);
		threadExec.allowCoreThreadTimeOut(true);
		
		loader = Main.class.getClassLoader();
		
		Main.prefs = Preferences.load();
		
		if (Main.prefs == null){
			new File(folderName).mkdir();
			new File(dataPath).mkdir();
			Main.prefs = new Preferences();
		}
		prefs.setCurrentTerm(Term.nextTerm());
		
		String current = prefs.getCurrentTerm();
		
		terms = new TreeMap<String, Database>();
		terms.put(current, Database.load(current));

		registerStartupEvent();
		
		try {
			fixRMPFile = loader.getResourceAsStream(jarFixRMP);
						
			icon = new ImageIcon(loader.getResource(smLogo));
			xlIcon = new ImageIcon(loader.getResource(logo));
			xIcon = new ImageIcon(loader.getResource(xIconStr));
			xIconP = new ImageIcon(loader.getResource(xIconPStr));
			xIconR = new ImageIcon(loader.getResource(xIconRStr));
			
			if(icon == null){
				icon = new ImageIcon(smLogo);
			}
			if(xlIcon == null){
				xlIcon = new ImageIcon(logo);
			}
			if(xIcon == null){
				xIcon = new ImageIcon(xIconStr);
			}
			if(xIconP == null){
				xIconP = new ImageIcon(xIconPStr);
			}
			if(xIconR == null){
				xIconR = new ImageIcon(xIconRStr);
			}
		} 
		catch (Exception e){
			icon = new ImageIcon(smLogo);
			xlIcon = new ImageIcon(logo);
			xIcon = new ImageIcon(xIconStr);
			xIconP = new ImageIcon(xIconPStr);
			xIconR = new ImageIcon(xIconRStr);
		}
		
		XTabComponent.setIcons(xIcon, xIconP, xIconR);
		
		master = new MainFrame();
		
		Database database = terms.get(current);
		
		master.setVisible(true);
		master.createBufferStrategy(buffers);
		
		//check if new privacy policy to display
		if(prefs.getPolicyVersion() < policyVersion){
			
			//display the policy
			master.mainMenu.aboutHelpFrame.setVisible(true);
			master.mainMenu.aboutHelpFrame.helpTabMain.setSelectedIndex(2);
			
			JOptionPane.showMessageDialog(master.mainMenu.aboutHelpFrame, "The Course Scheduler has an updated privacy policy! You can view the policy in the About section of the Help menu.", "Updated Privacy Policy", JOptionPane.INFORMATION_MESSAGE);
			
			//update the policy version
			prefs.setPolicyVersion(policyVersion);			
			prefs.save();
		}
		
		//check if user is running an older Java version, in this case 1.7
		logger.info("java.version: {}", System.getProperty("java.version"));
		logger.info("java.vm.specification.version: {}", System.getProperty("java.vm.specification.version"));
		logger.info("java.vm.version: {}", System.getProperty("java.vm.version"));
		logger.info("java.specification.version: {}", System.getProperty("java.specification.version"));
		logger.info("java.class.version: {}", System.getProperty("java.class.version"));
		if(System.getProperty("java.version").startsWith("1.7")){
			showJavaRuntimeWarning();
		}
	
		if (database == null){
			int hresult = JOptionPane.showConfirmDialog(Main.master,
					"You have not yet downloaded course information for " + 
					Term.getTermString(current) + ". Do you want to do so now?", "Download Course Information", 
					JOptionPane.YES_NO_OPTION);
			if (hresult == JOptionPane.YES_OPTION){
				updateDatabase(current);
			}
			else{
				master.mainMenu.newScheduleMenu.setEnabled(false);
			}
		}
				
		//open any schedule files specified at start up
		openScheduleFiles(parameters.getOpenFiles());
	}
	
	private static void registerStartupEvent(){
		if(!Main.prefs.isAnalyticsOptOut()){
			Map<String, Object> startupEvent = new HashMap<>();
			startupEvent.put("startup.type", "end user");
			KeenEngine.getDefaultKeenEngine().registerEvent(KEEN_STARTUP, startupEvent);
		}
	}
	
	public static void printZeroRatedProfs(){
		ArrayList<Prof> profs = new ArrayList<Prof>();
		
		Database current = terms.get(prefs.getCurrentTerm());
		
		for(String key: current.getCourseList(CourseType.all)){
			Course course = current.getCourse(key);
			
			for(Section item: course.getSectionsLl()){
				Prof prof = item.getInstructor();
				
				if(!profs.contains(prof) && prof.getRating() == 0.0){
					profs.add(prof);
				}
			}
		}
		
		//only print out the professors without ratings if debug enabled
		if(logger.isDebugEnabled()){
			for(Prof prof: profs){
				logger.debug("{}: {}", prof.getName(), prof.getRating());
			}		
		}
	}
	
	private static void showJavaRuntimeWarning(){
		// for copying style
	    JLabel label = new JLabel();
	    Font font = label.getFont();

	    // create some css from the label's font
	    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
	    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
	    style.append("font-size:" + font.getSize() + "pt;");
		
	    //build the message
		JEditorPane jreWarning = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
	            + "You are currently running an older version of the Java Runtime Environment (" + System.getProperty("java.version") + "). <br/><br/>"
	            + "Oracle has announced the end of life for JRE 7 in April of 2015, more information can be found at "
	            + "<a href=\"http://www.oracle.com/technetwork/java/javase/downloads/eol-135779.html\">http://www.oracle.com/technetwork/java/javase/downloads/eol-135779.html</a>. <br/><br/>"
	            + "As such, the Course Scheduler will be migrating to Java 8 in an upcoming release. All users are encouraged to " 
				+ "upgrade to Java 8 as soon as possible. You can download the appropriate installer for your system from " 
	            + "<a href=\"http://java.com/en/download/\">Oracle</a>."
				+ "</body></html>");
		
		jreWarning.setEditable(false);
		
		//Nimbus will override manually setting the background color via setBackground(Color) unless told otherwise
		Color bgColor = label.getBackground();
		UIDefaults defaults = new UIDefaults();
		defaults.put("EditorPane[Enabled].backgroundPainter", bgColor);
		jreWarning.putClientProperty("Nimbus.Overrides", defaults);
		jreWarning.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
		jreWarning.setBackground(bgColor);
		
		//add hyperlink listener to handle opening the link
		jreWarning.addHyperlinkListener(new DefaultBrowserHyperlinkListener());
		
		JOptionPane.showMessageDialog(master, 
			jreWarning,
			"Upgrade to Java 8",
			JOptionPane.WARNING_MESSAGE);
	}
	
	
	/********************************************************
	 * @purpose Update the current database
	*********************************************************/
	public static void updateDatabase(String term){
		ParseThread worker = new ParseThread();		//create new parse thread
		worker.setEngine(Parser.ku);				//set the parsing engine
		worker.setTerm(term);						//set term to download
		worker.setUrl(Main.prefs.getURL());			//set the url to download from
				
		Main.threadExec.execute(worker);							//run the thread
	}
	
	
	/********************************************************
	 * @purpose Update the term display
	*********************************************************/
	public static void displayTerm(){
		Main.master.mainMenu.currentTerm.setText(//set current term text
				"Current Term: " + Term.getTermString(
				Main.prefs.getCurrentTerm()) +	MainMenu.mainMenuPad);
	}
	
	
	/********************************************************
	 * @purpose Update the downloaded date display
	*********************************************************/
	public static void displayDate(){
		try{
			Main.master.mainMenu.downloadDate.setText("Downloaded: " + Main.terms.get(
					Main.prefs.getCurrentTerm()).getCreation().getTime().toString());
		}
		catch(NullPointerException ex){
			Main.master.mainMenu.downloadDate.setText("Not Yet Downloaded");
		}
		
	}
	
	
	/********************************************************
	 * @purpose Update all of the make schedule databases
	*********************************************************/
	public static void updateAllForRMP(){
		boolean anyUpdate = false;
		
		for(String item: terms.keySet()){
			if(Integer.parseInt(prefs.getCurrentTerm()) - Integer.parseInt(item) < 4){
				if(!terms.get(item).getProfs().hasRatings()){
					anyUpdate = true;
					updateDatabase(item);
				}
			}
		}
		
		reRateAll();
		displayDate();
		repairDates();
		
		if(!anyUpdate){
			Main.master.setEnabled(true);
		}
	}
	
	
	/********************************************************
	 * @purpose Update all of the make schedule databases
	*********************************************************/
	public static void updateAll(){
		for(String item: terms.keySet()){
			if(Integer.parseInt(prefs.getCurrentTerm()) - Integer.parseInt(item) < 4){
				updateDatabase(item);
			}
		}
		
		reRateAll();
		displayDate();
		repairDates();
	}
	
		
	public static void repairDates(){
		for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){//for each tab
			Component comp = Main.master.tabControl.getComponentAt(pos);//get the component there
			
			try{
				Tab item = (Tab)comp;
				
				item.setDate();
			}
			catch(ClassCastException ex){}
		}
		
		for(int pos = 0; pos < Main.master.detached.size(); pos++){//for each tab
			Popup comp = (Popup)Main.master.detached.get(pos);//get the component there
			
			try{
				Tab item = (Tab)comp.getComponent();
				
				item.setDate();
			}
			catch(ClassCastException ex){}
		}
	}
	
	
	/********************************************************
	 * @purpose Rerate all of the stored databases and update schedules
	*********************************************************/
	public static void reRateAll(){
		for(String item: terms.keySet()){
			terms.get(item).reRate();
		}
		
		for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){//for each tab
			Component comp = Main.master.tabControl.getComponentAt(pos);//get the component there
			
			try{
				Tab item = (Tab)comp;
				
				item.setDatabase(terms.get(item.getDatabase().getTerm()), true, true);
			}
			catch(ClassCastException ex){}
		}
		
		for(int pos = 0; pos < Main.master.detached.size(); pos++){//for each tab
			Popup comp = (Popup)Main.master.detached.get(pos);//get the component there
			
			try{
				Tab item = (Tab)comp.getComponent();
				
				item.setDatabase(terms.get(item.getDatabase().getTerm()), true, true);
			}
			catch(ClassCastException ex){}
		}
	}
	
	public static Preferences getPreferences(){
		return Main.prefs;
	}
	
	public static String getApplicationVersion(){
		return Main.version;
	}
	
	public static String getApplicationDirectory(){
		return Main.folderName;
	}
	
	public static void openScheduleFiles(List<String> files){
		for(String item: files){
			if(item.endsWith(Main.scheduleExt)){
				ScheduleWrap found = ScheduleWrap.load(item);
				
				Main.master.mainMenu.addMadeSchedule(found, new File(item).getName());
			}
		}	
	}
}
