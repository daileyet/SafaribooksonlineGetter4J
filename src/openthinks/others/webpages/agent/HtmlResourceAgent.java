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
* @Title: HtmlResourceAgent.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages.agent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.keeper.HtmlResourceKeeper;
import openthinks.others.webpages.keeper.ResourceKeep;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * The resource agent for HTML page
 * @author dailey.yet@outlook.com
 *
 */
public abstract class HtmlResourceAgent implements ResourceAgent {
	protected HtmlResourceKeeper keeper;

	public HtmlResourceAgent(HtmlResourceKeeper keeper) {
		super();
		this.keeper = keeper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ResourceKeep> T getKeeper() {
		return ((T) keeper);
	}

	/**
	 * persist the text to local file
	 * @param textContent String
	 */
	public void storeTextResource(String textContent) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(keeper.getResourcePath()))) {
			writer.write(textContent);
		} catch (Exception e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	/**
	 * persist the {@link InputStream} to local file
	 * @param ins {@link InputStream}
	 */
	public void storeBinaryResource(InputStream ins) {
		byte[] buff = new byte[1024 * 10];
		try (FileOutputStream fileOutputStream = new FileOutputStream(keeper.getResourcePath());
				BufferedInputStream bufferedInputStream = new BufferedInputStream(ins)) {
			int size = -1;
			while ((size = bufferedInputStream.read(buff)) != -1) {
				fileOutputStream.write(buff, 0, size);
			}
		} catch (IOException e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	@Override
	public void makeKeepToLocal(HtmlElement element) throws IOException {
		WebResponse wrp = this.keeper.loadWebResponse(this.keeper.getResourceURL());
		String text = wrp.getContentAsString("UTF-8");
		storeTextResource(text);
	}

}
