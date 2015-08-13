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
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 10, 2015
* @version V1.0   
*/
package openthinks.others.webpages.keeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.others.webpages.exception.ResourceAlreadyExistException;
import openthinks.others.webpages.util.ProcessLoger;
import openthinks.others.webpages.util.ResourceInfo;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class AbstractResourceKeeper implements ResourceKeep {
	private ResourceKeepListeners keepListeners = new ResourceKeepListeners();
	protected ResourceInfo resourceInfo;

	public void addResourceKeepListener(ResourceKeepListener keepListener) {
		keepListeners.add(keepListener);
	}

	@Override
	public void keep() {
		keepListeners.doKeepBefore(this);
		try {
			checkIfExist();
			doKeep();
			doChange();
		} catch (ResourceAlreadyExistException e) {
			ProcessLoger.debug(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			doChange();
		} catch (Exception e) {
			ProcessLoger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
		keepListeners.doKeepAfter(this);
	}

	protected abstract void doChange();

	protected void checkIfExist() {
		File keepPath = new File(this.getResourcePath());
		if (keepPath.exists()) {
			throw new ResourceAlreadyExistException(keepPath.getAbsolutePath() + " was already download.");
		}
		if (keepPath.isFile() && keepPath.getParentFile().isDirectory() || !keepPath.getParentFile().exists()) {
			keepPath.getParentFile().mkdirs();
		}
	}

	/**
	 * the really to do keep resource action
	 */
	protected abstract void doKeep() throws Exception;

	protected abstract WebResponse loadWebResponse(URL url) throws IOException;

	@Override
	public ResourceInfo resourceInfo() {
		return resourceInfo;
	}
}
