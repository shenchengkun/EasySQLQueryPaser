package operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import util.DatabaseCatalog;
import util.Tuple;

/**
 * The abstract class for all operators.
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public abstract class Operator {
	
	protected Operator leftSubTree;
	protected Operator rightSubTree;
	protected int currentIndex;
	
	public abstract Tuple getNextTuple();
	
	/**
	 * After reset is called, the call to getNextTuple returns the first tuple
	 * it would have returned. All operators except Scan should use this version.
	 * Do not overwrite!!
	 * 
	 */
	public void reset() {
		if (leftSubTree != null){
			leftSubTree.reset();
		}
		if (rightSubTree != null) {
			rightSubTree.reset();
		}
	}
	
	/**
	 * Continuously calls getNextTuple until null is returned. Put all returned
	 * tuples in an output buffer and flush to output txt.
	 * 
	 */
	public void dump() {
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String file = dbc.getOutputLocation() + File.separator + "query" + Integer.toString(dbc.getOutputIndex());
		dbc.incOutputIndex();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			Tuple t;
			while ((t = this.getNextTuple()) != null){
				bw.write(t.toString());
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
