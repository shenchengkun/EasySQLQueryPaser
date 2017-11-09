package JUnitTests;

import static org.junit.Assert.*;
import operators.ScanOperator;

import org.junit.Test;

import util.DatabaseCatalog;

public class ScanOperatorTests {

	@Test
	public void test() {
		DatabaseCatalog dbc = DatabaseCatalog.getInstance();
		String input = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\input";
		String output = "C:\\Users\\David\\Desktop\\School Work\\CS2110\\workspace\\CS4321-P2\\samples\\JUnitTests\\output";
		dbc.initCatalog(input,output);
		dbc.addTableLocation("Boats");
		ScanOperator sc = new ScanOperator("Boats");
		sc.dump();
	}

}
