package JUnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import util.DatabaseCatalog;

public class DatabaseCatalogTest {

	@Test
	public void test() {
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\input";
		String output = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\output";
		dbc.initCatalog(input,output);
		String[] schema1 = {"A","B","C"};
		assertArrayEquals(schema1,dbc.getSchema("Sailors"));
		String[] schema2 = {"D","E","F"};
		assertArrayEquals(schema2,dbc.getSchema("Boats"));
		String[] schema3 = {"G","H"};
		assertArrayEquals(schema3,dbc.getSchema("Reserves"));
		dbc.addTableLocation("Boats");
		assertEquals("C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\input\\db\\data\\Boats",dbc.getTableLocation("Boats"));
	}

}
