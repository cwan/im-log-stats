package net.mikaboshi.intra_mart.tools.log_stats.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;


/**
 * 時間分割統計
 */
public class TimeSpanStatistics {

	/**
	 * リクエスト数合計
	 */
	private int requestCount = 0;

	/**
	 * リクエスト処理時間のリスト
	 */
	private List<Long> requestPageTimes = new ArrayList<Long>();

	/**
	 * ユニークなセッションID
	 */
	private Set<String> uniqueSessionIds = new HashSet<String>();

	/**
	 * 画面遷移数合計
	 */
	private int transitionCount = 0;

	/**
	 * ユニークなユーザID
	 */
	private Set<String> uniqueUserIds = new HashSet<String>();

	/**
	 * 画面遷移例外数
	 */
	private int transitionExceptionCount = 0;

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）
	 */
	private int activeSessionCount = 0;

	/**
	 * 例外数
	 */
	private int exceptionCount = 0;

	/**
	 * スパン開始日時
	 */
	private Date startDate = null;

	/**
	 * 画面遷移ログのみ（リクエストログなし）ならばtrue
	 */
	private final boolean transitionLogOnly;

	/**
	 * コンストラクタ
	 * @param transitionLogOnly 画面遷移ログのみ（リクエストログなし）かどうか
	 */
	public TimeSpanStatistics(boolean transitionLogOnly) {
		this.transitionLogOnly = transitionLogOnly;
	}

	/**
	 * リクエストログを追加する
	 * @param log
	 */
	public void add(RequestLog log) {

		if (log == null) {
			return;
		}

		this.requestCount++;
		this.requestPageTimes.add(log.requestPageTime);
		this.uniqueSessionIds.add(log.clientSessionId);
	}

	/**
	 * 画面遷移ログを追加する
	 * @param log
	 */
	public void add(TransitionLog log) {

		if (log == null) {
			return;
		}

		this.transitionCount++;
		this.uniqueSessionIds.add(log.clientSessionId);
		this.uniqueUserIds.add(log.transitionAccessUserId);

		if (this.transitionLogOnly && log.type == TransitionType.REQUEST) {
			this.requestCount++;
			this.requestPageTimes.add(log.transitionTimeResponse);
			this.uniqueSessionIds.add(log.clientSessionId);
		}

		if (!"-".equals(log.transitionExceptionName)) {
			this.transitionExceptionCount++;
		}
	}

	/**
	 * 例外ログを追加する
	 * @param log
	 */
	public void add(ExceptionLog log) {

		if (log == null) {
			return;
		}

		this.exceptionCount++;
	}

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）を設定する
	 * @param activeSessionCount
	 */
	public void setActiveSessionCount(int activeSessionCount) {
		this.activeSessionCount = activeSessionCount;
	}

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）を取得する
	 * @return
	 */
	public int getActiveSessionCount() {
		return activeSessionCount;
	}

	/**
	 * スパン開始日時を設定する。
	 * @param startDate スパン開始日時
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * スパン開始日時を取得する。
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * リクエスト数を取得する。
	 * @return
	 */
	public int getRequestCount() {
		return requestCount;
	}

	/**
	 * 画面遷移数を取得する。
	 * @return
	 */
	public int getTransitionCount() {
		return transitionCount;
	}

	/**
	 * ユニークセッション数を取得する。
	 * @return
	 */
	public int getUniqueSessionCount() {
		return this.uniqueSessionIds.size();
	}

	/**
	 * ユニークユーザ数を取得する。
	 * @return
	 */
	public int getUniqueUserCount() {
		return this.uniqueUserIds.size();
	}

	/**
	 * リクエスト処理時間のリストを取得する。
	 */
	public List<Long> getRequestPageTimes() {
		return requestPageTimes;
	}

	/**
	 * 例外数合計を取得する。
	 * @return
	 */
	public int getExceptionCount() {
		return exceptionCount;
	}

	/**
	 * 画面遷移例外数合計を取得する。
	 * @return
	 */
	public int getTransitionExceptionCount() {
		return transitionExceptionCount;
	}

}
