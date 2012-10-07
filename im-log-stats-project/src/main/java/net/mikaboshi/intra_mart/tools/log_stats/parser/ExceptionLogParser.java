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

import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;

/**
 * 例外ログパーサ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public abstract class ExceptionLogParser extends GenericLogParser implements LogParser<ExceptionLog> {

	public ExceptionLogParser(ParserParameter parameter) {
		super(parameter);
	}

	public static ExceptionLogParser create(ParserParameter parameter) {

		Version version = parameter.getVersion();

		if (version.isVersion6()) {

			return new ExceptionLogParserV6(parameter);

		} else if (version.isVersion7()) {

			return new ExceptionLogParserV7(parameter);
		}

		throw new IllegalArgumentException("Unsupported version : " + version);
	}
}
