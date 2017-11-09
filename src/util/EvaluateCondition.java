package util;

import java.util.Stack;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;
import util.Tuple;

/**
 * Evaluates an expression from the where clause on the 1-2 tuples used to
 * create an instance of this class. If only 1 tuple (other is null), then
 * this class should be used in selection. If 2 tuples, use for join. 
 * How to use: Expression e; e.accept(new Expression Visitor(t1, t2))
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */

public class EvaluateCondition implements ExpressionVisitor {
	
	private Tuple tuple1;
	private Tuple tuple2;
	private boolean result;
	private Stack<Long> valStk;
	
	/**
	 * If using for selection, new EvaluateCondition(t1,null). If using for 
	 * join, use new EvaluateCondition(t1,t2)
	 * 
	 * @param table row as string and name of source table
	 */
	public EvaluateCondition(Tuple t1, Tuple t2){
		tuple1 = t1;
		tuple2 = t2;
		result = true; 
		valStk = new Stack<Long>();
	}
	
	/**
	 * Return the truth value of the final evaluation. The only statements that
	 * can affect the result are constants (i.e. 5 = 5) and binary operations
	 * where both values are NOT null. (i.e. if t1 is Sailors and t2 is null,
	 * Sailors.A = Boats.D does nothing. If t2 is Boats, it will consider this
	 * expression in the final result). Don't worry too much about how this works.
	 * Trust me that it works :)
	 * 
	 * @return final evaluation result
	 */
	public boolean getResult(){
		return result;
	}
	
	/************************VISIT METHODS BELOW ****************************/
	
	@Override
	public void visit(LongValue longValue) {
		valStk.push(longValue.getValue());
	}
	
	@Override
	public void visit(AndExpression andExpression) {
		andExpression.getLeftExpression().accept(this);
		andExpression.getRightExpression().accept(this);		
	}
	
	@Override
	public void visit(MinorThan minorThan) {
		minorThan.getLeftExpression().accept(this);
		minorThan.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left < right);
		}
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		minorThanEquals.getLeftExpression().accept(this);
		minorThanEquals.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left <= right);
		}
		
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		notEqualsTo.getLeftExpression().accept(this);
		notEqualsTo.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left != right);
		}
	}

	@Override
	public void visit(Column tableColumn) {
		String col = tableColumn.getWholeColumnName();
		Integer val1 = tuple1.findValofCol(col);
		//Join
		if (tuple2 != null) {
			Integer val2 = tuple2.findValofCol(col);
			if (val1 != null){
				valStk.push(val1.longValue());
			}
			else if (val2 != null) {
				valStk.push(val2.longValue());
			}
			else {
				valStk.push(null);
			}
		} 
		//Selection
		else {
			if (val1 == null){
				valStk.push(null); 
			}
			else {
				valStk.push(val1.longValue());
			}
		}
	}
	
	@Override
	public void visit(EqualsTo equalsTo) {
		equalsTo.getLeftExpression().accept(this);
		equalsTo.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left == right);
		}
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		greaterThan.getLeftExpression().accept(this);
		greaterThan.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left > right);
		}
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		greaterThanEquals.getLeftExpression().accept(this);
		greaterThanEquals.getRightExpression().accept(this);
		Long right = valStk.pop();
		Long left = valStk.pop();
		if (left != null && right != null) {
			result = result && (left >= right);
		}
		
	}
	
	@Override
	public void visit(NullValue nullValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function function) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression inverseExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue doubleValue) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimeValue timeValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimestampValue timestampValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringValue stringValue) {

	}

	@Override
	public void visit(Addition addition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division division) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication multiplication) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction subtraction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OrExpression orExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Between between) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InExpression inExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void visit(SubSelect subSelect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression caseExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause whenClause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Concat concat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches matches) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
		// TODO Auto-generated method stub
		
	}
	

}
