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
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

/**
 * @author mreinhold
 *
 */
public class CourseSelectionParser extends FormParser {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
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
		System.out.println("=== Subject Selection");
		FormElement form = (FormElement)document.select("form").first();
		Connection connection = processForm(form);
		
		Collection<Connection.KeyVal> data = new ArrayList<>();

		//for some reason the jsoup prepare builds out the sel_day fields when the web form does not when using a browser
		//unless this is run wide open it only seems to match a small set of courses, possibly due to "AND"-like behavior
		//on the day selection evaluation
		Collection<Connection.KeyVal> toRemove = new ArrayList<>();
		for(Connection.KeyVal entry: connection.request().data()){
			if(entry.key().equals("sel_day") && !entry.value().equals("dummy")){
				toRemove.add(entry);
			}
		}
		connection.request().data().removeAll(toRemove);
				
		//add in the subject selection options
		Elements subjects = form.select("select#subj_id option");
		for(Element subject: subjects){
			System.out.println("sel_subj: " + subject.attr("value") + " - " + subject.text());
			data.add(HttpConnection.KeyVal.create("sel_subj", subject.attr("value")));
		}
		
		//add in the hour restriction options
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
