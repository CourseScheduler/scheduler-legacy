/**
 * 
 */
package io.devyse.scheduler.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
 * @author mreinhold
 *
 */
public class JSoupTestParser {

	/**
	 * 
	 */
	public JSoupTestParser() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args){

		//Make sure the majority of SSL/TLS protocols are enabled
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
		
		
		String startURL = "https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject";
		
		try {
			termSelection(Jsoup.connect(startURL).get());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void termSelection(Document document) throws IOException{
		System.out.println("\n=== Term Selection ==============================");
		
		Element form = document.select("form").first();
		Connection connection = processForm(form);

		Collection<Connection.KeyVal> data = new ArrayList<>();
		
		processFormHiddenInputs(form, data);
		
		//TODO handle other form inputs
		
		Element termSelect = document.select("form select#term_input_id").first();
		Elements terms = document.select("form select#term_input_id option");
		
		//show term select
		String termParameter = termSelect.attr("name");
		
		Map<String, String> termOptions = new HashMap<>();
		System.out.println(termParameter + " selected from:");
		for(Element term: terms){
			termOptions.put(term.text(), term.attr("value"));
			System.out.println(term.attr("value") + ": " + term.text());
		}
		
		/*
		String term = (String) JOptionPane.showInputDialog(
				null, 
				"For which term would you like to download data?", 
				"Select download term", 
				JOptionPane.QUESTION_MESSAGE, 
				null, 
				termOptions.keySet().toArray(),
				terms.first().nextSibling().attr("value")
				);
		data.put(termParameter, termOptions.get(term));
		*/
		
		data.add(HttpConnection.KeyVal.create(termParameter, terms.first().nextSibling().attr("value")));
		
		courseSelection(connection.data(data).execute().parse());
	}
	
	private static void courseSelection(Document document) throws IOException{
		System.out.println("\n=== Course Selection ==============================");
		
		Element form = document.select("form").first();
		Connection connection = processForm(form);
		
		Collection<Connection.KeyVal> data = new ArrayList<>();

		processFormHiddenInputs(form, data);
		
		//TODO handle other form inputs
		
		Elements parameters = form.select("input[name]:not([type=hidden]):not([name=sel_day])");
		System.out.println("=== Non-hidden Parameters");
		for(Element parameter: parameters){
			System.out.println(parameter.attr("name") + ": " + parameter.attr("value"));
			data.add(HttpConnection.KeyVal.create(parameter.attr("name"), parameter.attr("value")));
		}
		
		Elements subjects = form.select("select#subj_id option");
		System.out.println("=== Subject Selection");
		for(Element subject: subjects){
			System.out.println("sel_subj: " + subject.attr("value") + " - " + subject.text());
			data.add(HttpConnection.KeyVal.create("sel_subj", subject.attr("value")));
		}
		
		Elements instructors = form.select("select#instr_id option[selected]");
		System.out.println("=== Instructor Selection");
		for(Element instructor: instructors){
			System.out.println("sel_instr: " + instructor.attr("value") + " - " + instructor.text());
			data.add(HttpConnection.KeyVal.create("sel_instr", instructor.attr("value")));
		}
		
//		term_in:201501
//		sel_subj:dummy
//		sel_day:dummy
//		sel_schd:dummy
//		sel_insm:dummy
//		sel_camp:dummy
//		sel_levl:dummy
//		sel_sess:dummy
//		sel_instr:dummy
//		sel_ptrm:dummy
//		sel_attr:dummy
//		sel_crse:
//		sel_title:
//		sel_from_cred:
//		sel_to_cred:
//		sel_subj:ACCT
//		sel_subj:BIOL
//		sel_subj:BUSN
//		sel_subj:CHME
//		sel_subj:CHEM
//		sel_subj:CHN
//		sel_subj:COMM
//		sel_subj:CE
//		sel_subj:CS
//		sel_subj:CUE
//		sel_subj:ECON
//		sel_subj:ECE
//		sel_subj:EE
//		sel_subj:EP
//		sel_subj:ESL
//		sel_subj:FINC
//		sel_subj:FYE
//		sel_subj:GER
//		sel_subj:HIST
//		sel_subj:HUMN
//		sel_subj:IME
//		sel_subj:KETT
//		sel_subj:INEN
//		sel_subj:MFGO
//		sel_subj:LS
//		sel_subj:LIT
//		sel_subj:MGMT
//		sel_subj:MRKT
//		sel_subj:MATH
//		sel_subj:MECH
//		sel_subj:MEDI
//		sel_subj:PHIL
//		sel_subj:PHYS
//		sel_subj:SSCI
//		sel_subj:SOC
		
//		sel_instr:%
		
//		begin_hh:0
//		begin_mi:0
//		begin_ap:a
//		end_hh:0
//		end_mi:0
//		end_ap:a
		
		//TODO pull these automatically
		data.add(HttpConnection.KeyVal.create("begin_hh", "0"));
		data.add(HttpConnection.KeyVal.create("begin_mi", "0"));
		data.add(HttpConnection.KeyVal.create("begin_ap", "a"));
		data.add(HttpConnection.KeyVal.create("end_hh", "0"));
		data.add(HttpConnection.KeyVal.create("end_mi", "0"));
		data.add(HttpConnection.KeyVal.create("end_ap", "a"));
		
		parse(connection.data(data).execute().parse());
		
	}
	
	private static void parse(Document document) throws IOException{
		System.out.println("\n=== Section Listing ==============================");
		Elements sectionRows = document.select("table.datadisplaytable > tbody > tr:has(th.ddtitle, td.dddefault span)");
		
		System.out.println("Found Rows: " + sectionRows.size());
		
		for(Element row = sectionRows.first(); row != null; row = row.nextElementSibling()){
			// Section info is 2 table rows - 1 "header" table row and 1 "detail" table row, each with sub info			
			Element section = row.clone();
			row = row.nextElementSibling();
			Element sectionDetail = row.clone();
			
			Document sectionDocument = new Document(document.baseUri());
			sectionDocument.appendChild(section);
			sectionDocument.appendChild(sectionDetail);
			
			parseCourse(sectionDocument);

		}
	}
	
	private static void parseCourse(Document document) throws IOException{
		Element sectionHeaderElement = document.select("tr > th.ddtitle > a[href]").first();
		
		String sectionDetailURL = sectionHeaderElement.absUrl("href");
		String sectionHeaderText = sectionHeaderElement.text();
		System.out.println("\n-- Section: " + sectionHeaderText);
		System.out.println("Details found at: " + sectionDetailURL);
		
		Element termElement = document.select("tr > td.dddefault > span:containsOwn(Associated Term)").first();
		String term = termElement.nextSibling().toString();
		System.out.println("Associated Term: " + term);
		
		Element registrationElement = document.select("tr > td.dddefault > span:containsOwn(Registration Dates)").first();
		String registration = registrationElement.nextSibling().toString();
		System.out.println("Registration Dates: " + registration);
		
		Element levelsElement = document.select("tr > td.dddefault > span:containsOwn(Levels)").first();
		String levels = levelsElement.nextSibling().toString();
		System.out.println("Levels: " + levels);
		
		Element campusElement = document.select("tr > td.dddefault > br + br").first();
		String campus = campusElement.nextSibling().toString();
		System.out.println("Campus: " + campus);
				
		Element scheduleTypeElement = campusElement.nextElementSibling();
		String scheduleType = scheduleTypeElement.nextSibling().toString();
		System.out.println("Schedule Type: "+ scheduleType);
		
		//TODO switch this to the catalog entry credits?
		Element creditElement = scheduleTypeElement.nextElementSibling();
		String credit = creditElement.nextSibling().toString();
		System.out.println("Credits: " + credit);
		
		Element catalogEntryElement = document.select("tr > td.dddefault > a[href]").first();
		String catalogEntryURL = catalogEntryElement.absUrl("href");
		System.out.println("Catalog Entry found at: " + catalogEntryURL);
		
		parseCourseDetail(Jsoup.connect(sectionDetailURL).get());
		parseCatalogEntry(Jsoup.connect(catalogEntryURL).get());
	
		
		//TODO meeting time loop
	}
	
	private static void parseCatalogEntry(Document document){
		
	}
	
	private static void parseCourseDetail(Document document){
		Elements availabilityHeaders = document.select("caption:containsOwn(Registration Availability) + tbody th.ddheader span");
		Elements availabilityValues = document.select("caption:containsOwn(Registration Availability) + tbody td.dddefault");
		
		Map<String, String> results = new HashMap<>();
		
		System.out.println("Seating Availability:");
		for(int pos = 0; pos < availabilityHeaders.size(); pos++){
			String header = availabilityHeaders.get(pos).text();
			String value = availabilityValues.get(pos).text();
			results.put(header, value);
			System.out.println(header + ": " + value);
		}
		
		
	}
	
	private static Connection processForm(Element form){
		String action = form.absUrl("action");
		String method = form.attr("method");

		System.out.println("Form submits to " + action + " via " + method);
		
		return Jsoup.connect(action).method(Method.valueOf(method));
	}
	
	private static void processFormHiddenInputs(Element form, Collection<Connection.KeyVal> data){
		Elements parameters = form.select("input[name][type=hidden]");

		for(Element parameter: parameters){
			System.out.println(parameter.attr("name") + ": " + parameter.attr("value"));
			data.add(HttpConnection.KeyVal.create(parameter.attr("name"), parameter.attr("value")));
		}
	}
}
