package ind.storm.xaviera;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}
	
	public void testTreeMap() {
		System.out.println(System.currentTimeMillis());
		Map<Long,String> m = new TreeMap<Long,String>();
		m.put(100L, "a");
		m.put(200L, "b");
		for(Map.Entry<Long, String> o:m.entrySet()) {
			System.out.println(o.getValue());
		}
		int step = 30;
		int area = 1440/30;
		for (int i = 0; i < area; i++) {
			for (int j = i * step; j < i * step + step; j++) {
				System.out.println(j);
			}
		}
	}
}
