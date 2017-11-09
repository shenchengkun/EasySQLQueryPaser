package util;
import java.util.*;
import java.util.Random;
import util.Tuple;

public class RandomGenerator {
	
	private Tuple tuple;
	private Integer Min;
	private Integer Max;
	private String tableAlias;
	/**
	 * 
	 * the random generator is consisted of three parameters, lower range, upper range and tablename
	 */
	
	public RandomGenerator(Integer LowerBound,Integer UpperBound,String table)
	{
	    tableAlias=table;
		Min=LowerBound;
		Max=UpperBound;
	}
	
	

	public Tuple getNextTuple()
	{
		Aliases a = Aliases.getInstance();
		String[] schema = DatabaseCatalog.getInstance().getSchema(a.getTableName(tableAlias));
		String line="";
		for (int i = 0; i < schema.length; i++){
			Integer rand=Min + (int)(Math.random() * ((Max - Min) + 1));
			line=line+Integer.toString(rand)+",";
		}
		line=line.substring(0,line.length() - 1);
		tuple=new Tuple(line,tableAlias);
		
		return tuple;
	}
}
