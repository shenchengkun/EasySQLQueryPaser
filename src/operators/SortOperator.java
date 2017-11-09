package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;
import util.Tuple;
/*
 * sort operator.This is going to read all the output from its child,
 * place it into an internal buffer,sort it,and then return individual tuples
 * when requested.
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */
public class SortOperator extends Operator {

	private ArrayList<Tuple> tuples;
	private ArrayList<String> sortPrecedence;
	private List<SelectItem> selectClause;
	private List<OrderByElement> orderClause;
	private boolean block;
	
	public SortOperator(List<SelectItem> selectClause, List<OrderByElement> orderClause, Operator child) {
		tuples = new ArrayList<Tuple>();
		sortPrecedence = null;
		this.selectClause = selectClause;
		this.orderClause = orderClause;
		leftSubTree = child;
		rightSubTree = null;
		block = true;
	}
	
	private void findSortPrecedence(Tuple t){
		sortPrecedence = new ArrayList<String>();
		
		if (orderClause != null){
			for (int i=0; i < orderClause.size(); i++){
				sortPrecedence.add(orderClause.get(i).toString());
			}
		}
		
		if (selectClause.get(0).toString().equals("*")){
			ArrayList<String> keys = t.getOrderedKeyList();
			for (int i=0; i < keys.size(); i++){
				if (sortPrecedence.contains(keys.get(i)) == false){
					sortPrecedence.add(keys.get(i));
				}
			}
		}
		else {
			for (int i=0; i < selectClause.size(); i++){
				if (sortPrecedence.contains(selectClause.get(i).toString()) == false){
					sortPrecedence.add(selectClause.get(i).toString());
				}
			}
		}
	}
	
	public Tuple getNextTuple() {
		if (block) {
			Tuple next;
			while ((next = leftSubTree.getNextTuple()) != null) {
				if (sortPrecedence == null){
					findSortPrecedence(next);
				}
				next.setCompareOrder(sortPrecedence);
				tuples.add(next);
			}
			block = false;
			Collections.sort(tuples);
		}
		if (tuples.size() > 0) {
			Tuple output = tuples.get(0);
			tuples.remove(0);
			return output;
		}
		else {
			return null;
		}
	}
}
