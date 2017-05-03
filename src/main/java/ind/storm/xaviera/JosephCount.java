package ind.storm.xaviera;

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

import org.apache.storm.trident.operation.CombinerAggregator;
import org.apache.storm.trident.tuple.TridentTuple;

public class JosephCount implements CombinerAggregator<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long init2(TridentTuple tuple) {
		System.out.println("deborah---" + tuple.size() + " " + tuple.toString());
		return 1L;
	}

	public Long init(TridentTuple tuple) {
		System.out.println("deborah---" + tuple.size() + " " + tuple.toString());
		return tuple.getLongByField("status");
	}

	public Long combine(Long val1, Long val2) {
		return val1 + val2;
	}

	public Long zero() {
		return 0L;
	}

}
