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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.util.LogStringUtil;


/**
 * Ver.6.xの例外ログパーサ
 *
 * @version 1.0.10
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ExceptionLogParserV6 extends ExceptionLogParser {

	private final static String GENERATING_TIME_PREFIX = "generating.time: ";

	private final static String LOG_MESSAGE_PREFIX = "log.message=";

	/** generating.timeのフォーマット */
	private final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

	public ExceptionLogParserV6(ParserParameter parameter) {
		super(parameter);
	}

	public ExceptionLog parse(String string) {

		if (string == null) {
			return null;
		}

		String[] lines = LogStringUtil.lines(string);

		ExceptionLog log = new ExceptionLog();

		log.groupingType = this.parameter.getExceptionGroupingType();

		if (lines.length > 0 && lines[0].startsWith(GENERATING_TIME_PREFIX)) {

			try {
				log.date = dateFormat.parse(lines[0].substring(GENERATING_TIME_PREFIX.length()));
			} catch (ParseException e) {
				warn("invalid date : " + lines[0]);
				this.parameter.getErrorCounter().increment();
				return null;
			}

			if (!isInRange(log)) {
				return null;
			}
		}

		int iLine = 3;

		StringBuilder stackTrace = new StringBuilder();

		boolean begin = false;

		while (iLine < lines.length) {

			String line = lines[iLine++];

			// 例外メッセージに空行が含まれる場合があるので、最初に at .. が来るまでは空行でもスタックとレースとみなす
			if (line.length() == 0 && begin) {
				break;
			}

			if (!begin && line.startsWith("\tat ")) {
				begin = true;
			}

			if (line.startsWith(LOG_MESSAGE_PREFIX)) {
				// まれにスタックトレースの無いログが出る場合がある

				log.message = line.substring(LOG_MESSAGE_PREFIX.length());
				break;
			}

			stackTrace.append(line);
		}

		log.stackTrace = stackTrace.toString();


		while (log.message == null && iLine < lines.length) {

			String line = lines[iLine++];

			if (line.startsWith(LOG_MESSAGE_PREFIX)) {

				log.message = line.substring(LOG_MESSAGE_PREFIX.length());
			}
		}


		return log;
	}

}
