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
* @Title: HtmlVideoResourceAgent.java 
* @Package openthinks.others.webpages.agent 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Sep 18, 2015
* @version V1.0   
*/
package openthinks.others.webpages.agent;

import java.net.URL;

import openthinks.others.webpages.HtmlPageTransfer;
import openthinks.others.webpages.keeper.HtmlResourceKeeper;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlVideoResourceAgent extends HtmlBinaryResourceAgent {

	/**
	 * @param keeper
	 */
	public HtmlVideoResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public void makeChangeToLocal(HtmlElement element) {
		element.setAttribute("src", HtmlPageTransfer.RESOURCE_VIDEO_DIR + "/" + keeper.getResourceName());
	}

	@Override
	public String resolve(URL url) {
		String videoName = url.toString();
		int start = videoName.lastIndexOf("/");
		videoName = videoName.substring(start + 1);
		String videoSuffix = ".mp4";
		int end = videoName.lastIndexOf(".");
		if (end != -1) {
			videoSuffix = videoName.substring(end);
			videoName = videoName.substring(0, end);
		}
		if (keeper.nameEncode())
			videoName = Base64.encodeBase64String(videoName.getBytes());

		return videoName + videoSuffix;
	}
}
