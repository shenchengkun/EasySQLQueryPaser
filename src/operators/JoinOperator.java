package operators;

import net.sf.jsqlparser.expression.Expression;
import util.EvaluateCondition;
import util.Tuple;

/**
 * Join operator to join two operators
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */
public class JoinOperator extends Operator{
	private Expression exp=null;
	private Tuple curLeft = null, curRight = null;
	private Operator left=null,right=null;
	
	/**
	 * Concatenate two tuples.
	 * @param tp1 the first tuple
	 * @param tp2 the second tuple
	 * @return the joined tuple
	 */
	private Tuple joinTp(Tuple tp1, Tuple tp2) {
		return  new Tuple(tp1,tp2);
	}
	
	/**
	 * Fix the left tuple and move on to the next right tuple.
	 * If the end of right is reached, reset it and get the 
	 * next left tuple.
	 */
	public void next() {
		if (curLeft == null) return;
		
		if (curRight != null)
			curRight = right.getNextTuple();
		
		if (curRight == null) {
			curLeft = left.getNextTuple();
			right.reset();
			curRight = right.getNextTuple();
		}
	}
	
	/**
	 * Keep the tuple nested loop until a valid pair 
	 * is found.
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple ret = null;
		while (curLeft != null) {
			EvaluateCondition ec = new EvaluateCondition(curLeft,curRight);
			if (exp == null) {
				ret = joinTp(curLeft,curRight);
				next();
				return ret;
			}
			exp.accept(ec);
			if (ec.getResult())
				ret=joinTp(curLeft,curRight);
			next();
			if(ret!=null) return ret;
		}
		return null;
	}
	/**
	 * Construct a join operator.
	 * @param left the left operator
	 * @param right the right operator
	 * @param exp the join condition
	 */
	public JoinOperator(Operator left, Operator right, Expression exp) {
		this.left=left;
		this.right=right;
		this.exp=exp;
		curLeft = left.getNextTuple();
		curRight = right.getNextTuple();
	}
	
}
