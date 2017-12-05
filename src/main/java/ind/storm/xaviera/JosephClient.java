package ind.storm.xaviera;

import org.apache.storm.Config;
import org.apache.storm.utils.DRPCClient;
/**
 * Warning!!! this client can't work with storm core version 1.2.0, but work with version 0.9.6.
 * @author juliana
 *
 */
public class JosephClient {

	public static void main(String[] args) throws Exception{
		Config conf=new Config();
		conf.setDebug(true);
		DRPCClient client = new DRPCClient(conf,"192.168.2.177", 3772);
		for (int i = 0; i < 60; i++) {
			System.out.println("DRPC RESULT: " + client.execute("joseph", "ALL 1 2 3 4 5 6 7 8 9"));
			Thread.sleep(1000);
		}
	}

}
