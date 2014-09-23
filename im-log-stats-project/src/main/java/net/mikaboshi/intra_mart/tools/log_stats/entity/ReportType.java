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
 * レポート種別
 *
 * @version 1.0.10
 * @since 1.0.10
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public enum ReportType {

	HTML,
	CSV,
	TSV,
	VISUALIZE,
	TEMPLATE;

	public static ReportType toEnum(String s) {

		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		if ("html".equals(s)) {
			return HTML;
		} else if ("csv".equals(s)) {
			return CSV;
		} else if ("tsv".equals(s)) {
			return TSV;
		} else if ("visualize".equals(s)) {
			return VISUALIZE;
		} else if ("template".equals(s)) {
			return TEMPLATE;
		} else {
			return null;
		}
	}
}
