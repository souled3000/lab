/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contains some contributions under the Thrift Software License.
 * Please see doc/old-thrift-license.txt in the Thrift distribution for
 * details.
 */
package ind.storm.xaviera;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.trident.TransactionalTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.FilterNull;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.tuple.Fields;

public class Xaviera {

	private String zkUrl;
	private String brokerUrl;

	Xaviera(String zkUrl, String brokerUrl) {
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

	public Properties getProducerConfig() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "storm-kafka-producer");
		return props;
	}

	public static void main(String[] args) throws Exception {

		String zkUrl = "localhost:2181/kafka"; // the defaults.
		String brokerUrl = "localhost:9092";

		System.out.println(args.length);
		if (args.length != 4) {
			System.out.println("Usage: Xaviera [kafka zookeeper url] [kafka broker url] [topology name] [number workers]");
			System.out.println("   E.g Xaviera [" + zkUrl + "]" + " [" + brokerUrl + "] [wordcount] [1]");
			System.exit(1);
		}

		zkUrl = args[0];
		brokerUrl = args[1];
		int n =Integer.parseInt(args[3]);
		System.out.println("Using Kafka zookeeper url: " + zkUrl + " broker url: " + brokerUrl);

		Joseph wordCount = new Joseph(zkUrl, brokerUrl);

		Config conf = new Config();
		conf.setMaxSpoutPending(20);
		conf.setNumWorkers(n);
		// submit the consumer topology.
		StormSubmitter.submitTopology(args[2] + "-consumer", conf, wordCount.buildConsumerTopology(null));

	}
}
