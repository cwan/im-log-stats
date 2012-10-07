/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.mikaboshi.intra_mart.tools.log_stats.entity;

/**
 * ログレベル
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public enum Level {

	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR;

	public static Level toEnum(String s) {

		if (s == null) {
			return null;
		}

		s = s.trim().toUpperCase();

		if ("INFO".equals(s)) {
			return INFO;
		} else if ("ERROR".equals(s)) {
			return ERROR;
		} else if ("WARN".equals(s)) {
			return WARN;
		} else if ("DEBUG".equals(s)) {
			return DEBUG;
		} else if ("TRACE".equals(s)) {
			return TRACE;
		} else {
			return null;
		}
	}
}
