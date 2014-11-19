/**
 * 
 */
package Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.devyse.scheduler.retrieval.CoursePersister;

/**
 * @author mreinhold
 *
 */
public class LegacyDataModelPersistor implements CoursePersister {

	/**
	 * 
	 */
	public LegacyDataModelPersistor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void persist(Map<String, String> data) {
		// TODO Auto-generated method stub

		//TODO remove temporary debugging output
		List<String> keys = new ArrayList<>();
		keys.addAll(data.keySet());
		Collections.sort(keys);
		for(String key: keys){
			System.out.println(key + ": " + data.get(key));
		}
	}

	
}
