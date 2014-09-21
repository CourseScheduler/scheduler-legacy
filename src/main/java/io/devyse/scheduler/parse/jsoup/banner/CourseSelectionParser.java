/**
 * 
 */
package io.devyse.scheduler.parse.jsoup.banner;

import io.devyse.scheduler.parse.jsoup.FormParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author mreinhold
 *
 */
public class CourseSelectionParser extends FormParser {
	
	/**
	 * 
	 */
	public CourseSelectionParser(Document document) {
		super(document);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try{
			parseCourseSelectionForm(this.getDocument());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param document
	 * @throws IOException
	 */
	private void parseCourseSelectionForm(Document document) throws IOException{		
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
		
		this.setRawResult(connection.data(data).execute().parse());
	}
}
