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
* @Title: HtmlCssImageResourceAgent.java 
* @Package openthinks.others.safaribook.agent 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.agent;

import java.net.URL;

import openthinks.others.safaribook.keeper.HtmlResourceKeeper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlCssImageResourceAgent extends HtmlImageResourceAgent {

	public HtmlCssImageResourceAgent(HtmlResourceKeeper<?> keeper) {
		super(keeper);
	}

	@Override
	public String resolve(URL url) {
		String name = url.toString();
		int start = name.lastIndexOf("/");
		int end = name.indexOf("?");
		if (end != -1 && start < end)
			name = name.substring(start + 1, end);
		else
			name = name.substring(start + 1);
		return name;
	}

	@Override
	public void makeChangeToLocal(HtmlElement element) {

	}

}
