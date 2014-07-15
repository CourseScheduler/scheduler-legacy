package Scheduler;

import java.io.File;

import javax.jnlp.SingleInstanceListener;

public class SISListener implements SingleInstanceListener {

	protected static final long versionID = 2008102400186L;	//object version
	
	@Override
	public void newActivation(String[] arg0) {
		for(String item: arg0){
			if(item.endsWith(Main.scheduleExt)){
				ScheduleWrap found = ScheduleWrap.load(item);
				
				Main.master.mainMenu.addMadeSchedule(found, new File(item).getName());
			}
		}
	}

	public static long getVersionID() {
		return versionID;
	}
}
