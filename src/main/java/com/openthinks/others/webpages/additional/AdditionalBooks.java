/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
* @Title: AdditionalBooks.java 
* @Package com.openthinks.others.webpages.additional 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 21, 2015
* @version V1.0   
*/
package com.openthinks.others.webpages.additional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.openthinks.others.webpages.agent.HtmlResourceAgent;

/**
 * @author dailey.yet@outlook.com
 *
 */
public final class AdditionalBooks {
	private final static Map<Class<? extends HtmlResourceAgent>, Optional<AdditionalProcessor>> processors = new ConcurrentHashMap<Class<? extends HtmlResourceAgent>, Optional<AdditionalProcessor>>();

	private AdditionalBooks() {
	}

	public final static void register(Class<? extends HtmlResourceAgent> key, AdditionalProcessor value) {
		processors.put(key, Optional.of(value));
	}

	public final static Optional<AdditionalProcessor> lookup(Class<? extends HtmlResourceAgent> key) {
		return processors.get(key) == null ? Optional.empty() : processors.get(key);
	}
}
