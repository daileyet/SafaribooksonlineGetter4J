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
* @Title: LostConfigureItemException.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 12, 2015
* @version V1.0   
*/
package openthinks.others.webpages.exception;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class LostConfigureItemException extends RuntimeException {

	private static final long serialVersionUID = -7214882904946817958L;

	public LostConfigureItemException() {
		super();
	}

	public LostConfigureItemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LostConfigureItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public LostConfigureItemException(String message) {
		super(message);
	}

	public LostConfigureItemException(Throwable cause) {
		super(cause);
	}

}
