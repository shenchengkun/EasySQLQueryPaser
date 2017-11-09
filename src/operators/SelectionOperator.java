package operators;

import net.sf.jsqlparser.expression.Expression;
import util.EvaluateCondition;
import util.Tuple;

/**
 * Selection Operator. Pulls tuple from child node (only 1) and applies the
 * conditions from the where clause it is created with to the tuple. If the 
 * condition fails, keep pulling tuples from the child node until it finds
 * one that passes the condition or finds not more (child returns null).
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class SelectionOperator extends Operator{
	
	private Expression e;

	/**
	 * Create a selection operator node that is tied to the expression of the
	 * WHERE clause and 1 child node. There will most likely be 1 of these nodes
	 * above each scan node in the final tree.
	 * 
	 * @param expresion from WHERE clause and child node (usually scan)
	 */
	public SelectionOperator(Expression e, Operator child) {
		this.e = e;
		leftSubTree = child;
		rightSubTree = null; 
	}
	
	/**
	 * Asks its child node for a tuple and applies the WHERE conditions to it.
	 * Continue to ask child for tuples until a tuple passes the conditions or
	 * the child returns null (there are no more tuples in the txt). 
	 * 
	 * @return tuple after selection conditions are appied to it
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple next;
		while ((next = leftSubTree.getNextTuple()) != null){
			if (e == null){
				return next;
			}
			EvaluateCondition ec = new EvaluateCondition(next,null);
			e.accept(ec);
			if (ec.getResult()) {
				return next;
			}
		}
		return null;
	}
}
