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
* @Title: SafariBookConfigure.java 
* @Package openthinks.others.safaribook 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.safaribook;

import java.io.File;
import java.util.Optional;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class SafariBookConfigure {

	public BrowserVersion getBrowserVersion() {
		return BrowserVersion.FIREFOX_38;
	}

	public Optional<String> getProxyHost() {
		return Optional.of("cn-proxy.jp.oracle.com");
	}

	public int getProxyPort() {
		return 80;
	}

	public Optional<String> getLoginPageUrl() {
		return Optional.of("https://www.safaribooksonline.com/accounts/login");
	}

	public Optional<String> getLoginFormSelector() {
		return Optional.of("");
	}

	public int getLoginFormIndex() {
		return 0;
	}

	public Optional<String> getLoginSubmitBtnName() {
		return Optional.of("login");
	}

	public Optional<String> getLoginAuthInputName() {
		return Optional.of("email");
	}

	public Optional<String> getLoginAuthInputValue() {
		return Optional.of("dailey.dai@oracle.com");
	}

	public Optional<String> getLoginAuthPassInputName() {
		return Optional.of("password1");
	}

	public Optional<String> getLoginAuthPassInputValue() {
		return Optional.of("dmj123");
	}

	public Optional<File> getKeepDir() {
		return Optional.of(new File("W:\\Book\\angularjs-up-and-running"));
		//		return Optional.of(new File("W:\\Book\\learn-javafx-8"));
	}

	public Optional<String> getCatalogPageUrl() {
		return Optional.of("http://techbus.safaribooksonline.com/book/web-development/9781491901939");
	}

	public Optional<String> getPageLinkOfCatalogSelector() {
		return Optional.of("div.catalog_container a[href*='9781491901939']");
	}

	public Optional<String> getStartChainPageUrl() {
		//		return "http://techbus.safaribooksonline.com/book/web-development/9781491901939/angularjs-up-and-running/index_html";

		return Optional
				.of("http://techbus.safaribooksonline.com/book/programming/java/9781484211427/cover/9781484211434_fm_0_cover_xhtml");
	}

	public Optional<String> getNextChainPageAnchorSelector() {
		return Optional.of("a.next[title*='Next (Key: n)']");
	}

	public Optional<Boolean> needLogin() {
		return Optional.ofNullable(true);
	}

}
