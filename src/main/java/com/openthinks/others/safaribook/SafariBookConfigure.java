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
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package com.openthinks.others.safaribook;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;

import com.openthinks.others.webpages.WebPagesConfigure;

/**
 * Safari book online getter configure
 * 
 * @author dailey.yet@outlook.com
 *
 */
public class SafariBookConfigure extends WebPagesConfigure {
	private static final long serialVersionUID = 9199073815096246950L;
	public static final String ATTR_VALUE_SPLIT_TOKEN = ";";
	public static final String BOOKNAME = "safaribook-name";
	// group task which need go to download book
	public static final String DOWNLOADGROUPTASKDIR = "group-task-dir";
	private transient DownloadBookTaskInfo bookTaskInfo=null;

	public static final SafariBookConfigure create() {
		return new SafariBookConfigure();
	}

	public static final SafariBookConfigure readXML(InputStream is)
			throws InvalidPropertiesFormatException, IOException {
		SafariBookConfigure instance = SafariBookConfigure.create();
		instance.loadFromXML(is);
		return instance;
	}

	public static final SafariBookConfigure readProps(InputStream is) throws IOException {
		SafariBookConfigure instance = SafariBookConfigure.create();
		instance.load(is);
		return instance;
	}

	public Optional<String> getBookName() {
		return getProp(BOOKNAME);
	}

	public void setBookName(String value) {
		setProperty(BOOKNAME, value);
	}

	public Optional<String> getGroupTaskDir() {
		return getProp(DOWNLOADGROUPTASKDIR);
	}

	public void setGroupTaskDir(String value) {
		setProperty(DOWNLOADGROUPTASKDIR, value);
	}
	
	public void setBookTaskInfo(final DownloadBookTaskInfo bookTaskInfo) {
		this.bookTaskInfo = bookTaskInfo;
	}
	
	public Optional<DownloadBookTaskInfo> getBookTaskInfo() {
		return Optional.ofNullable(bookTaskInfo);
	}

}