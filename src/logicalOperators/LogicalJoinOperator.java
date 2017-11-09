package logicalOperators;

import java.util.ArrayList;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import operators.BlockNestedJoin;
import operators.ExternalSortOperator;
import operators.JoinOperator;
import operators.Operator;
import operators.SortMergeJoin;
import operators.SortOperator;
import util.FindSMJSortOrder;
import util.PhysicalPlanBuilder;
import util.Tuple;

/**
 * Logical Join Operator 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class LogicalJoinOperator extends LogicalOperator{
	
	Expression exp;

	/**
	 * Constructor for Logical Join Operator
	 */
	public LogicalJoinOperator(LogicalOperator left, LogicalOperator right, Expression exp){
		this.leftSubTree = left;
		this.rightSubTree = right;
		this.exp = exp;
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
	public Operator makePhysicalOperator(Operator left, Operator right, int joinType, int joinBuffer, int sortType, int sortBuffer){
		//TNLJ
		if (joinType == 0){ 
			return new JoinOperator(left,right,exp);
		}
		//BNLJ
		else if (joinType == 1){
			return new BlockNestedJoin(left,right,exp,joinBuffer); // Change this to BNLJ once complete and use joinBuffer arg
		}
		else{
			//Pull a single tuple from the left and right to check the schema
			//Then immediately reset
			Tuple tempLeft = left.getNextTuple();
			Tuple tempRight = right.getNextTuple();
			left.reset(); // maybe reset before dump if too slow?
			right.reset();
			
			FindSMJSortOrder order = new FindSMJSortOrder(tempLeft,tempRight);
			exp.accept(order);
			ArrayList<String> leftSortOrder = order.getLeft();
			ArrayList<SelectItem> leftInput = new ArrayList<SelectItem>();
			ArrayList<String> rightSortOrder = order.getRight();
			ArrayList<SelectItem> rightInput = new ArrayList<SelectItem>();
			for (int i = 0; i < leftSortOrder.size(); i++){
				String name = leftSortOrder.get(i);
				String[] tableandcol = name.split("\\.");
				Table t = new Table();
				t.setName(tableandcol[0]);
				Column c = new Column(t, tableandcol[1]);
				SelectExpressionItem si = new SelectExpressionItem();
				si.setExpression(c);
				leftInput.add(si);
			}
			for (int i = 0; i < rightSortOrder.size(); i++){
				String name = rightSortOrder.get(i);
				String[] tableandcol = name.split("\\.");
				Table t = new Table();
				t.setName(tableandcol[0]);
				Column c = new Column(t, tableandcol[1]);
				SelectExpressionItem si = new SelectExpressionItem();
				si.setExpression(c);
				rightInput.add(si);
			}
			
			Operator leftSubTree;
			Operator rightSubTree;
			if (sortType == 0) {
				leftSubTree = new SortOperator(leftInput,null,left);
				rightSubTree = new SortOperator(rightInput,null,right);
			}
			else {
				leftSubTree = new ExternalSortOperator(leftInput,null,left,sortBuffer);
				rightSubTree = new ExternalSortOperator(rightInput,null,right,sortBuffer);
			}
			return new SortMergeJoin(leftSubTree,rightSubTree,exp,leftSortOrder,rightSortOrder); //Change this to SMJ once complete
		}
	}
}
