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
* @Title: ResourceKeepListeners.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package com.openthinks.others.webpages.keeper;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of {@link ResourceKeepListener}
 * @author dailey.yet@outlook.com
 *
 */
public class ResourceKeepListeners {
	private List<ResourceKeepListener> listeners = new ArrayList<ResourceKeepListener>();

	public void doKeepBefore(ResourceKeep resourceKeeper) {
		listeners.stream().forEach((keepListener) -> {
			try {
				keepListener.doKeepBefore(resourceKeeper);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void doKeepAfter(ResourceKeep resourceKeeper) {
		listeners.stream().forEach((keepListener) -> {
			try {
				keepListener.doKeepAfter(resourceKeeper);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void add(ResourceKeepListener keepListener) {
		listeners.add(keepListener);
	}

}
