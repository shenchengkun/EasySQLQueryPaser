package JUnitTests;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import util.DatabaseCatalog;

public class Test {

	@org.junit.Test
	public void test() {
		//Query tested: SELECT * FROM Sailors WHERE Sailors.A >= 3 AND Sailors.B < Sailors.C AND Sailors.B = 100
		
		//Setup DB catalog
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = "C://Users//syale//workspace//cs4321project//samples//input";
		String output = "C://Users//syale//workspace//cs4321project//samples//output";
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
				List<SelectItem> s = pb.getSelectItems();
				List<OrderByElement> o = pb.getOrderByElements();
				System.out.println(o);
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		
		//Add all tables in FROM to the DB catalog
		dbc.addTableLocation(table);
	}

}
