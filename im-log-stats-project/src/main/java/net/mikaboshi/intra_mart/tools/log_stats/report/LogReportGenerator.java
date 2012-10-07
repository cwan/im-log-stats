package net.mikaboshi.intra_mart.tools.log_stats.report;

import java.io.File;
import java.io.IOException;
import java.util.Collection;


import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.exception.FatalRuntimeException;
import net.mikaboshi.intra_mart.tools.log_stats.formatter.ReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ExceptionLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.LogFileReader;
import net.mikaboshi.intra_mart.tools.log_stats.parser.LogLayoutDefinitions;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.RequestLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.TransitionLogParser;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ログファイルを解析し、統計レポートを生成する。
 */
public class LogReportGenerator {

	private static final Log logger = LogFactory.getLog(LogReportGenerator.class);

	/** ログレポート */
	private Report report = new Report();

	/** ログファイルパーサパラメータ */
	private ParserParameter parserParameter = new ParserParameter();

	/** レポートパラメータ */
	private ReportParameter reportParameter = new ReportParameter();

	/**	リクエストログファイル */
	private Collection<File> requestLogFiles = null;

	/** 画面遷移ログファイル */
	private Collection<File> transitionLogFiles = null;

	/** 例外ログファイル  */
	private Collection<File> exceptionLogFiles = null;

	/** 遅延時間（ミリ秒） */
	private long delay = 0L;

	private int delayCounter = 0;

	/**
	 * レポートパラメータを取得する。
	 * @return
	 */
	public ReportParameter getReportParameter() {
		return reportParameter;
	}

	/**
	 * レポートパラメータを設定する。
	 * @param reportParameter
	 */
	public void setReportParameter(ReportParameter reportParameter) {
		this.reportParameter = reportParameter;
	}

	/**
	 * ログファイルパーサパラメータ を取得する。
	 * @return
	 */
	public ParserParameter getParserParameter() {
		return parserParameter;
	}

	/**
	 * ログファイルパーサパラメータ を設定する。
	 * @param parserParameter
	 */
	public void setParserParameter(ParserParameter parserParameter) {
		this.parserParameter = parserParameter;
	}

	/**
	 * @return requestLogFiles
	 */
	public Collection<File> getRequestLogFiles() {
		return requestLogFiles;
	}

	/**
	 * @param requestLogFiles セットする requestLogFiles
	 */
	public void setRequestLogFiles(Collection<File> requestLogFiles) {
		this.requestLogFiles = requestLogFiles;
	}

	/**
	 * @return transitionLogFiles
	 */
	public Collection<File> getTransitionLogFiles() {
		return transitionLogFiles;
	}

	/**
	 * @param transitionLogFiles セットする transitionLogFiles
	 */
	public void setTransitionLogFiles(Collection<File> transitionLogFiles) {
		this.transitionLogFiles = transitionLogFiles;
	}

	/**
	 * @return exceptionLogFiles
	 */
	public Collection<File> getExceptionLogFiles() {
		return exceptionLogFiles;
	}

	/**
	 * @param exceptionLogFiles セットする exceptionLogFiles
	 */
	public void setExceptionLogFiles(Collection<File> exceptionLogFiles) {
		this.exceptionLogFiles = exceptionLogFiles;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * ログファイルを解析し、統計レポートを生成する。
	 * @param reportFormatter
	 * @throws IOException
	 */
	public void execute(ReportFormatter reportFormatter) throws IOException {

		this.report.setParameter(getReportParameter());

		parseRequestLogs();
		parseTransitionLogs();
		parseExceptionLogs();

		reportFormatter.format(this.report);
	}

	protected void parseRequestLogs() throws IOException {

		if (CollectionUtils.isEmpty(this.requestLogFiles)) {
			return;
		}

		this.report.setRequestLogFiles(this.requestLogFiles);

		String layout = this.parserParameter.getRequestLogLayout();

		if (this.parserParameter.getVersion().isVersion6()) {
			layout = LogLayoutDefinitions.convertV6toV7(layout);
		}

		RequestLogParser parser = new RequestLogParser(this.parserParameter, layout);


		LogFileReader logFileReader = null;

		try {
			logFileReader = new LogFileReader(
									this.requestLogFiles,
									parser,
									this.parserParameter.getCharset());

			while (logFileReader.hasNext()) {

				RequestLog log = parser.parse(logFileReader.nextLine());

				delay(1);

				if (log != null) {
					this.report.add(log);
				}
			}

		} finally {
			if (logFileReader != null) {
				logFileReader.close();
			}
		}
	}

	protected void parseTransitionLogs() throws IOException {

		if (CollectionUtils.isEmpty(this.transitionLogFiles)) {
			return;
		}

		this.report.setTransitionLogFiles(this.transitionLogFiles);

		String layout = this.parserParameter.getTransitionLogLayout();

		if (this.parserParameter.getVersion().isVersion6()) {
			layout = LogLayoutDefinitions.convertV6toV7(layout);
		}

		TransitionLogParser parser = new TransitionLogParser(this.parserParameter, layout);


		LogFileReader logFileReader = null;

		try {
			logFileReader = new LogFileReader(
									this.transitionLogFiles,
									parser,
									this.parserParameter.getCharset());

			while (logFileReader.hasNext()) {

				TransitionLog log = parser.parse(logFileReader.nextLine());

				delay(1);

				if (log != null) {
					this.report.add(log);
				}
			}

		} finally {
			logFileReader.close();
		}
	}

	protected void parseExceptionLogs() throws IOException {

		if (CollectionUtils.isEmpty(this.exceptionLogFiles)) {
			return;
		}

		this.report.setExceptionLogFiles(this.exceptionLogFiles);


		ExceptionLogParser parser = ExceptionLogParser.create(this.parserParameter);

		int i = 0;

		for (File logFile : this.exceptionLogFiles) {

			String logText = FileUtils.readFileToString(logFile, this.parserParameter.getCharset());
			ExceptionLog log = parser.parse(logText);

			delay(10);

			if (log != null) {
				this.report.add(log);
			}

			if (++i % 1000 == 0) {
				logger.info(i + " exception log files had been read ...");
			}
		}
	}

	/**
	 * スリープを入れる
	 */
	private void delay(final int count) {

		if (this.delay <= 0L) {
			return;
		}

		for (int i = 0; i < count; i++) {

			if (++this.delayCounter % 100 != 0) {
				continue;
			}

			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e) {
				throw new FatalRuntimeException(e);
			}
		}


	}

}
