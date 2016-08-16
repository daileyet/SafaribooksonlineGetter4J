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
* @Title: HtmlImageResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package com.openthinks.others.webpages.agent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;
import com.openthinks.others.webpages.keeper.HtmlResourceKeeper;
import com.openthinks.others.webpages.util.ResourceType;

/**
 * The image resource agent for HTML page
 * @author dailey.yet@outlook.com
 * @see HtmlCssImageResourceAgent
 */
public class HtmlImageResourceAgent extends HtmlBinaryResourceAgent {

	public HtmlImageResourceAgent(HtmlResourceKeeper keeper) {
		super(keeper);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.IMAGE;
	}

	@Override
	public void makeKeepToLocal(HtmlElement element) throws IOException {
		try {
			WebResponse wrp = keeper.loadWebResponse(keeper.getResourceURL());
			if ("image/svg+xml".equalsIgnoreCase(wrp.getContentType())) {//issue for svg image
				throw new Exception("SVG image.");
			}
			ImageInputStream iis = ImageIO.createImageInputStream(wrp.getContentAsStream());
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (iter.hasNext()) {
				ImageReader imageReader = iter.next();
				imageReader.setInput(iis);
				ImageIO.write(imageReader.read(0), imageReader.getFormatName(), new File(keeper.getResourcePath()));
			}
		} catch (Exception e) {
			ProcessLogger.info(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			WebResponse wrp = keeper.loadWebResponse(keeper.getResourceURL());
			storeBinaryResource(wrp.getContentAsStream());
		}

	}

	@Override
	public void makeChangeToLocal(HtmlElement element) {
		element.setAttribute("src", pageTransfer().getImagePath() + "/" + keeper.getResourceName());
	}

	@Override
	public String resolve(URL url) {
		String imgName = url.toString();
		int start = imgName.lastIndexOf("/");
		imgName = imgName.substring(start + 1);
		imgName = imgName.replaceAll("[^0-9A-Za-z_\\-\\.]", "_");
		String imgSuffix = ".jpg";
		int end = imgName.lastIndexOf(".");
		if (end != -1) {
			imgSuffix = imgName.substring(end);
			imgName = imgName.substring(0, end);
		}
		if (keeper.nameEncode())
			imgName = Base64.encodeBase64String(imgName.getBytes());
		return imgName + imgSuffix;
	}

}
