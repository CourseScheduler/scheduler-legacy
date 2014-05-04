package Scheduler;

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceService;

public class Startup {

	protected static SingleInstanceService sis;
	protected static SISListener sisL;
	
	public static void main(String[] args) {
		try{
		    sis = (SingleInstanceService)ServiceManager.lookup("javax.jnlp.SingleInstanceService");
		    sisL = new SISListener();
		    sis.addSingleInstanceListener(sisL);
		    Runtime.getRuntime().addShutdownHook(new Thread(){
		    	@Override
		    	public void run() { 
		    		try{
		    			sis.removeSingleInstanceListener(sisL);
		    		}
		    		catch(Exception e){}
		    	}
		    });
		} 
		catch (Exception e) {
			sis = null; 
		}
	}

}
