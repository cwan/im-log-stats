package net.mikaboshi.intra_mart.tools.log_stats.entity;

/**
 * 画面遷移ログ
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
