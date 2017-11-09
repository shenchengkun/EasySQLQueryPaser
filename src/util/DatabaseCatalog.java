package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The database catalog manages the locations of all tables, schemas, queries, 
 * and much more. Uses singleton pattern; DO NOT INSTANTIATE!! 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public final class DatabaseCatalog {
	
	private HashMap<String,String> tableLocations; //Key: table name, Value: table path
	private HashMap<String,String[]> schemas; //Key: table name, Value: schema
	private String directoryLocation; //Location of input directory
	private String outputLocation; //Location of output directory
	private int outputIndex; //The number you see at the end of output files (i.e. query1)
	private String queries; //Location of queries.SQL file
	private String sep = File.separator;
	
	private static final DatabaseCatalog INSTANCE = new DatabaseCatalog();
	
	private DatabaseCatalog() {
		tableLocations = new HashMap<String,String>();
	}
	
	/**
	 * Use this to get the object. NEVER INSTANTIATE THIS THING. All classes
	 * must have the same view of this object.
	 * 
	 * @param instance of the DB catalog
	 */
	public static DatabaseCatalog getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Initialize the catalog with the location of the input and output directories
	 * 
	 * @param ABSOLUTE path to the input and output directories; no final /
	 */
	public void initCatalog(String dirLoc, String output){
		//Remember location of input directory
		directoryLocation = dirLoc;
		
		//Remember location of output directory
		outputLocation = output;
		outputIndex = 1;
		
		//Location of queries
		queries = directoryLocation + sep + "queries.SQL";
		
		//Set up schemas
		schemas = new HashMap<String,String[]>();
		String schemaLoc = directoryLocation + sep + "db" + sep + "schema.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(schemaLoc))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       String[] tokens = line.split("\\s");
		       schemas.put(tokens[0],Arrays.copyOfRange(tokens,1,tokens.length));
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Must add each table in the FROM clause of a query to the catalog before
	 * the catalog can look up its path. 
	 * 
	 * @param name of table
	 */
	public void addTableLocation(String tableName){
		String loc = directoryLocation + sep + "db" + sep + "data" + sep + tableName;
		tableLocations.put(tableName, loc);
	}
	
	/**
	 * Get the location (value) of the input table (key)
	 * 
	 * @param name of table
	 * @return path to table
	 */
	public String getTableLocation(String tableName){
		return tableLocations.get(tableName);
	}
	
	/**
	 * Get the schema of a table.
	 * 
	 * @param name of table
	 * @return schema of table
	 */
	public String[] getSchema(String tableName){
		return schemas.get(tableName);
	}
	
	/**
	 * Get path to queries.SQL file
	 * 
	 * @return path of queries.SQL file
	 */
	public String getQueryLocation(){
		return queries;
	}
	
	/**
	 * Get location of output directory
	 * 
	 * @return path of output directory
	 */
	public String getOutputLocation(){
		return outputLocation;
	}
	
	/**
	 * Get the index of the next output file to be created. This allows each 
	 * query to index its output file like query1, query2, etc. 
	 * 
	 * @return query index
	 */
	public int getOutputIndex(){
		return outputIndex;
	}
	
	/**
	 * Increment the query index.
	 * 
	 */
	public void incOutputIndex(){
		outputIndex += 1;
	}
}

