package util;

import java.util.*;

/**
 * A tuple is a linked hash map (ordered dictionary) where the key is the 
 * column name (i.e. "Saliors.A") and the value is the value from the table. 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class Tuple implements Comparable<Tuple>{

	private LinkedHashMap<String,Integer> tuple;
	private ArrayList<String> orderPrecedence;
	
	/**
	 * Create a new tuple where the entries are the comma separated string
	 * input and the columns are associated with the schema of the input table.
	 * 
	 * @param table row as string and name of source table
	 */
	public Tuple (String t, String tableAlias) {
		tuple = new LinkedHashMap<String,Integer>();
		Aliases a = Aliases.getInstance();
		String[] cols = t.split(",");
		String[] schema = DatabaseCatalog.getInstance().getSchema(a.getTableName(tableAlias));
		if (cols.length != schema.length){
			throw new IllegalArgumentException("Schema doesn't match data");
		}
		
		for (int i = 0; i < cols.length; i++){
			tuple.put(tableAlias + "." + schema[i],Integer.parseInt(cols[i]));
		}
	}
	
	/**
	 * Create a new tuple using a custom schema
	 * 
	 * @param table row as string and name of source table
	 */
	public Tuple (String t, String[] colNames){
		tuple = new LinkedHashMap<String,Integer>();
		String[] cols = t.split(",");
		if (cols.length != colNames.length){
			throw new IllegalArgumentException("Schema doesn't match data");
		}
		
		for (int i = 0; i < cols.length; i++){
			tuple.put(colNames[i],Integer.parseInt(cols[i]));
		}
	}
	/**
	 * Create a merged tuple from two input tuples 
	 * 
	 * @param table row as string and name of source tables
	 */
	public Tuple (Tuple t1,Tuple t2) {
		tuple = new LinkedHashMap<String,Integer>();
		tuple.putAll(t1.tuple);
		tuple.putAll(t2.tuple);
	}
	
	/**
	 * Converts tuple back to the string used to create it. 
	 * 
	 * @return tuple as a comma separated string
	 */
	public String toString() {
		Iterator<Integer> it = tuple.values().iterator();
		String output = "";
		while (it.hasNext()){
			output += Integer.toString(it.next()) + ",";
		}
		return output.substring(0,output.length() - 1);
	}
	
	
	/**
	 * Search a tuple by column name and return its value
	 * 
	 * @param key
	 * @return value
	 */
	public Integer findValofCol(String k) {
		return tuple.get(k);
	}
	
	public void setCompareOrder(ArrayList<String> orderPrecedence){
		this.orderPrecedence = orderPrecedence;
		
	}
	
	public ArrayList<String> getOrderedKeyList(){
		return new ArrayList<String>(tuple.keySet());
	}
	
	@Override
	public int compareTo(Tuple t) {
		for (int i = 0; i < orderPrecedence.size(); i++){
			Integer e1 = this.findValofCol(orderPrecedence.get(i));
			Integer e2 = t.findValofCol(orderPrecedence.get(i));
			if (e1 > e2) {
				return 1;
			}
			if (e1 < e2) {
				return -1;
			}
		}
		return 0;
	}
	

	
	
}