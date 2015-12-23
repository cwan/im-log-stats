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
 * @version 1.0.18
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public enum Version {

	V60("6.0"),

	V61("6.1"),

	V70("7.0"),

	V71("7.1"),

	V72("7.2"),

	V80("8.0"),

	V800("8.0.0"),

	V801("8.0.1"),

	V802("8.0.2"),

	V803("8.0.3"),

	V804("8.0.4"),

	V805("8.0.5"),

	V806("8.0.6"),

	V807("8.0.7"),	// 2014 Spring

	V808("8.0.8"),	// 2014 Summer

	V809("8.0.9"),	// 2014 Winter

	V8010("8.0.10"),	// 2015 Spring

	V8011("8.0.11"),	// 2015 Summer

	V8012("8.0.12");	// 2015 Winter


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
		return !isVersion6();
	}

	public String getName() {
		return name;
	}
}
