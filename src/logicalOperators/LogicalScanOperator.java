package logicalOperators;

import operators.Operator;
import operators.ScanOperator;
import util.PhysicalPlanBuilder;

/**
 * Logical Scan Operator 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalScanOperator extends LogicalOperator{
	
	String tableAlias;

	/**
	 * Constructor for Logical Scan Operator
	 */
	public LogicalScanOperator(String tableAlias){
		this.tableAlias = tableAlias;
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
	public Operator makePhysicalOperator(){
		return new ScanOperator(tableAlias);
	}
}
