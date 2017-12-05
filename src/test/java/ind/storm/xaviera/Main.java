package ind.storm.xaviera;

import java.math.BigDecimal;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
	}
	
	public static void main3(String[] args) {
		logger.trace("Entering application.");
		logger.error("Did it again!");
		for (Entry<Object, Object> o : System.getProperties().entrySet()) {
			logger.info(o.getKey() + "\t\t\t\t" + o.getValue());
		}
	}

	public static void main2(String[] args) {
		System.out.println(2.0 - 1.1);
		BigDecimal a1 = new BigDecimal(2.0);
		BigDecimal a2 = new BigDecimal(1.1);
		System.out.println(a1.subtract(a2));

		System.out.println(-0xff);
		System.out.printf("%b\n", 0100101);
		int x = 0010101;
		char c = (char) -1;
		System.out.println("aaa");
		System.out.println((char) -1);
		a1 = new BigDecimal("2.0");
		a2 = new BigDecimal("1.1");
		System.out.println(a1.subtract(a2));

		System.out.printf("%x\n", (byte) 0xff);
		System.out.printf("%x\n", (byte) -1);
		System.out.println(String.format("%x", -1));
		System.out.println(String.format("%x", (short) -1));
		System.out.println(String.format("%s", (char) -1));
		System.out.println(String.format("%x", (long) -1));

		// if (args.length == 3) {
		// System.out.println(args[0]);
		// System.out.println(args[1]);
		// System.out.println(args[2]);
		// } else {
		// System.out.println("nothing");
		// }
	}

}
