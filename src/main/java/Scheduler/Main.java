package Scheduler;

import io.devyse.scheduler.analytics.keen.KeenEngine;
import io.devyse.scheduler.logging.Logging;

import java.awt.Component;

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.XTabComponent;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
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
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 201412032345L;//object id
	protected static final long buildNumber = 1568L;//build number
	protected static final String version = new String("4.12.7");
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
	
	protected static SingleInstanceService sis;
	protected static SISListener sisL;
	
	protected static boolean nimbus = false;
	protected static boolean conflictDebugEnabled = false;
	
	public static final String KEEN_STARTUP = "start";
	public static final String KEEN_DOWNLOAD = "download";
	public static final String KEEN_SCHEDULE = "compute";
	public static final String KEEN_CONFIG = "config";
	
	
	public static void main(String[] args) throws Exception{	
	
		
		//Make sure the majority of SSL/TLS protocols are enabled
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
		
		try {
			sis = (SingleInstanceService) ServiceManager.lookup("javax.jnlp.SingleInstanceService");
			sisL = new SISListener();
			sis.addSingleInstanceListener(sisL);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						sis.removeSingleInstanceListener(sisL);
					} catch (Exception e) {
						logger.error("Unable to deregister the single instance listener", e);
					}
				}
			});
		} catch (UnavailableServiceException | NoClassDefFoundError e) {
			sis = null;
			logger.error("Unable to register as a single instance service", e);
		}
		
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
		
		if(prefs.getPolicyVersion() < policyVersion){
			
			//display the policy
			master.mainMenu.aboutHelpFrame.setVisible(true);
			master.mainMenu.aboutHelpFrame.helpTabMain.setSelectedIndex(2);
			
			JOptionPane.showMessageDialog(master.mainMenu.aboutHelpFrame, "The Course Scheduler has an updated privacy policy! You can view the policy in the About section of the Help menu.", "Updated Privacy Policy", JOptionPane.INFORMATION_MESSAGE);
			
			//update the policy version
			prefs.setPolicyVersion(policyVersion);			
			prefs.save();
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
		
		for(String item: args){
			if(item.endsWith(Main.scheduleExt)){
				ScheduleWrap found = ScheduleWrap.load(item);
				
				Main.master.mainMenu.addMadeSchedule(found, new File(item).getName());
			}
		}		
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
}
