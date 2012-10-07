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
 * ログエンティティ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public abstract class Log {

	/**
	 * ロガー名
	 */
	public String logger = null;

	/**
	 * ログ出力日時
	 */
	public Date date = null;

	/**
	 * ログメッセージ
	 */
	public String message = null;

	/**
	 * ログレベル
	 */
	public Level level = null;

	/**
	 * スレッドID
	 */
	public String thread = null;

	/**
	 * リクエストID
	 */
	public String requestId = null;

	/**
	 * ログID
	 */
	public String logId = null;

	/**
	 * ログ出力順序番号
	 */
	public int logReportSequence = 0;

	/**
	 * スレッドグループ名
	 */
	public String logThreadGroup = null;

	/**
	 * セッションID
	 */
	public String clientSessionId = null;

	/**
	 * アクセスURLを取得する。
	 * @return
	 */
	public String getUrl() {
		return null;
	}
}
