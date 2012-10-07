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

import java.util.Date;

/**
 * リクエストログ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class RequestLog extends Log {

	/**
	 * リモートホスト
	 */
	public String requestRemoteHost = null;

	/**
	 * リモートアドレス
	 */
	public String requestRemoteAddress = null;

	/**
	 * HTTPメソッド
	 */
	public String requestMethod = null;

	/**
	 * リクエストURL
	 */
	public String requestUrl = null;

	/**
	 * クエリ文字列
	 */
	public String requestQueryString = null;

	/**
	 * リファラ
	 */
	public String requestUrlReferer = null;

	/**
	 *  ページの処理時間（ミリ秒）
	 */
	public long requestPageTime = 0L;

	/**
	 * リクエストの受付時刻
	 */
	public Date requestAcceptTime = null;

	/**
	 * クエリ文字列の値を取得する。
	 * @param name
	 * @return
	 */
	public String getQueryValue(String name) {
		if (this.requestQueryString == null) {
			return null;
		}

		for (String param : this.requestQueryString.split("&")) {
			if (param.startsWith(name + "=")) {
				return param.substring(name.length() + 1);
			}
		}

		return null;
	}

	/**
	 * クエリ文字列im_actionの値を取得する。
	 * @return
	 */
	public String getImAction() {
		return getQueryValue("im_action");
	}

	/**
	 * im_action付きのリクエストURLを取得する。
	 * @return
	 */
	public String getRequestUrlWithImAction() {
		if (this.requestUrl == null) {
			return null;
		}

		String imAction = getImAction();

		if (imAction == null) {
			return this.requestUrl;
		} else {
			return this.requestUrl + "?im_action=" + imAction;
		}
	}

	/**
	 * 受付時のリクエストログならばtrueを返す。
	 * @return
	 */
	public boolean isIN() {

		return this.level == Level.DEBUG ||
				this.message != null && this.message.trim().equals("IN");
	}

	@Override
	public String getUrl() {
		return this.requestUrl;
	}
}
