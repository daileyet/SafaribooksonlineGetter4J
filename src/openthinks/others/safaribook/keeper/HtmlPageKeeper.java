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
* @Title: HtmlPageKeeper.java 
* @Package openthinks.others.safaribook.keeper 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.keeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import openthinks.others.safaribook.ResourceInfo;
import openthinks.others.safaribook.ResourceType;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlPageKeeper extends TextResourceKeeper {
	private HtmlPage htmlPage;
	private File keepDir;

	public HtmlPageKeeper(HtmlPage htmlPage, File keepDir) {
		super();
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
		initInfo();
	}

	private void initInfo() {
		this.resourceInfo = new ResourceInfo();
		URL resourceURL = this.htmlPage.getUrl();
		String resourceName = resourceNameOf(resourceURL);
		this.resourceInfo.setResourceURL(resourceURL);
		this.resourceInfo.setResourceName(resourceName);
		resourceInfo.setResourcePath(new File(keepDir, resourceName).getAbsolutePath());
	}

	@Override
	protected WebResponse loadWebResponse(URL url) throws IOException {
		final WebRequest referringRequest = this.htmlPage.getWebResponse().getWebRequest();
		final WebRequest wrq = new WebRequest(url, this.htmlPage.getWebClient().getBrowserVersion()
				.getScriptAcceptHeader());
		wrq.setAdditionalHeaders(new HashMap<>(referringRequest.getAdditionalHeaders()));
		wrq.setAdditionalHeader("Referer", referringRequest.getUrl().toString());
		wrq.setAdditionalHeader("Accept", this.htmlPage.getWebClient().getBrowserVersion().getScriptAcceptHeader());
		WebResponse wrp = this.htmlPage.getWebClient().loadWebResponse(wrq);
		return wrp;
	}

	@Override
	protected void doKeep() {
		String html = this.htmlPage.asXml();
		String keepPath = this.getResourcePath();
		Objects.requireNonNull(keepPath, "Need provide the keep path for this page[" + this.getResourceURL() + "]");
		store(html, keepPath);
	}

	public CssKeeper cssKeeper(ResourceInfo resourceInfo) {
		CssKeeper cssKeeper = new CssKeeper(this);
		cssKeeper.resourceInfo = resourceInfo;
		return cssKeeper;
	}

	public JavascriptKeeper jsKeeper(ResourceInfo resourceInfo) {
		JavascriptKeeper jsKeeper = new JavascriptKeeper(this);
		jsKeeper.resourceInfo = resourceInfo;
		return jsKeeper;
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.TEXT_HTML;
	}
}
