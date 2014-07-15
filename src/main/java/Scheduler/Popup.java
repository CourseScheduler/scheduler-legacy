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

package Scheduler;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;


public class Popup extends JFrame implements WindowListener {
	
	
	protected static final long versionID = 2008061300007L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.makeSchedule.id;				//serial version
	
	private Component component;
	private JMenuBar menu;
	private JMenu window;
	private JMenuItem close;
	private JMenuItem attach;
	private ButtonListener buttonAction;
		
	
	private static final Dimension defSchedSize = new Dimension(800,600);
	
	public Popup(MakeSchedule item, String title){		
		super(Term.getTermString(item.local.getTerm()));
		
		if(title != null){
			this.setTitle(title);
		}
		
		super.setIconImage(Main.icon.getImage());	//set the icon
		
		setMinimumSize(defSchedSize);
		
		setLocationRelativeTo(Main.master);
		
		add(item);
		component = item;
		
		menu = new JMenuBar();
		window = new JMenu("Window");
		
		buttonAction = new ButtonListener();
		
		attach = new JMenuItem("Reattach Schedule");
		attach.addActionListener(buttonAction);
		window.add(attach);
		
		close = new JMenuItem("Close");
		close.addActionListener(buttonAction);
		
		window.add(close);
		
		menu.add(window);
		this.setJMenuBar(menu);
		
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
	}

	
	
	public Popup(Course item){
		
	}
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	
	
	
	
	
	public void windowClosing(WindowEvent event){
		Main.master.detached.remove(this);
	}
	public void windowDeiconified(WindowEvent event){}
	public void windowOpened(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowClosed(WindowEvent event){}	
	
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(event.getSource().equals(close)){
				Popup.this.dispose();
			}
			else if(event.getSource().equals(attach)){
				Main.master.detached.remove(Popup.this);
				Main.master.tabControl.addTab(Popup.this.getTitle(), Popup.this.component);
				Main.master.makeButtonTab(
					Main.master.tabControl.indexOfComponent(Popup.this.component));
				Popup.this.setVisible(false);
				Popup.this.dispose();
			}
		}
	}
}
