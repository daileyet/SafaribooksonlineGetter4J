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
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.agent.HtmlCssResourceAgent;
import openthinks.others.webpages.agent.HtmlImageResourceAgent;
import openthinks.others.webpages.agent.HtmlJsResourceAgent;
import openthinks.others.webpages.agent.HtmlPageResourceAgent;
import openthinks.others.webpages.agent.HtmlVideoResourceAgent;
import openthinks.others.webpages.keeper.HtmlResourceKeeper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * Responsible for download whole html page include css,js,image resources<BR>
 * Also, will change the reference link to local dowanload
 * @author dailey.yet@outlook.com
 *
 */
public class HtmlPageTransfer {

	protected File keepDir;
	private HtmlPage htmlPage;

	protected HtmlPageTransfer(HtmlPage htmlPage, File keepDir) {
		super();
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
	}

	/**
	 * main action to download html page
	 */
	public void transfer() {
		Objects.requireNonNull(htmlPage);
		Objects.requireNonNull(keepDir);
		////////////////////// keep resource js, image, style
		//script
		processScriptElements();
		//image
		processImgElements();
		//video
		processVideoElements();
		//style
		processStylesheets();
		//body
		processPage();
	}

	void processPage() {
		final HtmlResourceKeeper keeper = new HtmlResourceKeeper(this, htmlPage, keepDir);
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

				}).forEach((linkNode) -> {
					HtmlLink link = (HtmlLink) linkNode;
					final HtmlResourceKeeper keeper = new HtmlResourceKeeper(this, link, getCssKeepDir());
					URL url = getFullyQualifiedUrl(link.getHrefAttribute());
					keeper.initial(url, () -> {
						return new HtmlCssResourceAgent(keeper);
					}).keep();
				});
	}

	void processImgElements() {
		htmlPage.getElementsByTagName("img").stream().filter((domNode) -> {
			return domNode.hasAttribute("src") && !domNode.getAttribute("src").isEmpty();
		}).forEach((imgNode) -> {
			HtmlImage el = (HtmlImage) imgNode;
			final HtmlResourceKeeper keeper = new HtmlResourceKeeper(this, el, getImageKeepDir());
			URL url = getFullyQualifiedUrl(imgNode.getAttribute("src"));
			keeper.initial(url, () -> {
				return new HtmlImageResourceAgent(keeper);
			}).keep();

		});
	}

	void processVideoElements() {
		htmlPage.querySelectorAll("video source").stream().filter((node) -> {
			HtmlElement e = (HtmlElement) node;
			return e.hasAttribute("src");
		}).forEach((domnode) -> {
			HtmlElement el = (HtmlElement) domnode;
			final HtmlResourceKeeper keeper = new HtmlResourceKeeper(this, el, getVideoKeepDir());
			URL url = getFullyQualifiedUrl(el.getAttribute("src"));
			keeper.initial(url, () -> {
				return new HtmlVideoResourceAgent(keeper);
			}).keep();
		});
	}

	void processScriptElements() {
		htmlPage.getElementsByTagName("script").stream().filter((domNode) -> {
			return domNode.hasAttribute("src") && !domNode.getAttribute("src").isEmpty();
		}).forEach((scriptNode) -> {

			final HtmlResourceKeeper keeper = new HtmlResourceKeeper(this, (HtmlScript) scriptNode, getJsKeepDir());
			URL url = getFullyQualifiedUrl(scriptNode.getAttribute("src"));
			keeper.initial(url, () -> {
				return new HtmlJsResourceAgent(keeper);
			}).keep();
		});
	}

	/**
	 * get full URL path for the special relative path
	 * @param relative String 
	 * @return URL
	 */
	public URL getFullyQualifiedUrl(String relative) {
		try {
			return this.htmlPage.getFullyQualifiedUrl(relative);
		} catch (Exception e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			return null;
		}
	}

	public File getJsKeepDir() {
		return new File(keepDir, RESOURCE_SCRIPT_DIR);
	}

	public String getJsPath() {
		return RESOURCE_SCRIPT_DIR;
	}

	public File getCssKeepDir() {
		return new File(keepDir, RESOURCE_STYLE_DIR);
	}

	public String getCssPath() {
		return RESOURCE_STYLE_DIR;
	}

	public File getImageKeepDir() {
		return new File(keepDir, RESOURCE_IMAGE_DIR);
	}

	public String getImagePath() {
		return RESOURCE_IMAGE_DIR;
	}

	public File getVideoKeepDir() {
		return new File(keepDir, RESOURCE_VIDEO_DIR);
	}

	public String getVideoPath() {
		return RESOURCE_VIDEO_DIR;
	}

	/**
	 * get the resource which referenced by <code>url()</code> from css save dir 
	 * @return File
	 */
	public File getCssRefKeepDir() {
		return new File(getCssKeepDir(), RESOURCE_STYLE_REFERENCE_DIR);
	}

	public String getCssRefPath() {
		return RESOURCE_STYLE_REFERENCE_DIR;
	}

	public static HtmlPageTransfer create(HtmlPage htmlPage, File keepDir) {
		return new HtmlPageTransfer(htmlPage, keepDir);
	}

	///////////////////////////////////////////////////////////////////////////
	public static final String RESOURCE_STYLE_DIR = "style";
	public static final String RESOURCE_SCRIPT_DIR = "js";
	public static final String RESOURCE_IMAGE_DIR = "images";
	public static final String RESOURCE_VIDEO_DIR = "videos";

	public static final String RESOURCE_STYLE_REFERENCE_DIR = "styleref";
	public static final String RESOURCE_STYLE_REFERENCE_REGEX = "url\\(['\"]?([^\\(\\)'\"]+)['\"]?\\)";
	public static final Pattern RESOURCE_STYLE_REFERENCE_PATTERN = Pattern.compile(RESOURCE_STYLE_REFERENCE_REGEX);
}
