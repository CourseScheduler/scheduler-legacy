package Scheduler;

import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Properties;


public class Main {
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010921152L;//object id
	protected static final long buildNumber = 1520L;//build number
	protected static final String version = new String("4.12.3");
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
	
	protected static String defURL = new String("https://jweb.kettering.edu/cku1/bwckschd.p_get_crse_unsec");
	protected static String defGradCampURL = new String("https://jweb.kettering.edu/cku1/xhwschedule_grad07.P_ViewSchedule");
	protected static String defGradDistURL = new String("https://jweb.kettering.edu/cku1/xhwschedule_grad08.P_ViewSchedule");
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
	
	public static KeenClient keen;
	public static KeenProject keenProject;
	public static final String KEEN_STARTUP = "start";
	public static final String KEEN_DOWNLOAD = "download";
	public static final String KEEN_SCHEDULE = "compute";
	public static final String KEEN_CONFIG = "config";
	
	private static final String KEEN_CONFIGURATION_FILE = "config/keen.properties";
	
	public static void main(String[] args) throws Exception{ 
		//Make sure the majority of SSL/TLS protocols are enabled
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
		
		try {
			try {
				sis = (SingleInstanceService) ServiceManager
						.lookup("javax.jnlp.SingleInstanceService");
				sisL = new SISListener();
				sis.addSingleInstanceListener(sisL);
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						try {
							sis.removeSingleInstanceListener(sisL);
						} catch (Exception e) {
						}
					}
				});
			} catch (UnavailableServiceException e) {
				sis = null;
			}
		} catch (NoClassDefFoundError e) {
			// TODO: handle exception
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
		catch (UnsupportedLookAndFeelException e) {} 
		catch (ClassNotFoundException e) {} 
		catch (InstantiationException e) {} 
		catch (IllegalAccessException e) {}
		
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

		setupKeen();
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
			Main.mapifyEntry(startupEvent, "startup.type", "end user");
			Main.registerEvent(KEEN_STARTUP, startupEvent);
		}
	}
	
	public static void registerEvent(String collection, Map<String, Object> event){
		try{
			keen.addEventAsync(collection, event);
		}catch(Exception e){
			//nothing
		}
	}

	private static Map<String, String> redactValues;
	
	static {
		redactValues = new HashMap<>();
		
		redactValues.put(System.getProperty("user.name"), "{user.name}");
		
	}
	
	public static String redactUserInfo(String string){
		String result = string;
		for(Entry<String, String> item: redactValues.entrySet()){
			result = result.replaceAll(item.getKey(), item.getValue());
		}
		return result;
	}
	
	private static void setupKeen() {
		try{
			keen = new JavaKeenClientBuilder().build();
			keen.setDebugMode(true);
			
			//TODO change the resource to be passed in as a parameter to the program or via an env variable
			Properties keenProperties = new Properties();
			try{
				keenProperties.load(loader.getResourceAsStream(KEEN_CONFIGURATION_FILE));
			}catch(Exception e){
				keenProperties.load(new FileInputStream(KEEN_CONFIGURATION_FILE));
			}
			
			keenProject = new KeenProject(
					keenProperties.getProperty("keen.project.id"),
					keenProperties.getProperty("keen.project.write"),
					null					// "keen.project.read"
			);
			
			keen.setDefaultProject(keenProject);
			
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run(){
					ExecutorService keenExecutor = (ExecutorService)keen.getPublishExecutor();
					keenExecutor.shutdown();
					try {
						keenExecutor.awaitTermination(15L, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			//global properties map
			Map<String, Object> global = new HashMap<>();
			
			//java system properties map
			//TODO exclude user sensitive attributes?
			Map<String, Object> system = new HashMap<>();
			for(Object key: System.getProperties().keySet()){
				mapifyEntry(system, key.toString(), System.getProperty(key.toString()));
			}
			global.put("system", system);
			
			//application details map
			Main.mapifyEntry(global, "scheduler.version", Main.version);
			Main.mapifyEntry(global, "scheduler.home", Main.folderName);
			
			//TODO generate or find some unique identifier
			
			UUID identifier = Main.prefs.getIdentifier();
			if(identifier == null){
				identifier = UUID.randomUUID();
				Main.prefs.setIdentifier(identifier);
				Main.prefs.save();
			}
			
			Main.mapifyEntry(global, "user.id", identifier);
			
			keen.setGlobalProperties(global);
		}catch(Exception e){
			System.out.println("Unable to initialize Keen IO Analytics: " + e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void mapifyEntry(Map<String, Object> map, String key, Object value){
		if(key.contains(".")){
			int left = key.indexOf(".");
			String parent = key.substring(0, left);
			String child = key.substring(left+1, key.length());
			
			Map<String, Object> subMap;
			
			try{
				subMap = (Map<String,Object>)map.get(parent);
			}catch(ClassCastException e){
				Object current = map.remove(parent);
				subMap = new HashMap<String,Object>();
				map.put(parent, subMap);
				subMap.put("@", redactUserInfo(current.toString()));
			}
			if(subMap == null){
				subMap = new HashMap<String, Object>();
				map.put(parent, subMap);
			}
			
			mapifyEntry(subMap, child, value);
		}else{
			map.put(key, redactUserInfo(value.toString()));
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
		
		for(Prof prof: profs){
			System.out.println(prof.getName() + ": " + prof.getRating());
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
}
