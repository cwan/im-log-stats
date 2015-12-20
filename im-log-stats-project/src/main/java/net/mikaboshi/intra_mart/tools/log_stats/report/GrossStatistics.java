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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest;
import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest.EventType;
import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.Log;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;
import net.mikaboshi.intra_mart.tools.log_stats.util.LongListFactory;
import net.mikaboshi.intra_mart.tools.log_stats.util.SingleIntFactory;

import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.lang.StringUtils;

/**
 * 全体の統計情報
 *
 * @version 1.0.16
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class GrossStatistics {

	/**
	 * リクエストURL(im_action付き） => 処理時間
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Long>> urlPageTimesMap = LazyMap.decorate(
			new HashMap<String, List<Long>>(),
			LongListFactory.INSTANCE);

	/**
	 * 例外ログ => 件数
	 */
	@SuppressWarnings("unchecked")
	private Map<ExceptionLog, int[]> exceptionCountMap = LazyMap.decorate(
			new HashMap<ExceptionLog, int[]>(),
			SingleIntFactory.INSTANCE);

	/**
	 * 処理時間順のリクエストランク
	 */
	private final RequestEntry[] requestPageTimeRank;

	/**
	 * 処理時間ランクの閾値（ms）
	 * @since 1.0.11
	 */
	private final long requestPageTimeRankThresholdMillis;

	/**
	 * 処理時間順のリクエストランクリスト。
	 * requestPageTimeRankThresholdMillisを指定した場合に使用する。
	 * @since 1.0.11
	 */
	private final List<RequestEntry> requestPageTimeRankList;

	/**
	 * リクエスト数合計
	 */
	private int requestCount = 0;

	/**
	 * セッション情報
	 */
	private SessionMap sessionMap = new SessionMap();

	/**
	 * 画面遷移ログのみ（リクエストログなし）ならばtrue
	 */
	private final boolean transitionLogOnly;

	/** 処理時間合計 */
	private long totalPageTime = 0L;

	/**
	 * 最大同時リクエスト数を表示するかどうか
	 * @since 1.0.13
	 */
	private boolean maxConcurrentRequest = false;

	/**
	 * 最大同時リクエスト数集計トリスト
	 * @since 1.0.13
	 */
	private List<ConcurrentRequest> concurrentRequestList = new ArrayList<ConcurrentRequest>();

	/**
	 * 最大同時リクエスト数集計トリスト（ソート済み）
	 * @since 1.0.16
	 */
	private List<ConcurrentRequest> sortedConcurrentRequestList = null;

	/**
	 * 処理時間順のリクエストランクサイズを指定するコンストラクタ
	 * @param requestPageTimeRankSize 処理時間順のリクエストランクサイズ
	 * @param transitionLogOnly 画面遷移ログのみ（リクエストログなし）かどうか
	 */
	public GrossStatistics(int requestPageTimeRankSize, boolean transitionLogOnly) {
		this.requestPageTimeRank = new RequestEntry[requestPageTimeRankSize];
		this.transitionLogOnly = transitionLogOnly;
		this.requestPageTimeRankThresholdMillis = -1L;
		this.requestPageTimeRankList = null;
	}

	/**
	 * 処理時間順のリクエストランクの閾値（ミリ秒）を指定するコンストラクタ
	 * @param requestPageTimeRankThresholdMillis
	 * @param transitionLogOnly
	 * @since 1.0.11
	 */
	public GrossStatistics(long requestPageTimeRankThresholdMillis, boolean transitionLogOnly) {

		if (requestPageTimeRankThresholdMillis < 0) {
			throw new IllegalArgumentException("requestPageTimeRankThresholdMillis must be >= 0.");
		}

		this.requestPageTimeRank = null;
		this.transitionLogOnly = transitionLogOnly;
		this.requestPageTimeRankThresholdMillis = requestPageTimeRankThresholdMillis;
		this.requestPageTimeRankList = new ArrayList<GrossStatistics.RequestEntry>();
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
	 * リクエストログを追加する。
	 * @param log
	 */
	public void add(RequestLog log) {

		if (log == null) {
			return;
		}

		addRequestInfo(log, log.getRequestUrlWithImAction(), log.requestPageTime);

		if (this.maxConcurrentRequest) {
			// 最大同時リクエスト数集計のため、受信時刻・返信時刻を記録する
			if (log.requestAcceptTime != null) {
				this.concurrentRequestList.add(
						new ConcurrentRequest(EventType.ACCEPT_TIME, log.requestAcceptTime.getTime(), log.tenantId));
			}

			if (log.date != null) {
				this.concurrentRequestList.add(
						new ConcurrentRequest(EventType.RESPONSE_TIME, log.date.getTime(), log.tenantId));
			}
		}
	}

	/**
	 * 画面遷移ログを追加する。
	 * @param log
	 */
	public void add(TransitionLog log) {

		if (log == null) {
			return;
		}

		this.sessionMap.putUserId(log.clientSessionId, log.transitionAccessUserId);

		if (this.transitionLogOnly && log.type == TransitionType.REQUEST) {
			// 画面遷移ログ（type=REQUEST）からリクエスト統計を構築する

			addRequestInfo(log, log.transitionPathPageNext, log.transitionTimeResponse);

			if (this.maxConcurrentRequest && log.date != null) {
				// 最大同時リクエスト数集計のため、受信時刻・返信時刻を記録する

				long responseTime = log.date.getTime();
				long acceptTime = responseTime - log.transitionTimeResponse;

				this.concurrentRequestList.add(
						new ConcurrentRequest(EventType.ACCEPT_TIME, acceptTime, log.tenantId));

				this.concurrentRequestList.add(
						new ConcurrentRequest(EventType.RESPONSE_TIME, responseTime, log.tenantId));
			}
		}

		registerSessionAccess(log);
	}

	/**
	 * 例外ログを追加する。
	 * @param log
	 */
	public void add(ExceptionLog log) {

		if (log == null) {
			return;
		}

		if (StringUtils.isNotEmpty(log.stackTrace)) {
			this.exceptionCountMap.get(log)[0]++;
		}
	}

	/**
	 * リクエスト数合計を取得する。
	 * @return
	 */
	public int getRequestCount() {
		return requestCount;
	}

	/**
	 * 例外件数マップを取得する。
	 * @return
	 */
	public Map<ExceptionLog, int[]> getExceptionCountMap() {
		return exceptionCountMap;
	}

	/**
	 * リクエストURL(im_action付き） => 処理時間マップを取得する。
	 * @return
	 */
	public Map<String, List<Long>> getUrlPageTimesMap() {
		return urlPageTimesMap;
	}

	/**
	 * 処理時間順のリクエストランクを取得する。
	 * @return
	 */
	public RequestEntry[] getRequestPageTimeRank() {

		if (this.requestPageTimeRank != null) {
			return this.requestPageTimeRank;

		} else {
			// レスポンスタイムでソートして返す

			Collections.sort(this.requestPageTimeRankList, new Comparator<RequestEntry>() {

				public int compare(RequestEntry e1, RequestEntry e2) {

					if (e1.time > e2.time) {
						return -1;
					} else if (e1.time < e2.time) {
						return 1;
					} else {
						return 0;
					}
				}
			});

			return this.requestPageTimeRankList.toArray(new RequestEntry[this.requestPageTimeRankList.size()]);
		}
	}

	/**
	 * セッション情報を取得する。
	 * @return
	 */
	public SessionMap getSessionMap() {
		return sessionMap;
	}

	/**
	 * 処理時間合計を取得する。
	 * @return
	 * @since 1.0.10
	 */
	public long getTotalPageTime() {
		return totalPageTime;
	}

	/**
	 * 同時リクエスト数リストを取得する。
	 * @return
	 * @since 1.0.13
	 */
	public List<ConcurrentRequest> getConcurrentRequestList() {

		if (this.sortedConcurrentRequestList != null) {
			return this.sortedConcurrentRequestList;
		}

		// 時刻でソート
		Collections.sort(this.concurrentRequestList, new Comparator<ConcurrentRequest>() {

			public int compare(ConcurrentRequest e1, ConcurrentRequest e2) {

				if (e1.getTime() < e2.getTime()) {
					return -1;
				} else if (e1.getTime() > e2.getTime()) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		int count = 0;

		// その時点でのリクエスト数を集計する
		for (ConcurrentRequest req : this.concurrentRequestList) {

			if (req.getType() == EventType.ACCEPT_TIME) {
				count++;
			} else if (req.getType() == EventType.RESPONSE_TIME) {
				count--;
			}

			req.setCount(count);
		}

		this.sortedConcurrentRequestList = this.concurrentRequestList;

		return this.sortedConcurrentRequestList;
	}

	public static class RequestEntry {
		public String url  = null;
		public long time = 0L;
		public Date date = null;
		public String sessionId = null;
	}

	private void addRequestInfo(Log log, String url, long responseTime) {

		this.requestCount++;
		this.totalPageTime += responseTime;

		if (StringUtils.isNotEmpty(url)) {
			this.urlPageTimesMap.get(url).add(responseTime);
		}

		this.sessionMap.addPageTime(log.clientSessionId, responseTime);

		registerSessionAccess(log);


		if (this.requestPageTimeRank != null) {

			// 処理時間ランクを設定
			int size = this.requestPageTimeRank.length;

			if (this.requestPageTimeRank[size - 1] == null ||
					responseTime > this.requestPageTimeRank[size - 1].time) {

				// ランクの入れ替え
				for (int i = 0; i < size; i++) {

					if (this.requestPageTimeRank[i] != null &&
							responseTime <= this.requestPageTimeRank[i].time) {
						continue;
					}

					// シフト
					for (int j = size - 1; j >= i + 1; j--) {
						this.requestPageTimeRank[j] = this.requestPageTimeRank[j - 1];
					}

					RequestEntry requestEntry = new RequestEntry();
					this.requestPageTimeRank[i] = requestEntry;

					requestEntry.url = url;
					requestEntry.time = responseTime;
					requestEntry.date = log.date;
					requestEntry.sessionId = log.clientSessionId;

					break;
				}
			}

		} else {
			if (responseTime >= this.requestPageTimeRankThresholdMillis) {

				RequestEntry requestEntry = new RequestEntry();

				requestEntry.url = url;
				requestEntry.time = responseTime;
				requestEntry.date = log.date;
				requestEntry.sessionId = log.clientSessionId;

				this.requestPageTimeRankList.add(requestEntry);
			}
		}
	}

	/**
	 * セッションアクセス履歴を登録する。
	 * @param log
	 */
	private void registerSessionAccess(Log log) {

		this.sessionMap.putLastAccessDate(log.clientSessionId, log.date);
		this.sessionMap.putFirstAccessDate(log.clientSessionId, log.date);

		String url = log.getUrl();

		if (url != null &&
				(url.endsWith("/user.logout") || url.endsWith(".portal"))) {

			this.sessionMap.putLogoutDate(log.clientSessionId, log.date);
		}
	}
}
