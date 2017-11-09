package JUnitTests;

import static org.junit.Assert.*;

import java.io.FileReader;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import org.junit.Test;

import util.DatabaseCatalog;
import util.EvaluateCondition;
import util.Tuple;

public class EvaluateConditionTests {

	@Test
	public void test() {
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\input";
		String output = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\output";
		dbc.initCatalog(input,output);
		
		Tuple t1 = new Tuple("1,1,3","Sailors");
		
		Expression exp = null;
		EvaluateCondition ec = new EvaluateCondition(t1,null);
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(dbc.getQueryLocation()));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				Select select = (Select) statement;
				PlainSelect pb = (PlainSelect) select.getSelectBody();
				Expression e = pb.getWhere();
				exp = e;
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		exp.accept(ec);
		//assertEquals(true,ec.getResult());
	}

}
