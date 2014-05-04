package Scheduler;

import java.util.HashMap;
import java.util.Vector;

public class Conflict implements Comparable<Conflict> {

	protected final static long versionID = 2009021100015L;//file version
	
	Schedule item;
	String comb;
	String cont;
	String count;
	Vector<String> primary;
	HashMap<String, Vector<String>> number;
	HashMap<String, Vector<String>> links;
	HashMap<String, String> minUse;
	HashMap<Section, Section> conflicted;
	HashMap<Section, ConflictType> conflictedTypes;
	
	public Conflict(){
		primary = new Vector<String>();
		number = new HashMap<String, Vector<String>>();
		links = new HashMap<String, Vector<String>>();
		minUse = new HashMap<String, String>();
		conflicted = new HashMap<Section, Section>();
		conflictedTypes = new HashMap<Section, ConflictType>();
	}
	
	public void setSchedule(Schedule sched){
		item = sched;
	}
	
	public void setScheduleCombonationDesc(String desc){
		comb = desc;
	}
	
	public void addConflict(Section missing, Section in, ConflictType type){
		conflicted.put(missing, in);
		conflictedTypes.put(missing, type);
	}
	
	public boolean hasConflict(Section missing){
		if(conflicted.get(missing) != null){
			return true;
		}
		return false;
	}
	
	public void setScheduleContents(String desc){
		cont = desc;
	}
	
	public void setCountMessage(String err){
		count = err;
	}
	
	public void addPrimary(String pri){
		primary.add(pri);
	}
	
	public void addPrimary(Section missing, Section in){
		addPrimary("Missing primary course " + missing.getDescription() + " because it conflicts with " + in.getDescription());
		addConflict(missing, in, ConflictType.primary);
	}
	
	public String getPrimaryError(Section missing){
		for(String one: primary){
			if(one.contains("Missing primary course " + missing.getDescription())){
				return one;
			}
		}
		return new String("");
	}
	
	public void addNumberError(String course, String err){
		Vector<String> numbers = number.remove(course);
		
		if(numbers == null){
			numbers = new Vector<String>();
		}
		
		numbers.add(err);
		
		number.put(course, numbers);
	}
	
	public String[] getNumberError(Section missing){
		String outer[] = new String[2];
		
		for(String key: number.keySet()){
			if(key.contains(missing.getPerceivedCourse())){
				outer[0] = new String(key);
				break;
			}
		}
		
		Vector<String> errs = number.get(outer[0]);
		
		//outer += " " + errs.get(0);
		outer[1] = errs.get(0);
		
		for(int pos = 1; pos < errs.size(); pos++){
			outer[1] += ", " + errs.get(pos);
		}
		
		return outer;
	}
	
	public void addNumberError(Section missing, Section in, int num){
		addNumberError(missing.getPerceivedCourse() + " is not present " + num + " times", "Conflicts with " + in.getDescription());
		addConflict(missing, in, ConflictType.number);
	}
	
	public void addLinkError(String link, String err){
		Vector<String> conflict = links.remove(link);
		
		if(conflict == null){
			conflict = new Vector<String>();
		}
		
		conflict.add(err);
		
		links.put(link, conflict);
	}
	
	public void addLinkError(String link, Vector<String> err){
		Vector<String> conflict = links.remove(link);
		
		if(conflict == null){
			conflict = err;
		}
		else{
			for(String once: err){
				conflict.add(once);
			}	
		}
		
		links.put(link, conflict);
	}
	
	public void addLinkError(String link, Vector<Section[]> comp, Void unused){
		Vector<String> conflict = links.remove(link);
		
		if(conflict == null){
			conflict = new Vector<String>();
		}
		
		for(Section[] secs: comp){
			addConflict(secs[0], secs[1], ConflictType.link);
			
			conflict.add("without " + secs[0].getCourseID() + " because " + secs[0].getDescription() + " conflicts with " + secs[1].getDescription());
		}
		
		links.put(link, conflict);
	}
	
	public String[] getLinkError(Section missing){
		String outer[] = new String[2];
		
		for(String key: links.keySet()){
			if(key.contains(missing.getCourseID())){
				outer[0] = new String(key);
				break;
			}
		}
		
		Vector<String> errs = links.get(outer[0]);
		
		//outer += " " + errs.get(0);
		outer[1] = errs.get(0);
		
		for(int pos = 1; pos < errs.size(); pos++){
			outer[1] += ", " + errs.get(pos);
		}
		
		return outer;
	}
	
	public void addMinUseError(Section missing, Section in){
		minUse.put(missing.getDescription(), "Conflicts with " + in.getDescription());
		
		addConflict(missing, in, ConflictType.minUse);
	}
	
	@Override
	public String toString(){
		int errors = numOfErrors();
		
		return numOfErrors() + " Error" + (errors != 1 ? "s (" : " (") + item + ")";
	}
	
	private int numOfErrors(){
		int errors = 0;
		
		if(count != null ){
			errors++;
		}
		
		for(@SuppressWarnings("unused")String key: minUse.keySet()){
			errors++;
		}
		
		errors += primary.size();

		for(String key: number.keySet()){
			Vector<String> one = number.get(key); 
			
			if(one != null){
				errors += one.size();
			}
		}
		
		for(String key: links.keySet()){
			Vector<String> one = links.get(key); 
			
			if(one != null){
				errors += one.size();
			}
		}
		
		return (errors > 1) ? errors : 1;
	}
	
	
	public String toTerminalString(){
		String toReturn = new String();
		
		toReturn += comb + "\n" + cont + "\n" + (count == null ? "" : count + "\n");
		
		for(String pri: primary){
			toReturn += pri + "\n";
		}
		
		for(String key: number.keySet()){
			toReturn += key + ":\n";
			
			for(String num: number.get(key)){
				toReturn += "\t\t\t\t" + num + "\n";
			}
		}
		
		for(String key: links.keySet()){
			toReturn += key + ":\n";
			
			for(String miss: links.get(key)){
				toReturn += "\t\t\t\t" + miss + "\n";
			}
		}
		
		return toReturn;
	}

	public String toDisplayString(){
		String toReturn = new String();
		
		toReturn += "<html>" + comb + "<br>" + cont + "<br>" + (count == null ? "" : count + "<br>");
		
		for(String pri: primary){
			toReturn += pri + "<br>";
		}
		
		for(String key: number.keySet()){
			toReturn += key + ":<br>";
			
			for(String num: number.get(key)){
				toReturn += "     " + num + "<br>";
			}
		}
		
		for(String key: links.keySet()){
			toReturn += key + ":<br>";
			
			for(String miss: links.get(key)){
				toReturn += "     " + miss + "<br>";
			}
		}
		
		return toReturn;
	}
	
	@Override
	public int compareTo(Conflict o) {
		int val = Compare.equal.value();
		
		if(hasClosedCourse() && !o.hasClosedCourse()){
			return Compare.more.value();
		}
		else if(!hasClosedCourse() && o.hasClosedCourse()){
			return Compare.less.value();
		}
		
		val =  new Integer(numOfErrors()).compareTo(new Integer(o.numOfErrors()));
		
		if(val == Compare.equal.value()){
			return -item.toString().compareTo(o.item.toString());
		}
		
		return val;	
	}
	
	public String toTipString(){
		String toReturn = new String("<html>");
		boolean first = true;
		
		for(Section one: item.getClassesObj()){
			if(one.isClosed()){
				toReturn += (!first ? "<br>" : "") + "<font color=\"maroon\">" + one.getDescription() + "</font>";
			}
			else{
				toReturn += (!first ? "<br>" : "") + one.getDescription();
			}
			first = false;
		}
		
		for(Section one: conflicted.keySet()){
			if(one.isClosed()){
				toReturn += (!first ? "<br>" : "") + "<strong><font color=\"maroon\">" + one.getDescription() + " [" + conflictedTypes.get(one)+ "]</font></strong>";
			}
			else{
				toReturn += (!first ? "<br>" : "") + "<strong>" + one.getDescription() + " [" + conflictedTypes.get(one) + "]</strong>";
			}
			first = false;
		}
		
		if(count != null){
			toReturn += "<br><strong>" + count + "</strong>";
		}
		
		toReturn += "</html>";
		
		return toReturn;
	}
	
	public boolean hasClosedCourse(){
		for(Section one: item.getClassesObj()){
			if(one.isClosed()){
				return true;
			}
		}
		
		for(Section key: conflicted.keySet()){
			if(key.isClosed()){
				return true;
			}
		}
		
		return false;
	}
	
	public enum ConflictType{
		primary ("Primary Course"),
		link ("Link Member"),
		number ("Selected Number"),
		minUse ("Minimum Number");
		
		private String text;
		
		ConflictType(String text){
			this.text = text;
		}
		
		@Override
		public String toString(){
			return text;
		}
	}

	public static long getVersionID() {
		return versionID;
	}
}
