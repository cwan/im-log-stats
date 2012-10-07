package net.mikaboshi.intra_mart.tools.log_stats.report;

import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;

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
	 * リクエストURLランクの出力件数
	 */
	private int requestUrlRankSize = 20;
	
	/**
	 * セッションランクの出力件数
	 */
	private int sessionRankSize = 20;
	
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
}
