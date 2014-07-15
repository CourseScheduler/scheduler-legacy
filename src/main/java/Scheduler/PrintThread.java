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
 * File: PrintThread.java
 * 
 * Contains class:
 * 
 * 		PrintThread:
 * 
 * 			Purpose: To manage printing and closing of dialogs
 * 				when finished with the print execution
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;									//declare as member of scheduler package


/*********************************************************
 * The following imports are necessary for this class's function
*********************************************************/
import javax.swing.SwingWorker;						//swing worker abstract class
import java.awt.print.PrinterException;				//printer exceptions
import java.awt.print.PrinterJob;					//printer job class
import javax.swing.JDialog;							//dialog class


/*********************************************************
 * Class PrintThread
 * 
 * @purpose provides a way to manage printing of files and 
 * 		closing dialogs when the printer has received the job
 * 
 * @See SwingWorker<T,V>
*********************************************************/
public class PrintThread extends SwingWorker<Void, Void> {
	
	/*********************************************************
	 * The following protected static constants are versioning values
	*********************************************************/
	protected static final long versionID = 2008070700001L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.printThread.id;					//serial version
	
	
	/*********************************************************
	 * The following are the fields of the thread
	*********************************************************/
	private PrinterJob printerJob;					//the job to print
	private JDialog toClose;						//the dialog to close after job is sent to printer
	
	
	/*********************************************************
	 * @purpose Print the job in the backgroung
	 * 
	 * @See Override, SwingWorker<T,V>
	*********************************************************/
	@Override
	protected Void doInBackground() throws Exception {
		try {										//try
			printerJob.print();						//print the job
		}
		catch (PrinterException pe) {				//catch exceptions
			System.out.println("Error printing: " + pe);//print out exceptions
		}
		return null;								//return Void
	}
	
	
	/*********************************************************
	 * @purpose Handle thread cleanup
	 * 
	 * @see Override, SwingWorker<T,V>
	*********************************************************/
	@Override
	protected void done(){
		toClose.setVisible(false);					//make the print dialog invisible
	}

	
	/*********************************************************
	 * @purpose return the printer job
	 * 
	 * @return PrinterJob: the print associated with this thread
	*********************************************************/
	public PrinterJob getPrinterJob() {
		return printerJob;							//return the job
	}

	
	/*********************************************************
	 * @purpose Set the printer job for this thread
	 * 
	 * @param PrinterJob printerJb: the print to associate with this thread
	*********************************************************/
	public void setPrinterJob(PrinterJob printerJob) {
		this.printerJob = printerJob;				//set the print job
	}

	
	/*********************************************************
	 * @purpose return the dialog to close
	 * 
	 * @return JDialog: the dialog that will be closed when the thread is done
	*********************************************************/
	public JDialog getToClose() {
		return toClose;								//return the dialog
	}

	
	/*********************************************************
	 * @purpose Set the dialog for this thread
	 * 
	 * @param JDialog toClose: the dialog that will be closed when the thread terminates
	*********************************************************/
	public void setToClose(JDialog toClose) {
		this.toClose = toClose;						//set the thread
	}
}
