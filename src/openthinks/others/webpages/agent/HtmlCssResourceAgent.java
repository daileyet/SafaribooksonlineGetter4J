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
* @Title: HtmlCssResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages.agent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.HtmlPageTransfer;
import openthinks.others.webpages.keeper.HtmlResourceKeeper;
import openthinks.others.webpages.util.ResourceType;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * The css resource agent of HTML page
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlCssResourceAgent extends HtmlTextResourceAgent {

	public HtmlCssResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.TEXT_CSS;
	}

	@Override
	public void makeKeepToLocal(HtmlElement element) throws IOException {
		WebResponse wrp = this.keeper.loadWebResponse(this.keeper.getResourceURL());
		String styleCtx = wrp.getContentAsString("UTF-8");
		try {
			styleCtx = deepIntoRef(styleCtx, element);
		} catch (Exception e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
		storeTextResource(styleCtx);
	}

	@Override
	public void makeChangeToLocal(HtmlElement element) {
		element.setAttribute("href", pageTransfer().getCssPath() + "/" + keeper.getResourceName());
	}

	/**
	 * keep those reference in css, like <code>url()</code>
	 * @param styleCtx
	 * @throws IOException 
	 * @return String the localized css after keep reference resource
	 */
	String deepIntoRef(String styleCtx, HtmlElement element) throws IOException {
		Matcher matcher = HtmlPageTransfer.RESOURCE_STYLE_REFERENCE_PATTERN.matcher(styleCtx);
		File refDir = getCssRefDir();
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String relativeURL = matcher.group(1);
			URL styleRefUrl = keeper.getFullyQualifiedUrl(relativeURL);
			WebResponse wrp = keeper.loadWebResponse(styleRefUrl);
			final HtmlResourceKeeper refKeeper = new HtmlResourceKeeper(pageTransfer(), element, refDir);
			if (wrp.getContentType().startsWith("image") || wrp.getContentType().startsWith("IMAGE")
					|| ResourceType.IMAGE.isSupportSuffix(keeper.getResourceName())) {
				refKeeper.initial(styleRefUrl, () -> {
					return new HtmlCssImageResourceAgent(refKeeper);
				}).keep();
			} else if (wrp.getContentType().startsWith("text") || wrp.getContentType().startsWith("TEXT")) {
				refKeeper.initial(styleRefUrl, () -> {
					return new HtmlTextResourceAgent(refKeeper);
				}).keep();
			} else {
				refKeeper.initial(styleRefUrl, () -> {
					return new HtmlBinaryResourceAgent(refKeeper);
				}).keep();
			}
			matcher.appendReplacement(sb,
					"url(" + pageTransfer().getCssRefPath() + "/" + refKeeper.getResourceNameOfProundSign() + ")");
			ProcessLogger.info("The resource which type:[" + wrp.getContentType() + "] url:[" + styleRefUrl
					+ "] was download.");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * get css reference resource keep dir
	 * @return File
	 */
	public File getCssRefDir() {
		return pageTransfer().getCssRefKeepDir();
	}

	@Override
	public String resolve(URL url) {
		String cssName = super.resolve(url);
		if (!cssName.endsWith(".css")) {
			cssName = cssName + ".css";
		}
		return cssName;
	}
}
