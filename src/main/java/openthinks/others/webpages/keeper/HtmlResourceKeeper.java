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
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.webpages.keeper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import openthinks.others.webpages.HtmlPageTransfer;
import openthinks.others.webpages.additional.AdditionalBooks;
import openthinks.others.webpages.additional.AdditionalProcessor;
import openthinks.others.webpages.agent.HtmlResourceAgent;
import openthinks.others.webpages.util.ResourceInfo;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlResourceKeeper extends AbstractResourceKeeper {
	protected HtmlElement htmlElement;
	protected File keepDir;
	private HtmlResourceAgent resourceAgent;
	private HtmlPage htmlPage;
	private HtmlPageTransfer pageTransfer;

	public HtmlResourceKeeper(HtmlPageTransfer pageTransfer, HtmlElement htmlElement, File keepDir) {
		super();
		this.pageTransfer = pageTransfer;
		this.htmlElement = htmlElement;
		this.keepDir = keepDir;
	}

	public HtmlResourceKeeper(HtmlPageTransfer pageTransfer, HtmlPage htmlPage, File keepDir) {
		super();
		this.pageTransfer = pageTransfer;
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
		this.htmlElement = this.htmlPage.getDocumentElement();
	}

	public final HtmlPageTransfer getPageTransfer() {
		return this.pageTransfer;
	}

	public final HtmlPage getHtmlPage() {
		if (this.htmlPage != null)
			return this.htmlPage;
		Objects.requireNonNull(htmlElement);
		return this.htmlElement.getHtmlPageOrNull();
	}

	public <T extends HtmlResourceAgent> HtmlResourceKeeper initial(URL resourceURL, Supplier<T> agentSupplier) {
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

	public String getResourceNameOfProundSign() {
		return this.resourceAgent.resolveOfPoundSign(this.getResourceURL());
	}

	public URL getFullyQualifiedUrl(String relative) {
		try {
			return getHtmlPage().getFullyQualifiedUrl(relative);
		} catch (MalformedURLException e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			return null;
		}
	}

	public <T extends HtmlResourceAgent> Optional<AdditionalProcessor> getAdditionalProcessor(Class<T> clazz) {
		return AdditionalBooks.lookup(clazz);
	}

	public <T extends HtmlResourceAgent> void doAdditionalProcessor(Class<T> clazz) {
		ProcessLogger.debug(clazz.getName());
		getAdditionalProcessor(clazz).ifPresent((aProcessor) -> {
			try {
				aProcessor.process(this.htmlElement);
			} catch (Exception e) {
				ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			}
		});
	}

	public <T extends HtmlResourceAgent> String doAdditionalProcessor(Class<T> clazz, String htmlContent) {
		ProcessLogger.debug(clazz.getName());
		final StringBuilder content = new StringBuilder();
		getAdditionalProcessor(clazz).ifPresent((aProcessor) -> {
			try {
				String newContent = aProcessor.process(htmlContent);
				content.append(newContent);
			} catch (Exception e) {
				content.append(htmlContent);
				ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			}
		});
		return content.toString();
	}

	public boolean nameEncode() {

		return false;
	}
}
