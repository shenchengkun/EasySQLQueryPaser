package operators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import util.Aliases;
import util.DatabaseCatalog;
import util.Tuple;

/**
 * Scan operator. Reads tuples line by line from the table it is created with.
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class ScanOperator extends Operator{
	
	private BufferedReader table;
	private String tableName;
	private String tableAlias;
	
	/**
	 * Create scan node with a reader on the txt file associated with the input 
	 * table name. This allows getNextTuple() to access the next line of the input
	 * each time it's called.
	 * 
	 * @param table name
	 */
	public ScanOperator(String tableAlias) {
		leftSubTree = null;
		rightSubTree = null;
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		Aliases a = Aliases.getInstance();
		tableName = a.getTableName(tableAlias);
		this.tableAlias = tableAlias;
		try {
			table = new BufferedReader(new FileReader(dbc.getTableLocation(tableName)));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read next tuple from the txt file and return it as tuple object
	 * 
	 * @return next tuple of txt file
	 */
	@Override
	public Tuple getNextTuple(){
		try {
			String line = table.readLine();
			if (line != null) {
				return new Tuple(line,tableAlias);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * After this method is called, the next call to getNextTuple() returns the
	 * first line of the txt file.
	 * 
	 */
	@Override
	public void reset(){
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		try {
			table = new BufferedReader(new FileReader(dbc.getTableLocation(tableName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
