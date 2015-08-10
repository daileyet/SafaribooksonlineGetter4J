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
* @Title: ResourceKeep.java 
* @Package openthinks.others.safaribook 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook;

import java.net.URL;
import java.util.Objects;

/**
 * The baisc interface for resource keeper
 * @author dailey.yet@outlook.com
 */
public interface ResourceKeep {

	/**
	 * Get resource type; {@link ResourceType}
	 * @return ResourceType
	 */
	public ResourceInfo resourceInfo();

	/**
	 * store the resource
	 */
	public void keep();

	public default String resourceNameOf(URL url) {
		String name = url.toString();
		int start = name.lastIndexOf("/");
		int end = name.indexOf("?");
		if (end != -1 && start < end)
			name = name.substring(start + 1, end);
		else
			name = name.substring(start + 1);
		return name;
	}

	public default URL getResourceURL() {
		Objects.requireNonNull(resourceInfo());
		return resourceInfo().getResourceURL();
	}

	public default ResourceType getResourceType() {
		Objects.requireNonNull(resourceInfo());
		return resourceInfo().getResourceType();
	}

	public default String getResourceName() {
		Objects.requireNonNull(resourceInfo());
		return resourceInfo().getResourceName();
	}

	public default String getResourcePath() {
		Objects.requireNonNull(resourceInfo());
		return resourceInfo().getResourcePath();
	}
}
