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
* @Title: HtmlTextResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package com.openthinks.others.webpages.agent;

import com.openthinks.others.webpages.keeper.HtmlResourceKeeper;
import com.openthinks.others.webpages.util.ResourceType;

/**
 * The text resource agent for HTML page
 * @author dailey.yet@outlook.com
 * @see HtmlPageResourceAgent
 * @see HtmlJsResourceAgent
 * @see HtmlCssResourceAgent
 */
public class HtmlTextResourceAgent extends HtmlResourceAgent {

	public HtmlTextResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.TEXT;
	}

}
