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
* @Title: HtmlBinaryResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package com.openthinks.others.webpages.agent;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.openthinks.others.webpages.keeper.HtmlResourceKeeper;
import com.openthinks.others.webpages.util.ResourceType;

/**
 * The binary resource agent for HTML page
 * @author dailey.yet@outlook.com
 * @see HtmlImageResourceAgent
 */
public class HtmlBinaryResourceAgent extends HtmlResourceAgent {

	public HtmlBinaryResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public void makeKeepToLocal(HtmlElement element) throws IOException {
		WebResponse wrp = this.keeper.loadWebResponse(this.keeper.getResourceURL());
		storeBinaryResource(wrp.getContentAsStream());
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.APPLICATION;
	}

}
