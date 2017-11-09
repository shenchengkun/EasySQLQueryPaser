package operators;

import util.Tuple;
/**
*@author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
*/
public class DuplicateEliminationOperator extends Operator {
	/*
	 * duplicate elimination operator assumes that the input
	 * from the child is in sorted order,
	 * reads the tuple from the child and only output the nonduplicates
	 */
	private Tuple lastTuple;

	public DuplicateEliminationOperator(Operator child){
		leftSubTree = child;
	}
	/*
	 * compare the tuple with its child's tuple, the tuple bofore it
	 * if they are the same, then return this tuple,
	 * otherwise, ignore this tuple
	 */
	public Tuple getNextTuple(){
		Tuple next;
		while ((next = leftSubTree.getNextTuple()) != null) {
			if (lastTuple != null && next.compareTo(lastTuple) == 0) {
				lastTuple = next;
			}
			else {
				lastTuple = next;
				return next;
			}
		}
		return null;
	}
}
