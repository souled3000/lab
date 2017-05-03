
package ind.storm.xaviera;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.kafka.trident.TransactionalTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.FilterNull;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.tuple.Fields;

public class Joseph {

	private String zkUrl;
	private String brokerUrl;

	Joseph(String zkUrl, String brokerUrl) {
		this.zkUrl = zkUrl;
		this.brokerUrl = brokerUrl;
	}


	private TransactionalTridentKafkaSpout createKafkaSpout() {
		ZkHosts hosts = new ZkHosts(zkUrl);
		TridentKafkaConfig config = new TridentKafkaConfig(hosts, "his_dev");
		config.scheme = new SchemeAsMultiScheme(new StringScheme());

		// Consume new data from the topic
		config.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
		return new TransactionalTridentKafkaSpout(config);
	}

	private Stream addDRPCStream(TridentTopology tridentTopology, TridentState state, LocalDRPC drpc) {
		return tridentTopology.newDRPCStream("joseph", drpc).each(new Fields("args"), new Split(), new Fields("dt")).groupBy(new Fields("dt")).stateQuery(state, new Fields("dt"), new MapGet(), new Fields("count")).each(new Fields("count"), new FilterNull()).project(new Fields("dt", "count"));
	}

	private TridentState addTridentState(TridentTopology tridentTopology) {
		return tridentTopology.newStream("streamdt", createKafkaSpout()).parallelismHint(1).each(new Fields("str"), new JosephSplit(), new Fields("dt", "status")).groupBy(new Fields("dt")).persistentAggregate(new MemoryMapState.Factory(), new Fields("dt", "status"), new JosephCount(), new Fields("count")).parallelismHint(1);
	}


	public StormTopology buildConsumerTopology(LocalDRPC drpc) {
		TridentTopology tridentTopology = new TridentTopology();
		addDRPCStream(tridentTopology, addTridentState(tridentTopology), drpc);
		return tridentTopology.build();
	}


	public Config getConsumerConfig() {
		Config conf = new Config();
		conf.setMaxSpoutPending(20);
		conf.setDebug(true);
		return conf;
	}


	public StormTopology buildProducerTopology(Properties prop) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new JosephSpout(), 2);

		KafkaBolt bolt = new KafkaBolt().withProducerProperties(prop).withTopicSelector(new DefaultTopicSelector("his_dev")).withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("key", "dt"));
		builder.setBolt("forwardToKafka", bolt, 1).shuffleGrouping("spout");
		return builder.createTopology();
	}


	public Properties getProducerConfig() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "storm-kafka-producer");
		return props;
	}

	public static void main(String[] args) throws Exception {

		String zkUrl = "193.168.1.115:2181/kafka"; // the defaults.
		String brokerUrl = "193.168.1.115:9092";

		if (args.length > 3 || (args.length == 1 && args[0].matches("^-h|--help$"))) {
			System.out.println("Usage: Joseph [kafka zookeeper url] [kafka broker url] [topology name]");
			System.out.println("   E.g Joseph [" + zkUrl + "]" + " [" + brokerUrl + "] [wordcount]");
			System.exit(1);
		} else if (args.length == 1) {
			zkUrl = args[0];
		} else if (args.length == 2) {
			zkUrl = args[0];
			brokerUrl = args[1];
		}

		System.out.println("Using Kafka zookeeper url: " + zkUrl + " broker url: " + brokerUrl);

		Joseph wordCount = new Joseph(zkUrl, brokerUrl);

		if (args.length == 3) {
			Config conf = new Config();
			conf.setMaxSpoutPending(20);
			conf.setNumWorkers(1);
			StormSubmitter.submitTopology(args[2] + "-consumer", conf, wordCount.buildConsumerTopology(null));
			StormSubmitter.submitTopology(args[2] + "-producer", conf, wordCount.buildProducerTopology(wordCount.getProducerConfig()));
		} else {
			LocalDRPC drpc = new LocalDRPC();
			LocalCluster cluster = new LocalCluster();

			cluster.submitTopology("wordCounter", wordCount.getConsumerConfig(), wordCount.buildConsumerTopology(drpc));

			Config conf = new Config();
			conf.setMaxSpoutPending(20);
//			cluster.submitTopology("kafkaBolt", conf, wordCount.buildProducerTopology(wordCount.getProducerConfig()));

			// keep querying the word counts for a minute.
			for (int i = 0; i < 60; i++) {
				System.out.println("DRPC RESULT: " + drpc.execute("joseph", "ALL 1 2 3 4 5 6 7 8 9"));
				// DRPC RESULT: [["the",846],["and",429],["apple",214],["snow",210],["jumped",211]]
				Thread.sleep(1000);
			}

			cluster.killTopology("kafkaBolt");
			cluster.killTopology("wordCounter");
			cluster.shutdown();
		}
	}
}
