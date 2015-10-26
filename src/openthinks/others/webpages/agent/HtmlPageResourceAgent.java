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
* @Title: HtmlPageResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages.agent;

import java.io.IOException;
import java.net.URL;

import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.keeper.HtmlResourceKeeper;
import openthinks.others.webpages.util.ResourceType;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The html content agent for HTML page
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlPageResourceAgent extends HtmlTextResourceAgent {

	public HtmlPageResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public void makeKeepToLocal(HtmlElement element) throws IOException {
		HtmlPage htmlPage = keeper.getHtmlPage();
		processAnchors(htmlPage);
		processOthers(htmlPage);
		keeper.doAdditionalProcessor(getClass());
		String html = htmlPage.asXml();
		//fix XML error
		html = html.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<!DOCTYPE html>");
		storeTextResource(html);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.TEXT_HTML;
	}

	void processAnchors(HtmlPage htmlPage) {
		//		htmlPage.getAnchors().stream().filter((HtmlAnchor anchor) -> {
		//			return anchor.hasAttribute("href") && !anchor.getAttribute("href").isEmpty();
		//		}).forEach((HtmlAnchor anchor) -> {
		//			String hrefURL = anchor.getHrefAttribute();
		//			ProcessLogger.debug("Process anchors before", hrefURL);
		//			int start = hrefURL.lastIndexOf("/");
		//			if (start != -1) {
		//				hrefURL = hrefURL.substring(start + 1);
		//				anchor.setAttribute("href", hrefURL);
		//				ProcessLogger.debug("Process anchors end", hrefURL);
		//			}
		//
		//		});

		htmlPage.getElementsByTagName("a")
				.stream()
				.filter((anchor) -> {
					return anchor.hasAttribute("href") && !anchor.getAttribute("href").isEmpty();
				})
				.filter((anchor) -> {
					return !anchor.getAttribute("href").toLowerCase().startsWith("http://")
							&& !anchor.getAttribute("href").toLowerCase().startsWith("https://")
							&& !anchor.getAttribute("href").startsWith("//");
				}).forEach((anchor) -> {
					String hrefURL = anchor.getAttribute("href");
					ProcessLogger.debug("Process anchors before", hrefURL);
					int start = hrefURL.lastIndexOf("/");
					if (start != -1) {
						hrefURL = hrefURL.substring(start + 1);
						anchor.setAttribute("href", hrefURL);
						ProcessLogger.debug("Process anchors end", hrefURL);
					}

				});

	}

	void processOthers(HtmlPage htmlPage) {
		// process tag pre text content trim blank space
		htmlPage.getElementsByTagName("pre").forEach((domEl) -> {
			String text = domEl.getTextContent();
			StringBuffer buff = new StringBuffer(text == null ? "" : text);
			if (text != null) {
				if (text.indexOf("'") == 0 || text.indexOf("\"") == 0) {
					buff.deleteCharAt(0);
				}
				int len = text.length();
				if (text.lastIndexOf("'") == (len - 1) || text.lastIndexOf("\"") == (len - 1)) {
					buff.deleteCharAt(len - 1);
				}
			}
			domEl.setTextContent(buff.toString().trim());
		});

		//process script 

	}

	@Override
	public String resolve(URL url) {
		String rst = super.resolve(url);
		if (rst == null || "".equals(rst)) {
			rst = "index.html";
		}
		return rst;
	}

}
