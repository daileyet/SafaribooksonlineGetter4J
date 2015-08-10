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
* @Title: TextResourceKeeper.java 
* @Package openthinks.others.safaribook.keeper 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.keeper;

import openthinks.others.safaribook.ResourceType;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class TextResourceKeeper extends AbstractResourceKeeper {

	@Override
	protected void doKeep() throws Exception {
		WebResponse wrp = loadWebResponse(this.getResourceURL());
		String content = wrp.getContentAsString("UTF-8");
		store(content, this.getResourcePath());
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.TEXT;
	}
}
