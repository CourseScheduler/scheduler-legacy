/**
 * 
 */
package io.devyse.scheduler.parse.jsoup.banner;

import io.devyse.scheduler.parse.jsoup.FormParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author mreinhold
 *
 */
public class TermSelectionParser extends FormParser {
	
	/**
	 * 
	 */
	public TermSelectionParser(Document document) {
		super(document);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try{
			parseTermSelectForm(this.getDocument());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @param document
	 * @throws IOException
	 */
	private void parseTermSelectForm(Document document) throws IOException{
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
		
		//TODO determine how to handle term selection in the UI - should be different than this...
		data.add(HttpConnection.KeyVal.create(termParameter, terms.first().nextSibling().attr("value")));
		
		this.setRawResult(connection.data(data).execute().parse());
	}
}
