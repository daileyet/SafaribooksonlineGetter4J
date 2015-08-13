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
* @Title: ProcessLoger.java 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 11, 2015
* @version V1.0   
*/
package openthinks.others.webpages.util;

/**
 * Simple process logger
 * @author dailey.yet@outlook.com
 *
 */
public class ProcessLoger {
	public static PLLevel currentLevel = PLLevel.DEBUG;
	public static final PLLevel defaultLevel = PLLevel.INFO;

	static PLLevel currentLevel() {
		return currentLevel == null ? defaultLevel : currentLevel;
	}

	public enum PLLevel {
		FATAL, ERROR, WARN, INFO, DEBUG
	}

	public static void log(PLLevel plevel, String... msgs) {
		switch (plevel) {
		case DEBUG:
			_debug(msgs);
			break;
		case INFO:
			_info(msgs);
			break;
		case WARN:
			_warn(msgs);
			break;
		case ERROR:
			_error(msgs);
			break;
		case FATAL:
			_fatal(msgs);
			break;
		default:
			break;
		}
	}

	public static void debug(String... msgs) {
		if (currentLevel().compareTo(PLLevel.DEBUG) >= 0)
			_debug(msgs);
	}

	public static void info(String... msgs) {
		if (currentLevel().compareTo(PLLevel.INFO) >= 0)
			_info(msgs);
	}

	public static void warn(String... msgs) {
		if (currentLevel().compareTo(PLLevel.WARN) >= 0)
			_warn(msgs);
	}

	public static void error(String... msgs) {
		if (currentLevel().compareTo(PLLevel.ERROR) >= 0)
			_error(msgs);
	}

	public static void fatal(String... msgs) {
		if (currentLevel().compareTo(PLLevel.FATAL) >= 0)
			_fatal(msgs);
	}

	private static void _debug(String... msgs) {

		System.out.print("DEBUG=>");
		for (String msg : msgs) {
			System.out.print(msg);
			System.out.print("  ");
		}
		System.out.println();
	}

	private static void _info(String... msgs) {
		System.out.print("INFO=>");
		for (String msg : msgs) {
			System.out.print(msg);
			System.out.print("  ");
		}
		System.out.println();
	}

	private static void _warn(String... msgs) {
		System.out.print("WARN=>");
		for (String msg : msgs) {
			System.out.print(msg);
			System.out.print("  ");
		}
		System.out.println();
	}

	private static void _error(String... msgs) {
		System.err.print("ERROR=>");
		for (String msg : msgs) {
			System.err.print(msg);
			System.err.print("  ");
		}
		System.err.println();
	}

	private static void _fatal(String... msgs) {
		System.err.print("FATAL=>");
		for (String msg : msgs) {
			System.err.print(msg);
			System.err.print("  ");
		}
		System.err.println();
	}

}
