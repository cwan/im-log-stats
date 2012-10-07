package net.mikaboshi.intra_mart.tools.log_stats.entity;

import java.util.Date;

/**
 * ログエンティティ
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
