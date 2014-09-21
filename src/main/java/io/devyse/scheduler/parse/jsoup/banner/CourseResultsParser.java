/**
 * 
 */
package io.devyse.scheduler.parse.jsoup.banner;

import io.devyse.scheduler.parse.jsoup.AbstractParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author mreinhold
 *
 */
public class CourseResultsParser extends AbstractParser<Void> {

	/**
	 * @param document
	 */
	public CourseResultsParser(Document document){
		super(document);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#exec()
	 */
	@Override
	protected boolean exec() {
		try {
			parse(this.getDocument());
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
	private void parse(Document document) throws IOException{
		Set<CourseParser> courseParsers = new HashSet<>();
		System.out.println("\n=== Section Listing ==============================");
		Elements sectionRows = document.select("table.datadisplaytable > tbody > tr:has(th.ddtitle, td.dddefault span)");
		
		System.out.println("Found " + sectionRows.size()/2 + " Sections (" + sectionRows.size() + " Rows)");
		
		for(Element row = sectionRows.first(); row != null; row = row.nextElementSibling()){
			// Section info is 2 table rows - 1 "header" table row and 1 "detail" table row, each with sub info			
			Element section = row.clone();
			row = row.nextElementSibling();
			Element sectionDetail = row.clone();
			
			Document sectionDocument = new Document(document.baseUri());
			sectionDocument.appendChild(section);
			sectionDocument.appendChild(sectionDetail);
			
			CourseParser courseParser = new CourseParser(sectionDocument);
			courseParsers.add(courseParser);
			courseParser.fork();
		}
		
		int section = 0;
		for(CourseParser parser : courseParsers){
			Map<String, String> result = parser.join();
			
			System.out.println("\n---- Section " + ++section);
			List<String> keys = new ArrayList<>();
			keys.addAll(result.keySet());
			Collections.sort(keys);
			for(String key: keys){
				System.out.println(key + ": " + result.get(key));
			}
		}
	}
}
