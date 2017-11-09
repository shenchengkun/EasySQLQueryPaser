package logicalOperators;

import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;
import operators.ExternalSortOperator;
import operators.Operator;
import operators.SortOperator;
import util.PhysicalPlanBuilder;

/**
 * Logical Sort Operator 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalSortOperator extends LogicalOperator{

	List<SelectItem> selectClause;
	List<OrderByElement> orderClause;
	
	/**
	 * Constructor for Logical Sort Operator
	 */
	public LogicalSortOperator(List<SelectItem> selectClause, List<OrderByElement> orderClause, LogicalOperator child){
		this.selectClause = selectClause;
		this.orderClause = orderClause;
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
	public Operator makePhysicalOperator(Operator child, int sortType, int sortBuffer){
		if (sortType == 0){
			return new SortOperator(selectClause, orderClause, child);
		}
		else {
			return new ExternalSortOperator(selectClause, orderClause, child, sortBuffer);
		}
	}
}
