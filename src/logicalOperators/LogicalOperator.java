package logicalOperators;

import util.PhysicalPlanBuilder;

/**
 * Logical Operator Abstract Class 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public abstract class LogicalOperator {
	
	protected LogicalOperator leftSubTree;
	protected LogicalOperator rightSubTree;
	
	public abstract void accept(PhysicalPlanBuilder ppb);
	
	public LogicalOperator getLeftSubTree(){
		return leftSubTree;
	}
	
	public LogicalOperator getRightSubTree(){
		return rightSubTree;
	}
	
}
