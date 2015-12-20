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

package net.mikaboshi.intra_mart.tools.log_stats.report;

import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;

/**
 * レポートパラメータ
 *
 * @version 1.0.15
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ReportParameter {

	/**
	 * 解析間隔（分）
	 */
	private long span = 5L;

	/**
	 * セッションタイムアウト時間（分）
	 */
	private int sessionTimeout = 10;

	/**
	 * リクエスト処理時間ランクの出力件数
	 */
	private int requestPageTimeRankSize = 20;

	/**
	 * リクエスト処理時間ランクの閾値（ミリ秒）
	 * @since 1.0.11
	 */
	private long requestPageTimeRankThresholdMillis = -1L;

	/**
	 * リクエストURLランクの出力件数
	 */
	private int requestUrlRankSize = 20;

	/**
	 * セッションランクの出力件数
	 */
	private int sessionRankSize = 20;

	/**
	 * JSSPページパスを表示するかどうか
	 * @since 1.0.11
	 */
	private boolean jsspPath = false;

	/**
	 * 最大同時リクエスト数を表示するかどうか
	 * @since 1.0.13
	 */
	private boolean maxConcurrentRequest = true;

	/**
	 * レポート名
	 */
	private String name = "intra-mart ログ統計レポート";

	/**
	 * 署名
	 */
	private String signature = "";

	/** バージョン  */
	private Version version = Version.V72;

	/**
	 * visualizeのベースURL
	 * @since 1.0.15
	 */
	private String visualizeBaseUrl = "visualize";

	/**
	 * 解析間隔（分）を取得する。
	 * @return
	 */
	public long getSpan() {
		return span;
	}

	/**
	 * 解析間隔（分）を設定する。
	 * @param span
	 */
	public void setSpan(long span) {
		this.span = span;
	}

	/**
	 * セッションタイムアウト時間（分）を取得する。
	 * @return
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * セッションタイムアウト時間（分）を設定する。
	 * @param sessionTimeout
	 */
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * リクエスト処理時間ランクの出力件数を取得する。
	 * @return
	 */
	public int getRequestPageTimeRankSize() {
		return requestPageTimeRankSize;
	}

	/**
	 * リクエスト処理時間ランクの出力件数を設定する。
	 * @param requestPageTimeRankSize
	 */
	public void setRequestPageTimeRankSize(int requestPageTimeRankSize) {
		this.requestPageTimeRankSize = requestPageTimeRankSize;
	}

	/**
	 * リクエストURLランクの出力件数を取得する。
	 * @return
	 */
	public int getRequestUrlRankSize() {
		return requestUrlRankSize;
	}

	/**
	 * リクエスト処理時間ランクの閾値（ミリ秒）を設定する。
	 * @param requestPageTimeRankThresholdMillis
	 * @since 1.0.11
	 */
	public void setRequestPageTimeRankThresholdMillis(long requestPageTimeRankThresholdMillis) {
		this.requestPageTimeRankThresholdMillis = requestPageTimeRankThresholdMillis;
	}

	/**
	 * リクエスト処理時間ランクの閾値（ミリ秒）を取得する。
	 * @return
	 * @since 1.0.11
	 */
	public long getRequestPageTimeRankThresholdMillis() {
		return requestPageTimeRankThresholdMillis;
	}

	/**
	 * リクエストURLランクの出力件数を設定する。
	 * @param requestUrlRankSize
	 */
	public void setRequestUrlRankSize(int requestUrlRankSize) {
		this.requestUrlRankSize = requestUrlRankSize;
	}

	/**
	 * セッションランクの出力件数を設定する。
	 * @return
	 */
	public int getSessionRankSize() {
		return sessionRankSize;
	}

	/**
	 * セッションランクの出力件数を設定する。
	 * @param sessionRankSize
	 */
	public void setSessionRankSize(int sessionRankSize) {
		this.sessionRankSize = sessionRankSize;
	}

	/**
	 * JSSPページパスを表示するかどうかを取得する。
	 * @return
	 * @since 1.0.11
	 */
	public boolean isJsspPath() {
		return jsspPath;
	}

	/**
	 * JSSPページパスを表示するかどうかを設定する。
	 * @param jsspPath
	 * @since 1.0.11
	 */
	public void setJsspPath(boolean jsspPath) {
		this.jsspPath = jsspPath;
	}

	/**
	 * 最大同時リクエスト数を表示するかどうかを取得する。
	 * @return
	 * @since 1.0.13
	 */
	public boolean isMaxConcurrentRequest() {
		return maxConcurrentRequest;
	}

	/**
	 * 最大同時リクエスト数を表示するかどうかを設定する。
	 * @param maxConcurrentRequest
	 * @since 1.0.13
	 */
	public void setMaxConcurrentRequest(boolean maxConcurrentRequest) {
		this.maxConcurrentRequest = maxConcurrentRequest;
	}

	/**
	 * レポート名を取得する
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * レポート名を設定する
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 署名を取得する。
	 * @return
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 署名を設定する。
	 * @param signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getVisualizeBaseUrl() {
		return visualizeBaseUrl;
	}

	public void setVisualizeBaseUrl(String visualizeBaseUrl) {
		this.visualizeBaseUrl = visualizeBaseUrl;
	}
}
