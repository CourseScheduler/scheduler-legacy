/**
 * 
 */
package io.devyse.scheduler.parse.jsoup;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

/**
 * @author Mike Reinhold
 *
 */
public abstract class FormParser extends AbstractParser<Document> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The document resulting from submitting the form
	 */
	private Document result;
	
	public FormParser(Document document) {
		super(document);
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#getRawResult()
	 */
	@Override
	public Document getRawResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ForkJoinTask#setRawResult(java.lang.Object)
	 */
	@Override
	protected void setRawResult(Document value) {
		this.result = value;
	}

	/**
	 * @param connection
	 * @param data
	 * @throws IOException
	 */
	protected void submitForm(Connection connection, Collection<KeyVal> data) throws IOException{
		connection.data(data).execute().parse();
	}
	
	/**
	 * @param form
	 * @return
	 */
	protected Connection processForm(FormElement form){
		String action = form.absUrl("action");
		String method = form.attr("method");

		System.out.println("Form submits to " + action + " via " + method);
		
		return form.submit();
		//return Jsoup.connect(action).method(Method.valueOf(method));
	}
	
	/**
	 * @param form
	 * @param data
	 */
	protected void processFormHiddenInputs(FormElement form, Collection<Connection.KeyVal> data){
		Elements parameters = form.select("input[name][type=hidden]");

		for(Element parameter: parameters){
			System.out.println(parameter.attr("name") + ": " + parameter.attr("value"));
			data.add(HttpConnection.KeyVal.create(parameter.attr("name"), parameter.attr("value")));
		}
	}
}
