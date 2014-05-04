 package Scheduler;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

public class ViewCourse extends JPanel implements Tab{
	
	
	/*********************************************************
	 * The following are static constants for versioning
	*********************************************************/
	protected static final long versionID = 2008070900076L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.viewCourse.id;				//serial version
	
	
	
	private JFrame owner;
	private Database data;
	
	
	private JPopupMenu context;
	
	private JTree courses;
	private JLabel downloaded;
	
	
	
	
	
	
	
	public ViewCourse(){
		super(new BorderLayout());
		
		courses = new JTree();
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

	
	public void setDatabase(Database toUse, boolean settingsUpdate){
		
	}
	
	public void setDate(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Database getDatabase(){
		return data;
	}
	
	public JPopupMenu getContextMenu(){
		return context;
	}
	
	public void setOwner(JFrame owner){
		this.owner = owner;
	}
	
	public JFrame getOwner(){
		return owner;
	}













	/* (non-Javadoc)
	 * @see Scheduler.Tab#setDatabase(Scheduler.Database, boolean, boolean)
	 */
	@Override
	public void setDatabase(Database data, boolean allowPopups,
			boolean allowBuild) {
		this.data = data;
		
	}
}
