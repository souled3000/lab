/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ind.storm.xaviera;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

public class JosephSpout extends BaseRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SpoutOutputCollector _collector;
	Random _rand;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;
		_rand = new Random();
	}
/**
 * id|mac|device_type|flag(0:on line;1:off line)|date and hour
 */
	public void nextTuple() {
		Utils.sleep(100);
		String[] sentences = new String[] {
				"-9|0000b0d59d63f742|1|0|1479190941|2016111514",
				"-9|0000b0d59d63f742|1|1|1479190941|2016111514",
				"-8|0000b0d59d63f7de|2|0|1479190956|2016111514",
				"-8|0000b0d59d63f7de|2|1|1479190956|2016111514",
				"-5|0000b0d59d63f799|3|0|1479190959|2016111514",
				"-5|0000b0d59d63f799|3|1|1479190959|2016111514",
				"-20|00002091489838b0|4|0|1479190959|2016111514",
				"-20|00002091489838b0|4|1|1479190959|2016111514",
				"-22|0000b0d59d63f6b6|5|0|1479190977|2016111514",
				"-22|0000b0d59d63f6b6|5|1|1479190977|2016111514",
				"-17|0000b0d59d63f6cc|6|0|1479190984|2016111514",
				"-17|0000b0d59d63f6cc|6|1|1479190984|2016111514",
				"-3|0000b0d59d63f84f|7|0|1479190989|2016111514",
				"-3|0000b0d59d63f84f|7|1|1479190989|2016111514",
				"-45|0000b0d59d63f877|8|0|1479190990|2016111514",
				"-45|0000b0d59d63f877|8|1|1479190990|2016111514",
				"-2|0000b0d59d63f81f|9|0|1479190998|2016111514",
				"-2|0000b0d59d63f81f|9|1|1479190998|2016111514",
				};
		String sentence = sentences[_rand.nextInt(sentences.length)];
		_collector.emit(new Values(sentence));
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dt"));
	}

}
