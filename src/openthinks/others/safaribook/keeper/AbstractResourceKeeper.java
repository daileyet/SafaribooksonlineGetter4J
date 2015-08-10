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
* @Title: AbstractResourceKeeper.java 
* @Package openthinks.others.safaribook.keeper 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.safaribook.keeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import openthinks.others.safaribook.ResourceInfo;
import openthinks.others.safaribook.ResourceKeep;
import openthinks.others.safaribook.ResourceKeepListeners;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class AbstractResourceKeeper implements ResourceKeep {
	private ResourceKeepListeners keepListeners = new ResourceKeepListeners();
	protected ResourceInfo resourceInfo;

	@Override
	public void keep() {
		keepListeners.doKeepBefore(this);
		try {
			checkIfExist();
			doKeep();
		} catch (Exception e) {
			e.printStackTrace();
		}
		keepListeners.doKeepAfter(this);
	}

	protected void checkIfExist() {
		File keepPath = new File(this.getResourcePath());
		if (keepPath.isFile() && keepPath.getParentFile().isDirectory() || !keepPath.getParentFile().exists()) {
			keepPath.getParentFile().mkdirs();
		}
	}

	/**
	 * the really to do keep resource action
	 */
	protected abstract void doKeep() throws Exception;

	protected abstract WebResponse loadWebResponse(URL url) throws IOException;

	protected void store(String textContent, String keepFile) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(keepFile))) {
			writer.write(textContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResourceInfo resourceInfo() {
		return resourceInfo;
	}
}
