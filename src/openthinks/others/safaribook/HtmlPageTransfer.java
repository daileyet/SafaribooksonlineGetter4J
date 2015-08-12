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
* @Title: HtmlPageTransfer.java 
* @Package openthinks.others.safaribook 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.safaribook;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.others.safaribook.agent.HtmlCssResourceAgent;
import openthinks.others.safaribook.agent.HtmlImageResourceAgent;
import openthinks.others.safaribook.agent.HtmlJsResourceAgent;
import openthinks.others.safaribook.agent.HtmlPageResourceAgent;
import openthinks.others.safaribook.keeper.HtmlResourceKeeper;
import openthinks.others.safaribook.util.ProcessLoger;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlPageTransfer {

	private File keepDir;
	private HtmlPage htmlPage;

	private HtmlPageTransfer(HtmlPage htmlPage, File keepDir) {
		super();
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
	}

	public void transfer() {
		Objects.requireNonNull(htmlPage);
		Objects.requireNonNull(keepDir);
		////////////////////// keep resource js, image, style
		//script
		processScriptElements();
		//image
		processImgElements();
		//style
		processStylesheets();
		//anchors
		processAnchors();
		//other pre
		processOthers();
		//body
		processPage();
	}

	void processOthers() {
		// process tag pre text content trim blank space
		this.htmlPage.getElementsByTagName("pre").forEach((domEl) -> {
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

	}

	void processAnchors() {
		this.htmlPage.getAnchors().stream().filter((HtmlAnchor anchor) -> {
			return anchor.hasAttribute("href") && !anchor.getAttribute("href").isEmpty();
		}).forEach((HtmlAnchor anchor) -> {
			String hrefURL = anchor.getHrefAttribute();
			int start = hrefURL.lastIndexOf("/");
			if (start != -1) {
				hrefURL = hrefURL.substring(start + 1);
				anchor.setAttribute("href", hrefURL);
			}
		});

	}

	void processPage() {
		final HtmlResourceKeeper<HtmlPageResourceAgent> keeper = new HtmlResourceKeeper<HtmlPageResourceAgent>(
				htmlPage, keepDir);
		keeper.initial(htmlPage.getUrl(), () -> {
			return new HtmlPageResourceAgent(keeper);
		}).keep();

	}

	void processStylesheets() {
		htmlPage.getElementsByTagName("link")
				.stream()
				.filter((linkNode) -> {
					return linkNode.hasAttribute("rel") && linkNode.hasAttribute("href")
							&& !linkNode.getAttribute("href").isEmpty()
							&& "stylesheet".equalsIgnoreCase(linkNode.getAttribute("rel"));

				})
				.forEach(
						(linkNode) -> {
							HtmlLink link = (HtmlLink) linkNode;
							final HtmlResourceKeeper<HtmlCssResourceAgent> keeper = new HtmlResourceKeeper<HtmlCssResourceAgent>(
									link, getCssKeepDir());
							URL url = getFullyQualifiedUrl(link.getHrefAttribute());
							keeper.initial(url, () -> {
								return new HtmlCssResourceAgent(keeper);
							}).keep();
						});
	}

	void processImgElements() {
		htmlPage.getElementsByTagName("img")
				.stream()
				.filter((domNode) -> {
					return domNode.hasAttribute("src") && !domNode.getAttribute("src").isEmpty();
				})
				.forEach(
						(imgNode) -> {
							HtmlImage el = (HtmlImage) imgNode;
							final HtmlResourceKeeper<HtmlImageResourceAgent> keeper = new HtmlResourceKeeper<HtmlImageResourceAgent>(
									el, getImageKeepDir());
							URL url = getFullyQualifiedUrl(imgNode.getAttribute("src"));
							keeper.initial(url, () -> {
								return new HtmlImageResourceAgent(keeper);
							}).keep();

						});
	}

	void processScriptElements() {
		htmlPage.getElementsByTagName("script")
				.stream()
				.filter((domNode) -> {
					return domNode.hasAttribute("src") && !domNode.getAttribute("src").isEmpty();
				})
				.forEach(
						(scriptNode) -> {

							final HtmlResourceKeeper<HtmlJsResourceAgent> keeper = new HtmlResourceKeeper<HtmlJsResourceAgent>(
									(HtmlScript) scriptNode, getJsKeepDir());
							URL url = getFullyQualifiedUrl(scriptNode.getAttribute("src"));
							keeper.initial(url, () -> {
								return new HtmlJsResourceAgent(keeper);
							}).keep();
						});
	}

	public URL getFullyQualifiedUrl(String relative) {
		try {
			return this.htmlPage.getFullyQualifiedUrl(relative);
		} catch (Exception e) {
			ProcessLoger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			return null;
		}
	}

	public File getJsKeepDir() {
		return new File(keepDir, RESOURCE_SCRIPT_DIR);
	}

	public File getCssKeepDir() {
		return new File(keepDir, RESOURCE_STYLE_DIR);
	}

	public File getImageKeepDir() {
		return new File(keepDir, RESOURCE_IMAGE_DIR);
	}

	public File getCssRefKeepDir() {
		return new File(keepDir, RESOURCE_STYLE_REFERENCE_DIR);
	}

	public static HtmlPageTransfer create(HtmlPage htmlPage, File keepDir) {
		return new HtmlPageTransfer(htmlPage, keepDir);
	}

	///////////////////////////////////////////////////////////////////////////
	public static final String RESOURCE_STYLE_DIR = "style";
	public static final String RESOURCE_SCRIPT_DIR = "js";
	public static final String RESOURCE_IMAGE_DIR = "images";

	public static final String RESOURCE_STYLE_REFERENCE_DIR = "styleref";
	public static final String RESOURCE_STYLE_REFERENCE_REGEX = "url\\(['\"]?([^\\(\\)'\"]+)['\"]?\\)";
	public static final Pattern RESOURCE_STYLE_REFERENCE_PATTERN = Pattern.compile(RESOURCE_STYLE_REFERENCE_REGEX);
}
