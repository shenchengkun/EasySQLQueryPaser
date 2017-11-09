package JUnitTests;

import static org.junit.Assert.*;

import java.io.FileReader;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import operators.Operator;
import operators.ScanOperator;
import operators.SelectionOperator;

import org.junit.Test;

import util.DatabaseCatalog;
import util.EvaluateCondition;

public class SelectionOperatorTests {

	@Test
	public void test() {
		//Query tested: SELECT * FROM Sailors WHERE Sailors.A >= 3 AND Sailors.B < Sailors.C AND Sailors.B = 100
		
		//Setup DB catalog
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\input";
		String output = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\output";
		dbc.initCatalog(input,output);
		
		/************This is the equivalent of building the tree************/
		
		Expression exp = null; //WHERE clause
		String table = ""; //FROM clause
		
		//Now get the values of WHERE and FROM from queries.SQL file
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(dbc.getQueryLocation()));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				Select select = (Select) statement;
				PlainSelect pb = (PlainSelect) select.getSelectBody();
				table = pb.getFromItem().toString();
				Expression e = pb.getWhere();
				exp = e;
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		
		//Add all tables in FROM to the DB catalog
		dbc.addTableLocation(table);
		
		//MAKE THE TREE BELOW IMPORTANT IMPORTANT!!
		Operator tree = new SelectionOperator(exp,new ScanOperator(table)); //Simple 2 node tree.
		tree.dump(); //Output handled automatically by the DB catalog
	}
}