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
* @Title: PageKeeper.java 
* @Package openthinks.others.safari 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 6, 2015
* @version V1.0   
*/
package openthinks.others.htmlunit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class PageKeeper {
	private HtmlPage htmlPage;
	private File keepDir;

	public static final String RESOURCE_STYLE_DIR = "style";
	public static final String RESOURCE_SCRIPT_DIR = "js";
	public static final String RESOURCE_IMAGE_DIR = "images";
	public static final String RESOURCE_STYLE_REFERENCE_DIR = "style\\styleref";
	public static final String RESOURCE_STYLE_REFERENCE_URL = "styleref";

	public static final String RESOURCE_STYLE_REFERENCE_REGEX = "url\\(['\"]?([^\\(\\)'\"]+)['\"]?\\)";
	public static final Pattern RESOURCE_STYLE_REFERENCE_PATTERN = Pattern.compile(RESOURCE_STYLE_REFERENCE_REGEX);

	private PageKeeper(HtmlPage htmlPage, File keepDir) {
		super();
		this.htmlPage = htmlPage;
		this.keepDir = keepDir;
	}

	private PageKeeper(HtmlPage htmlPage) {
		this(htmlPage, new File("."));
	}

	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
	}

	public void setKeepDir(File keepDir) {
		this.keepDir = keepDir;
	}

	private void keepPage() throws FileNotFoundException, IOException {
		String fileName = this.htmlPage.getUrl().toString();
		int end = fileName.indexOf("?");
		int start = fileName.lastIndexOf("/");
		if (end != -1 && start < end)
			fileName = fileName.substring(start + 1, end);
		else
			fileName = fileName.substring(start + 1);
		String htmlContent = this.htmlPage.asXml();
		File keepFile = new File(keepDir, fileName);
		store(htmlContent, keepFile);
	}

	private void keepResources() {
		keepStylesheets(htmlPage.getElementsByTagName("link"));
		keepScripts(htmlPage.getElementsByTagName("script"));
		keepImages(htmlPage.getElementsByTagName("img"));
	}

	private void keepStylesheets(DomNodeList<DomElement> domElements) {
		File styleDir = new File(keepDir, RESOURCE_STYLE_DIR);
		if (!styleDir.exists())
			styleDir.mkdirs();
		for (DomElement element : domElements) {
			if ("stylesheet".equalsIgnoreCase(element.getAttribute("rel")) && !element.getAttribute("href").isEmpty()) {
				String styleUrl = getFullyQualifiedUrl(element.getAttribute("href"));
				if (styleUrl == null) {
					continue;
				}
				String styleName = getResourceName(styleUrl);
				String styleCtx = "";

				try {
					File keepFile = new File(styleDir, styleName);
					checkIfAlreadExist(keepFile);
					WebResponse wrp = getResourceResponse(styleUrl);
					styleCtx = wrp.getContentAsString("UTF-8");
					styleCtx = keepStyleReference(styleCtx).toString();
					store(styleCtx, keepFile);
					element.setAttribute("href", RESOURCE_STYLE_DIR + "/" + styleName);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					element.setAttribute("href", RESOURCE_STYLE_DIR + "/" + styleName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private StringBuffer keepStyleReference(String styleCtx) {
		Matcher matcher = RESOURCE_STYLE_REFERENCE_PATTERN.matcher(styleCtx);
		File refDir = new File(keepDir, RESOURCE_STYLE_REFERENCE_DIR);
		if (!refDir.exists())
			refDir.mkdirs();
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String relativeURL = matcher.group(1);
			String styleRefUrl = getFullyQualifiedUrl(relativeURL);
			String styleRefName = getResourceName(styleRefUrl);
			String styleRefCtx = "";
			File keepFile = new File(refDir, styleRefName);
			WebResponse wrp = null;
			try {
				checkIfAlreadExist(keepFile);
				wrp = getResourceResponse(styleRefUrl);
				if (wrp.getContentType().startsWith("image") || wrp.getContentType().startsWith("IMAGE")) {
					ImageInputStream iis = ImageIO.createImageInputStream(wrp.getContentAsStream());
					Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
					ImageReader imageReader = iter.next();
					imageReader.setInput(iis);
					ImageIO.write(imageReader.read(0), imageReader.getFormatName(), keepFile);
				} else {
					styleRefCtx = wrp.getContentAsString("UTF-8");
					store(styleRefCtx, keepFile);
				}
				matcher.appendReplacement(sb, "url(" + RESOURCE_STYLE_REFERENCE_URL + "/" + styleRefName + ")");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				matcher.appendReplacement(sb, "url(" + RESOURCE_STYLE_REFERENCE_URL + "/" + styleRefName + ")");
			} catch (NoSuchElementException e) {
				if (wrp != null) {
					styleRefCtx = wrp.getContentAsString("UTF-8");
					store(styleRefCtx, keepFile);
					matcher.appendReplacement(sb, "url(" + RESOURCE_STYLE_REFERENCE_URL + "/" + styleRefName + ")");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		matcher.appendTail(sb);
		return sb;
	}

	private void keepScripts(DomNodeList<DomElement> elementsByTag) {
		File tagDir = new File(keepDir, RESOURCE_SCRIPT_DIR);
		if (!tagDir.exists())
			tagDir.mkdirs();
		for (DomElement element : elementsByTag) {
			if (!element.getAttribute("src").isEmpty()) {
				String tagUrl = getFullyQualifiedUrl(element.getAttribute("src"));
				if (tagUrl == null) {
					continue;
				}
				String tagName = getResourceName(tagUrl);
				String tagCtx = "";
				try {
					File keepFile = new File(tagDir, tagName);
					checkIfAlreadExist(keepFile);
					WebResponse wrp = getResourceResponse(tagUrl);
					tagCtx = wrp.getContentAsString("UTF-8");
					store(tagCtx, new File(tagDir, tagName));
					element.setAttribute("src", RESOURCE_SCRIPT_DIR + "/" + tagName);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					element.setAttribute("src", RESOURCE_SCRIPT_DIR + "/" + tagName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void keepImages(DomNodeList<DomElement> elementsByTag) {
		File imgeDir = new File(keepDir, RESOURCE_IMAGE_DIR);
		if (!imgeDir.exists())
			imgeDir.mkdirs();
		for (DomElement element : elementsByTag) {
			HtmlImage htmlImage = (HtmlImage) element;
			String srcUrl = getFullyQualifiedUrl(htmlImage.getAttribute("src"));
			String imgName = srcUrl;
			int start = imgName.lastIndexOf("/");
			imgName = imgName.substring(start + 1);
			imgName = Base64.encodeBase64String(imgName.getBytes());
			if (imgName.indexOf(".") == -1) {
				imgName = imgName + ".jpg";
			}
			try {
				File keepFile = new File(imgeDir, imgName);
				checkIfAlreadExist(keepFile);
				htmlImage.saveAs(new File(imgeDir, imgName));
				htmlImage.setAttribute("src", RESOURCE_IMAGE_DIR + "/" + imgName);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				htmlImage.setAttribute("src", RESOURCE_IMAGE_DIR + "/" + imgName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param tagUrl
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected WebResponse getResourceResponse(String tagUrl) throws MalformedURLException, IOException {
		final WebRequest referringRequest = this.htmlPage.getWebResponse().getWebRequest();
		final WebRequest wrq = new WebRequest(new URL(tagUrl), this.htmlPage.getWebClient().getBrowserVersion()
				.getScriptAcceptHeader());
		wrq.setAdditionalHeaders(new HashMap<>(referringRequest.getAdditionalHeaders()));
		wrq.setAdditionalHeader("Referer", referringRequest.getUrl().toString());
		wrq.setAdditionalHeader("Accept", this.htmlPage.getWebClient().getBrowserVersion().getScriptAcceptHeader());
		WebResponse wrp = this.htmlPage.getWebClient().loadWebResponse(wrq);
		return wrp;
	}

	private String getFullyQualifiedUrl(String relative) {
		try {
			return this.htmlPage.getFullyQualifiedUrl(relative).toString();
		} catch (Exception e) {
			return null;
		}
	}

	private String getResourceName(String url) {
		String name = url;
		int start = name.lastIndexOf("/");
		int end = name.indexOf("?");
		if (end != -1 && start < end)
			name = name.substring(start + 1, end);
		else
			name = name.substring(start + 1);
		return name;
	}

	private void checkIfAlreadExist(File file) {
		if (file != null && file.exists()) {
			throw new IllegalArgumentException("Already download!");
		}
	}

	private void keepTo(File keepDirectory) {
		this.keepDir = keepDirectory;
		if (!keepDir.exists()) {
			keepDir.mkdirs();
		} else if (!keepDir.isDirectory()) {
			throw new IllegalArgumentException("Cann't save to the directory:[" + keepDirectory + "]");
		}
	}

	protected void store(String htmlContent, File keepFile) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(keepFile))) {
			writer.write(htmlContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void print(HtmlPage htmlPage) {
		String htmlContent = htmlPage.getWebResponse().getContentAsString("UTF-8");
		System.out.println(htmlContent);
	}

	static PageKeeper keeper(File saveDir) {
		PageKeeper keeper = new PageKeeper(null, saveDir);
		return keeper;
	}

	static void save(HtmlPage htmPage, File saveDir) {
		Objects.requireNonNull(htmPage);
		Objects.requireNonNull(saveDir);
		PageKeeper keeper = new PageKeeper(htmPage, saveDir);
		keeper.keepTo(saveDir);
		try {
			keeper.keepResources();
			keeper.localAnchorLink();
			keeper.keepPage();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void localAnchorLink() {
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

}
