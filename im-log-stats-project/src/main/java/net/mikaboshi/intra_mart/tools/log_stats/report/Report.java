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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest;
import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.Level;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.report.GrossStatistics.RequestEntry;
import net.mikaboshi.intra_mart.tools.log_stats.util.MathUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.map.LazyMap;

/**
 * ログ解析レポート
 *
 * @version 1.0.18
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class Report {

	/**
	 * レポートパラメータ
	 */
	private ReportParameter parameter = new ReportParameter();

	/**
	 * 分割開始時刻 => 時間分割統計
	 */
	@SuppressWarnings("unchecked")
	private Map<Date, TimeSpanStatistics> timeSpanStatMap = LazyMap.decorate(
			new HashMap<Date, TimeSpanStatistics>(),
			new Factory() {
				public Object create() {
					return new TimeSpanStatistics(CollectionUtils.isEmpty(getRequestLogFiles()));
				}
			});

	/**
	 * テナントID => テナント別統計
	 * @since 1.0.16
	 */
	@SuppressWarnings("unchecked")
	private Map<String, TenantStatistics> tenantStatMap = LazyMap.decorate(
			new HashMap<String, TenantStatistics>(),
			new Factory() {
				public Object create() {
					return new TenantStatistics(CollectionUtils.isEmpty(getRequestLogFiles()));
				}
			});

	/**
	 * 全体統計
	 */
	private GrossStatistics grossStatistics;

	/**
	 * リクエストログファイルのコレクション
	 */
	private Collection<File> requestLogFiles = null;

	/**
	 * 画面遷移ログファイルのコレクション
	 */
	private Collection<File> transitionLogFiles = null;

	/**
	 * 例外ログファイルのコレクション
	 */
	private Collection<File> exceptionLogFiles = null;

	/**
	 * 処理時間統計情報を設定する。
	 */
	public static void setPageTimeStat(PageTimeStat stat, List<Long> pageTimes) {

		if (CollectionUtils.isEmpty(pageTimes)) {
			return;
		}

		Collections.sort(pageTimes);

		stat.count = pageTimes.size();
		stat.pageTimeMin = pageTimes.get(0);
		stat.pageTimeMedian = MathUtil.getMedian(pageTimes, 0L);
		stat.pageTimeP90 = MathUtil.getPercentile(pageTimes, 90, 0L);
		stat.pageTimeMax = pageTimes.get(stat.count - 1);
		stat.pageTimeSum = MathUtil.getSum(pageTimes);
		stat.pageTimeAverage = MathUtil.getAverage(pageTimes, stat.pageTimeSum, 0L);
		stat.pageTimeStandardDeviation = MathUtil.getStandardDeviation(pageTimes, stat.pageTimeAverage, 0L);
	}

	/**
	 * レポートパラメータを設定する。
	 * @param parameter
	 */
	public void setParameter(ReportParameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * レポートパラメータを取得する。
	 * @return
	 */
	public ReportParameter getParameter() {
		return parameter;
	}

	/**
	 * リクエストログを追加する。
	 * @param log
	 */
	public void add(RequestLog log) {

		if (log == null || log.date == null || log.isIN() || this.parameter.getSpan() <= 0) {
			return;
		}

		TimeSpanStatistics stat = this.timeSpanStatMap.get(floorDate(log.date));
		stat.add(log);

		if (log.tenantId != null) {
			TenantStatistics tenantStat = this.tenantStatMap.get(log.tenantId);
			tenantStat.add(log);
		}

		getGrossStatistics().add(log);
	}

	/**
	 * 画面遷移ログを追加する。
	 * @param log
	 */
	public void add(TransitionLog log) {

		if (log == null || log.date == null || this.parameter.getSpan() <= 0) {
			return;
		}

		TimeSpanStatistics stat = this.timeSpanStatMap.get(floorDate(log.date));
		stat.add(log);

		if (log.tenantId != null) {
			TenantStatistics tenantStat = this.tenantStatMap.get(log.tenantId);
			tenantStat.add(log);
		}

		getGrossStatistics().add(log);
	}

	/**
	 * 例外ログを追加する。
	 * @param log
	 */
	public void add(ExceptionLog log) {

		if (log == null || log.date == null || this.parameter.getSpan() <= 0) {
			return;
		}

		TimeSpanStatistics stat = this.timeSpanStatMap.get(floorDate(log.date));
		stat.add(log);

		// ※標準フォーマットでは例外ログにテナントIDは含まれない
		if (log.tenantId != null) {
			TenantStatistics tenantStat = this.tenantStatMap.get(log.tenantId);
			tenantStat.add(log);
		}

		getGrossStatistics().add(log);
	}

	/**
	 * 時間分割統計毎に、有効なセッション数を集計する。
	 */
	public void countActiveSessions() {

		if (this.parameter.getSpan() <= 0) {
			return;
		}

		long sessionTimeoutMillis = this.parameter.getSessionTimeout() * 60 * 1000;

		SessionMap sessionMap = getGrossStatistics().getSessionMap();

		for (Map.Entry<Date, TimeSpanStatistics> statEntry : this.timeSpanStatMap.entrySet()) {

			// 統計終了時点で有効なセッションを数える
			long startTime = statEntry.getKey().getTime();
			long endTime = startTime + (this.parameter.getSpan() * 60 * 1000);

			int activeSessions = 0;

			for (Map.Entry<String, Date> sessionEntry : sessionMap.getLastAccessSessionMap().entrySet()) {

				String sessionId = sessionEntry.getKey();

				if (sessionMap.isAvailabilityCheckSessionId(sessionId)) {
					continue;
				}

				Date date = sessionMap.getFirstAccessDate(sessionId);

				if (date == null) {
					continue;
				}

				long firstAccessTime = date.getTime();

				if (firstAccessTime > endTime) {
					// これより後の期間の生成されるセッション
					continue;
				}

				long lastAccessTime = sessionEntry.getValue().getTime();

				if (sessionTimeoutMillis > 0L && endTime - lastAccessTime > sessionTimeoutMillis) {
					// セッションがタイムアウトしている
					continue;
				}

				Date logoutDate = sessionMap.getLogoutDate(sessionId) ;

				if (logoutDate != null && logoutDate.getTime() < endTime) {
					// ログアウトしている
					continue;
				}

				activeSessions++;
			}

			statEntry.getValue().setActiveSessionCount(activeSessions);
		}
	}

	/**
	 * 時間分割統計のリスト（ソート済み）を取得する。
	 * @return
	 */
	public List<TimeSpanStatistics> getTimeSpanStatisticsList() {

		List<TimeSpanStatistics> list = new ArrayList<TimeSpanStatistics>();

		if (this.parameter.getSpan() <= 0) {
			return list;
		}

		long spanMills = this.parameter.getSpan() * 60L * 1000L;

		// ログ出力日時の昇順にならびかえ
		List<Date> startDates = new ArrayList<Date>(this.timeSpanStatMap.keySet());
		Collections.sort(startDates);

		// 最大同時リクエスト数
		List<ConcurrentRequest> concurrentRequestList = getGrossStatistics().getConcurrentRequestList();

		for (Date startDate : startDates) {
			TimeSpanStatistics stat = this.timeSpanStatMap.get(startDate);
			stat.setStartDate(startDate);
			stat.setEndDate(new Date(stat.getStartDate().getTime() + spanMills));

			if (this.parameter.isMaxConcurrentRequest()) {
				stat.countMaxConcurrentRequest(concurrentRequestList);
			}

			list.add(stat);
		}

		return list;
	}

	/**
	 * テナント別統計のリスト（ソート済み）を取得する。
	 * @since 1.0.16
	 * @return
	 */
	public List<TenantStatistics> getTenantStatisticsList() {

		List<TenantStatistics> list = new ArrayList<TenantStatistics>();

		List<String> tenantIds = new ArrayList<String>(this.tenantStatMap.keySet());
		Collections.sort(tenantIds);

		// 最大同時リクエスト数
		List<ConcurrentRequest> concurrentRequestList = getGrossStatistics().getConcurrentRequestList();

		for (String tenantId : tenantIds) {
			TenantStatistics stat = this.tenantStatMap.get(tenantId);
			stat.setTenantId(tenantId);

			if (this.parameter.isMaxConcurrentRequest()) {
				stat.countMaxConcurrentRequest(concurrentRequestList);
			}

			list.add(stat);
		}

		return list;
	}

	/**
	 * 例外統計のリスト（件数の多い順にソート済み）を取得する。
	 * 件数は、メッセージ&スタックトレース1行目ごとに集計している。
	 * @return
	 */
	public List<ExceptionReportEntry> getExceptionReport() {

		List<ExceptionReportEntry> list = new ArrayList<ExceptionReportEntry>();

		for (Map.Entry<ExceptionLog, int[]> entry : getGrossStatistics().getExceptionCountMap().entrySet()) {

			ExceptionLog log = entry.getKey();

			ExceptionReportEntry e = new ExceptionReportEntry();

			e.level = log.level;
			e.message = log.message;
			e.groupingLineOfStackTrace = log.getGroupingLineOfStackTrace();
			e.count = entry.getValue()[0];

			list.add(e);
		}

		Collections.sort(list, new Comparator<ExceptionReportEntry>() {

			public int compare(ExceptionReportEntry o1, ExceptionReportEntry o2) {

				if (o1.count == o2.count) {
					return 0;
				}

				return o1.count > o2.count ? -1 : 1;
			}
		});

		return list;
	}

	/**
	 * リクエスト処理時間のランクを取得する。
	 * 同一のリクエストURLが複数含まれる場合がある。
	 * @return
	 */
	public RequestEntry[] getRequestPageTimeRank() {

		return getGrossStatistics().getRequestPageTimeRank();
	}

	/**
	 * セッションIDから、ユーザIDを取得する。
	 * @param sessionId セッションID
	 * @param nullValue 該当なしの場合の戻り値
	 * @return
	 */
	public String getUserIdFromSessionId(String sessionId, String nullValue) {

		String userId = getGrossStatistics().getSessionMap().getUserId(sessionId);
		return userId != null ? userId : nullValue;
	}

	/**
	 * リクエストURL毎のの統計情報のリストを取得する。
	 * リストは、処理時間合計の降順でソートされている。
	 * @return
	 */
	public List<RequestUrlReportEntry> getRequestUrlReportList() {

		List<RequestUrlReportEntry> list = new ArrayList<RequestUrlReportEntry>();

		for (Entry<String, List<Long>> req : getGrossStatistics().getUrlPageTimesMap().entrySet()) {

			List<Long> pageTimes = req.getValue();

			RequestUrlReportEntry entry = new RequestUrlReportEntry();
			list.add(entry);

			entry.url = req.getKey();
			setPageTimeStat(entry, pageTimes);
		}

		// 回数、処理時間比率の設定
		setPageTimeStatRate(list);

		// 処理時間合計の大きい順でソート
		Collections.sort(list, PageTimeSumDescComparator.INSTANCE);

		return list;
	}

	/**
	 * セッション毎のリクエスト統計情報のリストを取得する。
	 * リストは、処理時間合計の降順でソートされている。
	 * @return
	 */
	public List<SessionReportEntry> getSessionReportList() {

		List<SessionReportEntry> list = new ArrayList<SessionReportEntry>();

		SessionMap sessionMap = getGrossStatistics().getSessionMap();

		for (Entry<String, List<Long>> req : sessionMap.getSessionPageTimesMap().entrySet()) {

			List<Long> pageTimes = req.getValue();

			SessionReportEntry entry = new SessionReportEntry();
			list.add(entry);

			entry.sessionId = req.getKey();
			entry.firstAccessTime = sessionMap.getFirstAccessDate(entry.sessionId);
			entry.lastAccessTime = sessionMap.getLastAccessDate(entry.sessionId);

			setPageTimeStat(entry, pageTimes);
		}

		// 回数、処理時間比率の設定
		setPageTimeStatRate(list);

		// 処理時間合計の大きい順でソート
		Collections.sort(list, PageTimeSumDescComparator.INSTANCE);

		return list;
	}

	/**
	 * リクエスト数合計を取得する。
	 * @return
	 */
	public int getRequestCount() {
		return getGrossStatistics().getRequestCount();
	}

	/**
	 * 処理時間合計を取得する。
	 * @return
	 * @since 1.0.10
	 */
	public long getTotalPageTime() {
		return getGrossStatistics().getTotalPageTime();
	}

	/**
	 * 処理時間統計情報の比率情報を設定する。
	 */
	public void setPageTimeStatRate(List<? extends PageTimeStat> stats) {

		if (CollectionUtils.isEmpty(stats)) {
			return;
		}

		for (PageTimeStat stat : stats) {

			stat.countRate = MathUtil.getRate(stat.count, getRequestCount(), 0.0d);
			stat.pageTimeRate = MathUtil.getRate(stat.pageTimeSum, getTotalPageTime(), 0.0d);
		}
	}

	public Collection<File> getRequestLogFiles() {
		return requestLogFiles;
	}

	public void setRequestLogFiles(Collection<File> requestLogFiles) {
		this.requestLogFiles = requestLogFiles;
	}

	public Collection<File> getTransitionLogFiles() {
		return transitionLogFiles;
	}

	public void setTransitionLogFiles(Collection<File> transitionLogFiles) {
		this.transitionLogFiles = transitionLogFiles;
	}

	public Collection<File> getExceptionLogFiles() {
		return exceptionLogFiles;
	}

	public void setExceptionLogFiles(Collection<File> exceptionLogFiles) {
		this.exceptionLogFiles = exceptionLogFiles;
	}


	/**
	 * 例外レポート行
	 */
	public static class ExceptionReportEntry {

		public Level level = null;
		public String message = null;
		public String groupingLineOfStackTrace = null;
		public int count = 0;
	}

	/**
	 * リクエストURLレポート行
	 */
	public static class RequestUrlReportEntry extends PageTimeStat {

		public String url = null;
	}

	/**
	 * セッションレポート行
	 */
	public static class SessionReportEntry extends PageTimeStat {

		public String sessionId = null;
		public Date firstAccessTime = null;
		public Date lastAccessTime = null;
	}

	private GrossStatistics getGrossStatistics() {

		if (this.grossStatistics == null) {

			if (this.parameter.getRequestPageTimeRankThresholdMillis() < 0L) {

				this.grossStatistics = new GrossStatistics(
												this.parameter.getRequestPageTimeRankSize(),
												CollectionUtils.isEmpty(getRequestLogFiles()));
			} else {
				this.grossStatistics = new GrossStatistics(
												this.parameter.getRequestPageTimeRankThresholdMillis(),
												CollectionUtils.isEmpty(getRequestLogFiles()));
			}

			this.grossStatistics.setMaxConcurrentRequest(this.parameter.isMaxConcurrentRequest());
		}

		return grossStatistics;
	}

	private Date floorDate(Date date) {

		long time = date.getTime();
		return new Date(time - time % (this.parameter.getSpan() * 60 * 1000));
	}

	private static class PageTimeSumDescComparator implements Comparator<PageTimeStat>  {

		public static final PageTimeSumDescComparator INSTANCE = new PageTimeSumDescComparator();

		public int compare(PageTimeStat stat1, PageTimeStat stat2) {

			if (stat1.pageTimeSum == stat2.pageTimeSum) {
				return 0;
			}

			return stat1.pageTimeSum > stat2.pageTimeSum ? -1 : 1;
		}

	}

}
