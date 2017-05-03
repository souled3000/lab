package ind.storm.xaviera;

import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {
		System.out.println(2.0 - 1.1);
		BigDecimal a1 = new BigDecimal(2.0);
		BigDecimal a2 = new BigDecimal(1.1);
		System.out.println(a1.subtract(a2));

		System.out.println(-0xff);
		System.out.printf("%b\n",0100101);
		int x = 0010101;
		char c = (char)-1;
		System.out.println("aaa");
		System.out.println((char)-1);
		a1 = new BigDecimal("2.0");
		a2 = new BigDecimal("1.1");
		System.out.println(a1.subtract(a2));

		System.out.printf("%x\n", (byte)0xff);
		System.out.printf("%x\n", (byte)-1);
		System.out.println(String.format("%x", -1));
		System.out.println(String.format("%x", (short)-1));
		System.out.println(String.format("%s", (char)-1));
		System.out.println(String.format("%x", (long)-1));
		
		
//		if (args.length == 3) {
//			System.out.println(args[0]);
//			System.out.println(args[1]);
//			System.out.println(args[2]);
//		} else {
//			System.out.println("nothing");
//		}
	}

}
