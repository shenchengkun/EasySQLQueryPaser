package logicalOperators;

import operators.DuplicateEliminationOperator;
import operators.Operator;
import util.PhysicalPlanBuilder;

/**
 * Logical Distinct Operator
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalDistinctOperator extends LogicalOperator{

	/**
	 * Constructor for Logical Distinct Operator
	 */
	public LogicalDistinctOperator(LogicalOperator child){
		this.leftSubTree = child;
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
	public Operator makePhysicalOperator(Operator child){
		return new DuplicateEliminationOperator(child);
	}
}
