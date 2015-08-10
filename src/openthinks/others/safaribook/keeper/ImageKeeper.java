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
* @Title: ImageKeeper.java 
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
import java.util.Iterator;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ImageKeeper extends BinaryResourceKeeper {
	private HtmlPageKeeper pageKeeper;
	private HtmlImage htmlImage;

	public ImageKeeper(HtmlPageKeeper pageKeeper) {
		super();
		this.pageKeeper = pageKeeper;

	}

	public ImageKeeper(HtmlPageKeeper pageKeeper, HtmlImage htmlImage) {
		super();
		this.pageKeeper = pageKeeper;
		this.htmlImage = htmlImage;
	}

	@Override
	protected WebResponse loadWebResponse(URL url) throws IOException {
		if (pageKeeper == null && htmlImage != null && htmlImage.getPage().isHtmlPage()) {
			return loadWebResponse((HtmlPage) htmlImage.getPage(), url);
		}
		Objects.requireNonNull(pageKeeper);
		return pageKeeper.loadWebResponse(url);
	}

	private WebResponse loadWebResponse(HtmlPage htmlPage, URL url) throws IOException {
		final WebRequest referringRequest = htmlPage.getWebResponse().getWebRequest();
		final WebRequest wrq = new WebRequest(url, htmlPage.getWebClient().getBrowserVersion().getScriptAcceptHeader());
		wrq.setAdditionalHeaders(new HashMap<>(referringRequest.getAdditionalHeaders()));
		wrq.setAdditionalHeader("Referer", referringRequest.getUrl().toString());
		wrq.setAdditionalHeader("Accept", htmlPage.getWebClient().getBrowserVersion().getScriptAcceptHeader());
		WebResponse wrp = htmlPage.getWebClient().loadWebResponse(wrq);
		return wrp;
	}

	@Override
	protected void doKeep() throws Exception {
		try {
			if (htmlImage != null) {
				htmlImage.saveAs(new File(this.getResourcePath()));
			} else {
				WebResponse wrp = loadWebResponse(this.getResourceURL());
				ImageInputStream iis = ImageIO.createImageInputStream(wrp.getContentAsStream());
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
				ImageReader imageReader = iter.next();
				imageReader.setInput(iis);
				ImageIO.write(imageReader.read(0), imageReader.getFormatName(), new File(this.getResourcePath()));
			}
		} catch (Exception e) {
			super.doKeep();
		}

	}
}
