package logicalOperators;

import java.util.List;

import net.sf.jsqlparser.statement.select.SelectItem;
import operators.Operator;
import operators.ProjectionOperator;
import util.PhysicalPlanBuilder;

/**
 * Logical Projection Operator
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalProjectionOperator extends LogicalOperator {
	
	List<SelectItem> si;
	
	/**
	 * Constructor for Logical Projection Operator
	 */
	public LogicalProjectionOperator(List<SelectItem> si, LogicalOperator child){
		leftSubTree = child;
		this.si = si;
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
		return new ProjectionOperator(si, child);
	}

}