package operators;
import java.util.List;

import net.sf.jsqlparser.statement.select.*;
import util.Tuple;
/**
 * projection operator receives the tuple from the child
 * tuple and only returns certain attributes
*@author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
*/
public class ProjectionOperator extends Operator {
	
	private List<SelectItem> si;
	/*It only has one child and the child can be a scan or a select operator
	 */
	public ProjectionOperator(List<SelectItem> si,Operator child)
	{
		this.si=si;
		leftSubTree=child;
		rightSubTree=null;
	}
	
	public Tuple getNextTuple() {
        Tuple next;
        while ((next = leftSubTree.getNextTuple()) != null){
        	String line="";
        	String[] colNames = new String[si.size()];
        	
        	for(int index=0;index<si.size();index++){
        	    String columnName = si.get(index).toString();
        	    
        	    if(columnName.equals("*")){/*select item is * */
        		    return next;
        	    }
        	    else{/*select item is column number*/
        	        Integer ColumnValue=next.findValofCol(columnName);
        	    	line=line+Integer.toString(ColumnValue)+",";
        	    	colNames[index] = (columnName);
        	    }
        	 }
        	
        	line=line.substring(0,line.length() - 1);
        	
        	return new Tuple(line, colNames);/*create new tuple with new value and corresponding columnname*/
		}
		
		return null;
        
}


}
