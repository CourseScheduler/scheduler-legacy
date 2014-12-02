/**
 * 
 */
package io.devyse.scheduler.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Mike Reinhold
 * @since 4.12.5
 */
public class Privacy {


	private static Map<String, String> redactValues;
	
	static {
		redactValues = new HashMap<>();
		
		redactValues.put(System.getProperty("user.name"), "{user.name}");
		
	}
	
	/**
	 * @param string
	 * @return
	 */
	public static String redactPrivateInformation(String string){
		String result = string;
		for(Entry<String, String> item: redactValues.entrySet()){
			result = result.replaceAll(item.getKey(), item.getValue());
		}
		return result;
	}
	
}
