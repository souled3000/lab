package ind.storm.xaviera;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

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

import org.apache.storm.tuple.Values;

public class JosephSplit extends BaseFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void execute2(TridentTuple tuple, TridentCollector collector) {
        String[] words= tuple.getString(0).split("\\|");
        if (words[3].equals("0")){
        	collector.emit(new Values(words[2]));
        	collector.emit(new Values("ALL"));
        }
        if (words[3].equals("1")){
        	collector.emit(new Values(words[2]));
        	collector.emit(new Values("ALL"));
        }
    }
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String[] words= tuple.getString(0).split("\\|");
		if (words[3].equals("0")){
			collector.emit(new Values(words[2],1L));
			collector.emit(new Values("ALL",1L));
		}
		if (words[3].equals("1")){
			collector.emit(new Values(words[2],-1L));
			collector.emit(new Values("ALL",-1L));
		}
	}
    
}
