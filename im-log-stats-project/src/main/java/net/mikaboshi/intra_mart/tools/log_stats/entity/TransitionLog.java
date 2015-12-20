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
 * 画面遷移ログ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class TransitionLog extends Log {

	/**
	 * 遷移タイプ
	 */
	public TransitionType type = null;

	/**
	 * リモートホスト
	 */
	public String requestRemoteHost = null;

	/**
	 * リモートアドレス
	 */
	public String requestRemoteAddress = null;

	/**
	 * ユーザID
	 */
	public String transitionAccessUserId = null;

	/**
	 * 遷移先パス
	 */
	public String transitionPathPageNext = null;

	/**
	 * 応答時間
	 */
	public long transitionTimeResponse = 0L;

	/**
	 * 例外名
	 */
	public String transitionExceptionName = null;

	/**
	 * 例外メッセージ
	 */
	public String transitionExceptionMessage = null;

	/**
	 * 遷移元パス
	 */
	public String transitionPathPagePrevious = null;

	@Override
	public String getUrl() {
		return this.transitionPathPageNext;
	}
}
