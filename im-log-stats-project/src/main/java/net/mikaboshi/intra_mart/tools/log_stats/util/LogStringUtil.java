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

package net.mikaboshi.intra_mart.tools.log_stats.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * ログ文字列に関するユーティリティクラス
 *
 * @version 1.0.21
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public final class LogStringUtil {

	private static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\\r?\\n");

	private static final String[] ENPTY_STRING_ARRAY = new String[0];

	private static final Pattern TRUNCATE_URL_PATTERN = Pattern.compile("(?:[^(\\:/)]+\\:)?//(?:[^(\\:/)]+)(?:\\:\\d+)?(/.+)");

	private LogStringUtil() {}

	/**
	 * 引数を改行コードで分割する。
	 *
	 * @param s
	 * @return 引数を分割した文字列の配列。引数がnullの場合は、空の配列を返す。
	 */
	public static String[] lines(CharSequence s) {

		if (s == null) {
			return ENPTY_STRING_ARRAY;
		}

		return LINE_SPLIT_PATTERN.split(s);
	}

	/**
	 * URLからスキーム、ホスト、ポートを削除する。
	 * @param url
	 * @return
	 */
	public static String truncateUrl(String url) {

		if (url == null) {
			return StringUtils.EMPTY;
		}

		Matcher matcher = TRUNCATE_URL_PATTERN.matcher(url);

		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			return url;
		}
	}

	/**
	 * セッションID、ユーザIDが有効ならばtrueを返す。
	 * @since 1.0.21
	 * @param id
	 * @return
	 */
	public static boolean isValidId(String id) {
		return StringUtils.isNotBlank(id) && !"-".equals(id.trim());
	}

}
