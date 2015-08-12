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
* @Title: ResourceType.java 
* @Package openthinks.others.safaribook.util
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resource type
 * @author dailey.yet@outlook.com
 *
 */
public enum ResourceType {
	TEXT, TEXT_HTML, TEXT_XML, TEXT_JAVASCRIPT, TEXT_CSS, IMAGE, APPLICATION;

	@SuppressWarnings("unchecked")
	public List<String> getSuffix() {
		return suffixMap.get(this) == null ? Collections.EMPTY_LIST : suffixMap.get(this);
	}

	public boolean isSupportSuffix(String resourceName) {
		List<String> suffixs = getSuffix();
		for (String suffix : suffixs) {
			if (resourceName.endsWith(suffix))
				return true;
		}
		return false;
	}

	private final static Map<ResourceType, List<String>> suffixMap = new ConcurrentHashMap<ResourceType, List<String>>();

	static {
		suffixMap.put(IMAGE,
				Collections.unmodifiableList(Arrays.asList(".tiff", ".png", ".gif", ".jpg", ".bmp", ".jpeg")));
		suffixMap
				.put(TEXT, Collections.unmodifiableList(Arrays.asList(".txt", ".htm", ".html", ".xml", ".js", ".css")));
	}
}
