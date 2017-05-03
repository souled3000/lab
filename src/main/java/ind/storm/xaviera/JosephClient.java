package ind.storm.xaviera;

import org.apache.storm.Config;
import org.apache.storm.utils.DRPCClient;

public class JosephClient {

	public static void main(String[] args) throws Exception{
		Config conf=new Config();
		conf.setDebug(true);
		DRPCClient client = new DRPCClient(conf,"193.168.1.115", 3772);
		for (int i = 0; i < 60; i++) {
			System.out.println("DRPC RESULT: " + client.execute("joseph", "ALL 1 2 3 4 5 6 7 8 9"));
			Thread.sleep(1000);
		}
	}

}
