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
* @Title: HtmlPageResourceKeeper.java 
* @Package openthinks.others.safaribook.keeper 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.keeper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.others.safaribook.agent.HtmlResourceAgent;
import openthinks.others.safaribook.util.ProcessLoger;
import openthinks.others.safaribook.util.ResourceInfo;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlResourceKeeper<T extends HtmlResourceAgent> extends AbstractResourceKeeper {
	protected HtmlElement htmlElement;
	protected File keepDir;
	private T resourceAgent;
	private HtmlPage htmlPage;

	public HtmlResourceKeeper(HtmlElement htmlElement, File keepDir) {
		super();
		this.htmlElement = htmlElement;
		this.keepDir = keepDir;
	}

	public HtmlResourceKeeper(HtmlPage htmlPage, File keepDir) {
		super();
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
	}

	public final HtmlPage getHtmlPage() {
		if (this.htmlPage != null)
			return this.htmlPage;
		Objects.requireNonNull(htmlElement);
		return this.htmlElement.getHtmlPageOrNull();
	}

	public HtmlResourceKeeper<T> initial(URL resourceURL, Supplier<T> agentSupplier) {
		this.resourceInfo = new ResourceInfo();
		this.resourceAgent = agentSupplier.get();
		Objects.requireNonNull(resourceAgent);
		String resourceName = resourceAgent.resolve(resourceURL);
		resourceName = (resourceName == null) ? resourceNameOf(resourceURL) : resourceName;
		this.resourceInfo.setResourceURL(resourceURL);
		this.resourceInfo.setResourceName(resourceName);
		resourceInfo.setResourcePath(new File(keepDir, resourceName).getAbsolutePath());
		return this;
	}

	@Override
	public WebResponse loadWebResponse(URL url) throws IOException {
		final WebRequest referringRequest = getHtmlPage().getWebResponse().getWebRequest();
		final WebRequest wrq = new WebRequest(url, getHtmlPage().getWebClient().getBrowserVersion()
				.getScriptAcceptHeader());
		wrq.setAdditionalHeaders(new HashMap<>(referringRequest.getAdditionalHeaders()));
		wrq.setAdditionalHeader("Referer", referringRequest.getUrl().toString());
		wrq.setAdditionalHeader("Accept", getHtmlPage().getWebClient().getBrowserVersion().getScriptAcceptHeader());
		WebResponse wrp = getHtmlPage().getWebClient().loadWebResponse(wrq);
		return wrp;
	}

	@Override
	protected void doKeep() throws Exception {
		Objects.requireNonNull(resourceAgent);
		this.resourceAgent.makeKeepToLocal(htmlElement);
	}

	@Override
	protected void doChange() {
		Objects.requireNonNull(resourceAgent);
		this.resourceAgent.makeChangeToLocal(htmlElement);
	}

	public final File getKeepDir() {
		return this.keepDir;
	}

	public URL getFullyQualifiedUrl(String relative) {
		try {
			return getHtmlPage().getFullyQualifiedUrl(relative);
		} catch (MalformedURLException e) {
			ProcessLoger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			return null;
		}
	}

}
