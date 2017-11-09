package logicalOperators;

import net.sf.jsqlparser.expression.Expression;
import operators.Operator;
import operators.SelectionOperator;
import util.PhysicalPlanBuilder;

/**
 * Logical Selection Operator 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalSelectionOperator extends LogicalOperator {
	
	Expression e;
	
	/**
	 * Constructor for Logical Selection Operator
	 */
	public LogicalSelectionOperator(Expression e, LogicalOperator child){
		leftSubTree = child;
		this.e = e;
	}
	
	/**
	 * Accept function to accept Physical Plan Builder visitor
	 * 
	 * @param a Physical Plan Builder
	 */
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
		
	}

	/**
	 * Builds a physical operator give the child of the operator
	 * 
	 * @param a Physical operator which is the child
	 */
	public Operator makePhysicalOperator(Operator child) {
		return new SelectionOperator(e, child);
	}

}
