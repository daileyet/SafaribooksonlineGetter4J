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
* @Title: ResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages.agent;

import java.io.IOException;
import java.net.URL;

import openthinks.others.webpages.keeper.HtmlResourceKeeper;
import openthinks.others.webpages.keeper.ResourceKeep;
import openthinks.others.webpages.util.ResourceType;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * The agent interface for {@link ResourceKeep}
 * @author dailey.yet@outlook.com
 * @see HtmlResourceAgent
 *
 */
public interface ResourceAgent {

	/**
	 * the real action to download the resource which represented by the special parameter
	 * @param element HtmlElement
	 * @throws IOException
	 */
	public void makeKeepToLocal(HtmlElement element) throws IOException;

	/**
	 * the real action to change link reference of the resource which represented by the special parameter 
	 * @param element HtmlElement
	 */
	public default void makeChangeToLocal(HtmlElement element) {
		//default nothing to change
	}

	/**
	 * get the reference {@link ResourceKeep}
	 * @return T ResourceKeep
	 */
	public <T extends ResourceKeep> T getKeeper();

	/**
	 * get the resource type
	 * @return ResourceType
	 */
	public default ResourceType getResourceType() {
		return ResourceType.TEXT;
	}

	/**
	 * the real resource name convert function, it will override the {@link ResourceKeep#resourceNameOf(URL)}
	 * @param url URL
	 * @return String
	 */
	public default String resolve(URL url) {
		String name = url.toString();
		int start = name.lastIndexOf("/");
		int end = name.indexOf("?");
		if (end != -1 && start < end)
			name = name.substring(start + 1, end);
		else
			name = name.substring(start + 1);
		//
		end = name.lastIndexOf("#");
		if (end != -1) {
			name = name.substring(0, end);
		}

		return name;
	}

	/**
	 * xxx.svg#abc
	 * the real resource name convert function, it will called the {@link HtmlResourceKeeper#getResourceNameOfProundSign()}
	 * @param url URL
	 * @return String
	 */
	public default String resolveOfPoundSign(URL url) {
		String name = url.toString();
		int start = name.lastIndexOf("/");
		int end = name.indexOf("?");
		if (end != -1 && start < end)
			name = name.substring(start + 1, end);
		else
			name = name.substring(start + 1);
		return name;
	}
}
