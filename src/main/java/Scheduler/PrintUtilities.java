/*
 * Copied from this tutorial:
 * 
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing...
 * 
 * And also from a post on the forums at java.swing.com. My apologies that do not have
 * a link to that post, by my hat goes off to the poster because he/she figured out the 
 * sticky problem of paging properly when printing a Swing component.
 */

package Scheduler;

import java.awt.*;

import javax.swing.*;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.awt.print.*;

public class PrintUtilities implements Printable {
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(PrintUtilities.class);
	
	protected static final long versionID = 2008070900006L;	//object version
	protected static final long serialVersionUID = 1L +
			Version.printUtility.id;				//serial version
	
	
	private Component componentToBePrinted;

	public static void printComponent(Component c, String jobName) {
		new PrintUtilities(c).print(jobName);
	}

	public PrintUtilities(Component componentToBePrinted) {
		this.componentToBePrinted = componentToBePrinted;
	}

	public void print(String jobName) {
		PrinterJob printJob = PrinterJob.getPrinterJob();
				
		printJob.setJobName(jobName);
		try{
			printJob.setPrintService(PrinterJob.lookupPrintServices()[0]);
		}
		catch(Exception ex){
			logger.error("Unable to access print service", ex);
		}
		printJob.setPrintable(this);
		if (printJob.printDialog()){				
			
			//custom addition to original code allowing a small dialog to tell
			//the use that the program is sending the job to the printer
			JDialog toShow = new JDialog(Main.master, "Printing...",
					true, null);
			
			JPanel holder = new JPanel();
			holder.add(new JLabel("<html>Sending job to printer: <br><br>" + printJob.getPrintService().getName()));
			toShow.add(holder);
			
			toShow.pack();
			toShow.setLocationRelativeTo(Main.master);
						
			PrintThread print = new PrintThread();
			print.setPrinterJob(printJob);
			print.setToClose(toShow);
			Main.threadExec.execute(print);
			
			toShow.setVisible(true);
			//end of custom addition
			
		}
	}

	public int print(Graphics g, PageFormat pf, int pageIndex) {
		int response = NO_SUCH_PAGE;

		Graphics2D g2 = (Graphics2D) g;

		//  for faster printing, turn off double buffering
		disableDoubleBuffering(componentToBePrinted);

		Dimension d = componentToBePrinted.getSize(); //get size of document
		double panelWidth = d.width; //width in pixels
		double panelHeight = d.height; //height in pixels

		double pageHeight = pf.getImageableHeight(); //height of printer page
		double pageWidth = pf.getImageableWidth(); //width of printer page

		double scale = pageWidth / panelWidth;
		int totalNumPages = (int) Math.ceil(scale * panelHeight / pageHeight);

		//  make sure not print empty pages
		if (pageIndex >= totalNumPages) {
			response = NO_SUCH_PAGE;
		}
		else {

			//  shift Graphic to line up with beginning of print-imageable region
			g2.translate(pf.getImageableX(), pf.getImageableY());

			//  shift Graphic to line up with beginning of next page to print
			g2.translate(0f, -pageIndex * pageHeight);

			//  scale the page so the width fits...
			g2.scale(scale, scale);

			componentToBePrinted.paint(g2); //repaint the page for printing

			enableDoubleBuffering(componentToBePrinted);
			response = Printable.PAGE_EXISTS;
		}
		return response;
	}

	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}