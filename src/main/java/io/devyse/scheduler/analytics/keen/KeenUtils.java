/**
 * 
 */
package io.devyse.scheduler.analytics.keen;

import io.devyse.scheduler.security.Privacy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mike Reinhold
 * @since 4.12.5
 */
public class KeenUtils {

	public static void mapifyEntry(Map<String, Object> map, String key, Object value){
		if(key.contains(".")){
			int left = key.indexOf(".");
			String parent = key.substring(0, left);
			String child = key.substring(left+1, key.length());
			
			Map<String, Object> subMap;
			
			try{
				subMap = (Map<String,Object>)map.get(parent);
			}catch(ClassCastException e){
				Object current = map.remove(parent);
				subMap = new HashMap<String,Object>();
				map.put(parent, subMap);
				subMap.put("@", Privacy.redactPrivateInformation(current.toString()));
			}
			if(subMap == null){
				subMap = new HashMap<String, Object>();
				map.put(parent, subMap);
			}
			
			mapifyEntry(subMap, child, value);
		}else{
			map.put(key, Privacy.redactPrivateInformation(value.toString()));
		}
	}
}
