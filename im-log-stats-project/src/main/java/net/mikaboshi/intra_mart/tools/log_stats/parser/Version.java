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

package net.mikaboshi.intra_mart.tools.log_stats.parser;

/**
 * ログファイルのバージョン
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public enum Version {

	V60("6.0"),

	V61("6.1"),

	V70("7.0"),

	V71("7.1"),

	V72("7.2");

	private final String name;

	private Version(String name) {
		this.name = name;
	}

	public static Version toEnum(String name) {

		for (Version version : Version.values()) {
			if (version.name.equals(name)) {
				return version;
			}
		}

		return null;
	}

	public boolean isVersion6() {
		return this == V60 || this == V61;
	}

	public boolean isVersion7() {
		return this == V70 || this == V71 || this == V72;
	}

	public String getName() {
		return name;
	}
}
