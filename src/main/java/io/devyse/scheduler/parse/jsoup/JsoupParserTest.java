/**
 * 
 */
package io.devyse.scheduler.parse.jsoup;

import io.devyse.scheduler.parse.jsoup.banner.CourseResultsParser;
import io.devyse.scheduler.parse.jsoup.banner.CourseSelectionParser;
import io.devyse.scheduler.parse.jsoup.banner.TermSelectionParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @author Mike Reinhold
 *
 */
public class JsoupParserTest {

	/**
	 * 
	 */
	public JsoupParserTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args){

		//Make sure the majority of SSL/TLS protocols are enabled
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
		
		
		String startURL = "https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject";
		
		ForkJoinPool pool = new ForkJoinPool();
		
		try {
			TermSelectionParser termSelect = new TermSelectionParser(Jsoup.connect(startURL).method(Method.GET).execute().parse());
			CourseSelectionParser courseSelect = new CourseSelectionParser(pool.invoke(termSelect));
			CourseResultsParser courseParse = new CourseResultsParser(pool.invoke(courseSelect));
			
			pool.invoke(courseParse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
