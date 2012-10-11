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

package net.mikaboshi.intra_mart.tools.log_stats.ant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Antのパラメータに関するユーティリティ
 *
 * @version 1.0.9
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public final class AntParameterUtil {

	private AntParameterUtil() {}

	/**
	 * 引数の文字列において、{ と } で囲まれた内部を、{@link SimpleDateFormat}
	 * のパターンとして、システム日時でフォーマットした文字列を返す。
	 *
	 * @param s
	 * @return
	 * @throws IllegalArgumentException フォーマットが不正な場合にスローされる
	 */
	public static String getReportOutput(String s) throws IllegalArgumentException {

		Pattern formatPattern = Pattern.compile("(.+)\\{(.+)\\}(.+)");

		String replaced = s;
		Matcher matcher;
		Date now = new Date();

		while ((matcher = formatPattern.matcher(replaced)).matches()) {

			replaced =
					matcher.group(1) +
					new SimpleDateFormat(matcher.group(2)).format(now) +
					matcher.group(3);
		}

		return replaced;
	}

	/**
	 * 引数の文字列が "today" の場合、システム日付を yyyy/MM/dd でフォーマットした文字列を返す。<br/>
	 * 引数の文字列が "today - n" (n = 0, 1, 2...) の場合、システム日付のn日前の日付を
	 * yyyy/MM/dd でフォーマットした文字列を返す。<br/>
	 * 引数の文字列がその他の場合、引数をそのまま返す。
	 *
	 * @param s
	 * @return
	 */
	public static String parseToday(String s) {

		if (s == null) {
			return s;
		}

		Pattern pattern = Pattern.compile("today(?:\\s*\\-\\s*(\\d+))?");
		Matcher matcher = pattern.matcher(s);

		if (!matcher.matches()) {
			return s;
		}

		Calendar cal = Calendar.getInstance();
		String n = matcher.group(1);

		if (n != null) {
			cal.add(Calendar.DATE, -1 * Integer.parseInt(n));
		}

		return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
	}

}
