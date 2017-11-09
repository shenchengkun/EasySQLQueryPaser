package main;

import java.io.FileReader;
import java.util.List;

import operators.DuplicateEliminationOperator;
import operators.JoinOperator;
import operators.Operator;
import operators.ProjectionOperator;
import operators.ScanOperator;
import operators.SelectionOperator;
import operators.SortOperator;
import util.Aliases;
import util.DatabaseCatalog;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;


/**
 * SQL Parser. Work in progress. 
 * 
 * @author David Wang (dw472), Yue Sun (ys758), Chengkun Shen (cs2327)
 */
public class SQLParser {
	
	private static Operator createQueryPlan(PlainSelect ps){
		List<SelectItem> selectClause = ps.getSelectItems(); 
		Distinct d = ps.getDistinct();
		FromItem fromClause = ps.getFromItem();
		List<Join> fromClause2 = ps.getJoins();
		Expression whereClause = ps.getWhere();
		List<OrderByElement> orderClause = ps.getOrderByElements();
		if (selectClause == null || fromClause == null){
			throw new IllegalArgumentException("Missing SELECT or FROM clause");
		}
		
		mapAliasesAndTables(fromClause, fromClause2);
		Operator output = new ProjectionOperator(selectClause, recursiveTreeMaker(fromClause, fromClause2, whereClause));

		if (d != null) {
			return new DuplicateEliminationOperator(new SortOperator(selectClause, orderClause, output));
		}
		else if (orderClause == null)  {
			return output;
		}
		else {
			return new SortOperator(selectClause, orderClause, output);
		}
	}
	
	private static Operator recursiveTreeMaker(FromItem outer, List<Join> inner, Expression whereClause){
		Aliases a = Aliases.getInstance();
		//Put select operator above every scan but it does nothing if where clause is null. 
		if (inner == null || inner.size() == 0) {
			String tableName;
			if (((Table)outer).getAlias() == null){
				tableName = ((Table)outer).getName();
			}
			else {
				tableName = ((Table)outer).getAlias();
			}
			ScanOperator outerRelation = new ScanOperator(tableName);
			return new SelectionOperator(whereClause, outerRelation);
		}
		else {
			
			Table in = (Table)inner.get(inner.size() - 1).getRightItem();
			inner.remove(inner.size() - 1); //remove from list
			String tableName;
			if (((Table)in).getAlias() == null){
				tableName = ((Table)in).getName();
			}
			else {
				tableName = ((Table)in).getAlias();
			}
			ScanOperator innerRelation = new ScanOperator(tableName);
			SelectionOperator selectInner = new SelectionOperator(whereClause, innerRelation);
			return new JoinOperator(recursiveTreeMaker(outer, inner, whereClause), selectInner, whereClause);
		}
	}
	
	private static void mapAliasesAndTables (FromItem outer, List<Join> inner){
		Aliases a = Aliases.getInstance();
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		Table o = (Table)outer;
		//If there is no alias, the key should be the table name
		if (o.getAlias() == null) {
			a.addAlias(o.getName(), o.getName());
		}
		else {
			a.addAlias(o.getAlias(), o.getName());
		}
		dbc.addTableLocation(o.getName());
		
		//Add inner relations if any
		if (inner == null){
			return;
		}
		
		for (int i=0; i < inner.size(); i++){
			Table in = (Table)inner.get(i).getRightItem();
			if (in.getAlias() == null) {
				a.addAlias(in.getName(), in.getName());
			}
			else {
				a.addAlias(in.getAlias(), in.getName());
			}
			dbc.addTableLocation(in.getName());
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 2){
			throw new IllegalArgumentException("Invalid arguments");
		}
		
		//Setup DB catalog
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = args[0];
		String output = args[1];
		dbc.initCatalog(input,output);
		
		//Now get the values of WHERE and FROM from queries.SQL file
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(dbc.getQueryLocation()));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				Select select = (Select) statement;
				PlainSelect ps= (PlainSelect) select.getSelectBody();
				Operator qp = createQueryPlan(ps);
				qp.dump();
				Aliases.getInstance().reset(); //reset all alias mappings for next query
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}	
}

