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

package net.mikaboshi.intra_mart.tools.log_stats.formatter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.report.PageTimeStat;
import net.mikaboshi.intra_mart.tools.log_stats.report.Report;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;
import net.mikaboshi.intra_mart.tools.log_stats.report.TimeSpanStatistics;
import net.mikaboshi.intra_mart.tools.log_stats.report.GrossStatistics.RequestEntry;
import net.mikaboshi.intra_mart.tools.log_stats.report.Report.ExceptionReportEntry;
import net.mikaboshi.intra_mart.tools.log_stats.report.Report.RequestUrlReportEntry;
import net.mikaboshi.intra_mart.tools.log_stats.report.Report.SessionReportEntry;

import org.apache.commons.collections.CollectionUtils;


/**
 * テキスト形式のレポートを生成する。
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class SimpleTextFileReportFormatter extends AbstractFileReportFormatter {

	/** 列の区切り文字 */
	private final char delimiter;

	/** 項目のエスケープが必要なパターン */
	private final Pattern escapeCheckPattern;

	/** 日時フォーマッタ */
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/** 期間別統計の開始時刻フォーマッタ */
	private final DateFormat spanStartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/** 期間別統計の終了時刻フォーマッタ */
	private final DateFormat spanEndDateFormat = new SimpleDateFormat("HH:mm");

	/** 開始・終了時刻パラメータのフォーマッタ */
	private final DateFormat beginEndDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	/** リクエストURLの表記 */
	private String requestUrlLabel;


	public SimpleTextFileReportFormatter(
				File output,
				String charset,
				char delimiter,
				ParserParameter parserParameter,
				ReportParameter reportParameter) {

		super(output, charset, parserParameter, reportParameter);
		this.delimiter = delimiter;
		this.escapeCheckPattern = Pattern.compile(".*[" + this.delimiter + "\\r\\n\"].*");
	}

	public SimpleTextFileReportFormatter(
				File output,
				char delimiter,
				ParserParameter parserParameter,
				ReportParameter reportParameter) {

		this(output, null, delimiter, parserParameter, reportParameter);
	}

	@Override
	public void doFormat(Report report) throws IOException {

			this.requestUrlLabel =
					CollectionUtils.isEmpty(report.getRequestLogFiles()) ? "遷移先画面パス" : "リクエストURL";

		addLine(
			report.getParameter().getName(),
			new Date(),
			report.getParameter().getSignature());

		newLine();

		formatTimeSpanStatistics(report);

		newLine();

		formatRequestPageTimeRank(report);

		newLine();

		formatRequestUrlRank(report);

		newLine();

		formatSessionRank(report);

		newLine();

		formatExceptionRank(report);

		newLine();

		formatLogFiles(report);

		newLine();

		formatParameters();
	}

	/**
	 * 期間別統計を出力する。
	 * @param report
	 */
	protected void formatTimeSpanStatistics(Report report) {

		report.countActiveSessions();

		// 見出し
		addLine("期間別統計");

		// ヘッダ
		addLine(
			"#",
			"集計期間▲",
			"リクエスト数合計",
			"リクエスト処理時間の合計(ms)",
			"リクエスト処理時間の平均値(ms)",
			"リクエスト処理時間の最小値(ms)",
			"リクエスト処理時間の中央値(ms)",
			"リクエスト処理時間の90%Line(ms)",
			"リクエスト処理時間の最大値(ms)",
			"リクエスト処理時間の標準偏差(ms)",
			"ユニークユーザ数",
			"ユニークセッション数",
			"有効セッション数",
			"例外数合計",
			"画面遷移数合計",
			"画面遷移例外数合計");

		int i = 0;

		for (TimeSpanStatistics stat : report.getTimeSpanStatisticsList()) {

			List<Long> pageTimes = stat.getRequestPageTimes();

			PageTimeStat pageTimeStat = new PageTimeStat();
			report.setPageTimeStat(pageTimeStat, pageTimes);

			addLine(
				++i,
				getSpanDate(stat.getStartDate(), report.getParameter().getSpan()),
				stat.getRequestCount(),
				pageTimeStat.pageTimeSum,
				pageTimeStat.pageTimeAverage,
				pageTimeStat.pageTimeMin,
				pageTimeStat.pageTimeMedian,
				pageTimeStat.pageTime90Percent,
				pageTimeStat.pageTimeMax,
				pageTimeStat.pageTimeStandardDeviation,
				stat.getUniqueUserCount(),
				stat.getUniqueSessionCount(),
				stat.getActiveSessionCount(),
				stat.getExceptionCount(),
				stat.getTransitionCount(),
				stat.getTransitionExceptionCount());
		}
	}

	/**
	 * リクエスト処理時間のランキング（総合トップN）を出力する
	 * @param report
	 */
	protected void formatRequestPageTimeRank(Report report) {

		// 見出し
		addLine(
			"リクエスト処理時間総合ランク",
			report.getParameter().getRequestPageTimeRankSize() + "/" +
			report.getRequestCount());

		// ヘッダ
		addLine("#", this.requestUrlLabel, "処理時間(ms)▼", "ログ出力日時", "セッションID", "ユーザID");

		int i = 0;

		for (RequestEntry req : report.getRequestPageTimeRank()) {

			if (req == null) {
				continue;
			}

			addLine(
				++i,
				req.url,
				req.time,
				req.date,
				req.sessionId,
				report.getUserIdFromSessionId(req.sessionId, ""));
		}
	}

	/**
	 * リクエスト処理時間のランキング（URL別・処理時間合計トップN）を出力する
	 * @param report
	 */
	protected void formatRequestUrlRank(Report report) {

		List<RequestUrlReportEntry> list = report.getRequestUrlReportList();

		// 見出し
		addLine(
			this.requestUrlLabel + "別・処理時間合計ランク",
			report.getParameter().getRequestUrlRankSize() + "/" + list.size());

		// ヘッダ
		addLine(
				"#",
				this.requestUrlLabel,
				"リクエスト回数",
				"処理時間合計(ms)▼",
				"処理時間平均(ms)",
				"処理時間最小値(ms)",
				"処理時間中央値(ms)",
				"処理時間90%Line(ms)",
				"処理時間最大値(ms)",
				"処理時間標準偏差(ms)",
				"リクエスト回数%",
				"処理時間%");

		MathContext percentMathContext = new MathContext(2, RoundingMode.HALF_UP);

		int i = 0;

		for (RequestUrlReportEntry entry : list) {

			if (++i > report.getParameter().getRequestUrlRankSize()) {
				break;
			}

			addLine(
				i,
				entry.url,
				entry.count,
				entry.pageTimeSum,
				entry.pageTimeAverage,
				entry.pageTimeMin,
				entry.pageTimeMedian,
				entry.pageTime90Percent,
				entry.pageTimeMax,
				entry.pageTimeStandardDeviation,
				new BigDecimal(entry.countRate * 100, percentMathContext).doubleValue(),
				new BigDecimal(entry.pageTimeRate * 100, percentMathContext).doubleValue());
		}
	}

	/**
	 * セッション毎の統計情報を出力する。
	 * @param report
	 */
	protected void formatSessionRank(Report report) {

		List<SessionReportEntry> list = report.getSessionReportList();

		// 見出し
		addLine(
			"セッション別・処理時間合計ランク",
			report.getParameter().getSessionRankSize() + "/" + list.size());

		// ヘッダ
		addLine(
				"#",
				"セッションID",
				"ユーザID",
				"リクエスト回数",
				"処理時間合計(ms)▼",
				"処理時間平均(ms)",
				"処理時間最小値(ms)",
				"処理時間中央値(ms)",
				"処理時間90%Line(ms)",
				"処理時間最大値(ms)",
				"処理時間標準偏差(ms)",
				"リクエスト回数%",
				"処理時間%",
				"初回アクセス日時",
				"最終アクセス日時");

		MathContext percentMathContext = new MathContext(2, RoundingMode.HALF_UP);

		int i = 0;

		for (SessionReportEntry entry : list) {

			if (++i > report.getParameter().getSessionRankSize()) {
				break;
			}

			addLine(
				i,
				entry.sessionId,
				report.getUserIdFromSessionId(entry.sessionId, ""),
				entry.count,
				entry.pageTimeSum,
				entry.pageTimeAverage,
				entry.pageTimeMin,
				entry.pageTimeMedian,
				entry.pageTime90Percent,
				entry.pageTimeMax,
				entry.pageTimeStandardDeviation,
				new BigDecimal(entry.countRate * 100, percentMathContext).doubleValue(),
				new BigDecimal(entry.pageTimeRate * 100, percentMathContext).doubleValue(),
				entry.firstAccessTime,
				entry.lastAccessTime);
		}
	}


	/**
	 * 例外回数ランクを出力する
	 * @param report
	 */
	protected void formatExceptionRank(Report report) {

		// 見出し
		addLine("例外");

		// ヘッダ
		addLine("#", "メッセージ", "スタックトレース1行目", "回数▼");

		int i = 0;

		for (ExceptionReportEntry e : report.getExceptionReport()) {
			addLine(++i, e.message, e.firstLineOfStackTrace, e.count);
		}
	}

	/**
	 * 解析対象ログファイルを出力する
	 * @param report
	 */
	protected void formatLogFiles(Report report) {

		// 見出し
		addLine("解析対象ログファイル");

		// ヘッダ
		addLine("種類", "ファイルパス/個数");

		if (report.getRequestLogFiles() != null) {
			for (File file : report.getRequestLogFiles()) {
				addLine("リクエストログ", file.getAbsolutePath());
			}
		}

		if (report.getTransitionLogFiles() != null) {
			for (File file : report.getTransitionLogFiles()) {
				addLine("画面遷移ログ", file.getAbsolutePath());
			}
		}

		addLine("エラーログ",
				(report.getExceptionLogFiles() != null ?
				report.getExceptionLogFiles().size() : 0) + "ファイル");
	}

	/**
	 * パラメータ情報を出力する
	 */
	protected void formatParameters() {

		// 見出し
		addLine("レポートパラメータ");

		// ヘッダ
		addLine("項目名", "値");

		addLine("バージョン", this.parserParameter.getVersion().getName());

		addLine("開始時刻",
				this.parserParameter.getBegin() != null ?
				this.beginEndDateFormat.format(this.parserParameter.getBegin()) :
				"-");

		addLine("終了時刻",
				this.parserParameter.getEnd() != null ?
				this.beginEndDateFormat.format(this.parserParameter.getEnd()) :
				"-");

		addLine("ログファイルの文字コード", this.parserParameter.getCharset());
		addLine("リクエストログレイアウト", this.parserParameter.getRequestLogLayout());
		addLine("画面遷移ログレイアウト", this.parserParameter.getTransitionLogLayout());
		addLine("期間別統計の間隔", this.reportParameter.getSpan() + "分");
		addLine("セッションタイムアウト時間", this.reportParameter.getSessionTimeout() + "分");
		addLine("リクエスト処理時間ランクの出力件数", this.reportParameter.getRequestPageTimeRankSize());
		addLine("リクエストURLランクの出力件数", this.reportParameter.getRequestPageTimeRankSize());
		addLine("セッションランクの出力件数", this.reportParameter.getSessionRankSize());
	}

	protected void addDelimiter() {
		this.writer.write(this.delimiter);
	}

	protected void add(Object ... args) {

		int i = 0;

		for (Object s : args) {
			if (i++ != 0) {
				addDelimiter();
			}

			this.writer.print(escape(s));
		}
	}

	protected void addLine(Object ... args) {

		add(args);
		newLine();
	}

	protected void newLine() {
		this.writer.println();
	}

	protected String escape(Object o) {

		if (o == null) {
			return "";
		}

		if (o instanceof Number) {
			return String.valueOf(o);
		}

		if (o instanceof Date) {
			return this.dateFormat.format((Date) o);
		}

		String s = o.toString();

		if (this.escapeCheckPattern.matcher(s).find()) {
			return "\"" + s.replaceAll("\"", "\"\"") + "\"";
		} else {
			return s;
		}
	}

	private String getSpanDate(Date startDate, long span) {

		if (startDate == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		sb.append(this.spanStartDateFormat.format(startDate));
		sb.append(" - ");

		Date endDate = new Date(startDate.getTime() + span * 60 * 1000);
		sb.append(this.spanEndDateFormat.format(endDate));

		return sb.toString();
	}

}
