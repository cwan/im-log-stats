package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 汎用ログファイルパーサ
 */
public class GenericLogParser {

	private Log logger = null;

	protected File logFile = null;

	protected final ParserParameter parameter;

	public GenericLogParser(ParserParameter parameter) {

		if (parameter == null) {
			throw new NullPointerException();
		}

		this.parameter = parameter;
	}

	/**
	 * 現在解析中のログファイルを設定する。
	 * @param logFile
	 */
	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	protected String getLogFilePath() {

		if (this.logFile == null) {
			return null;
		}

		return this.logFile.getAbsolutePath();
	}

	/**
	 * ロガーを取得する
	 * @return
	 */
	protected Log getLogger() {
		if (this.logger == null) {
			this.logger = LogFactory.getLog(getClass());
		}
		return this.logger;
	}

	/**
	 * INFOレベルのログを出力する
	 * @param format
	 * @param args
	 */
	protected void info(String format, Object ... args) {

		Log logger = getLogger();

		if (logger.isInfoEnabled()) {
			logger.info(String.format(
					"[" + getLogFilePath() + "] " +  format, args));
		}
	}

	/**
	 * WARNレベルのログを出力する
	 * @param format
	 * @param args
	 */
	protected void warn(String format, Object ... args) {

		Log logger = getLogger();

		if (logger.isWarnEnabled()) {
			logger.warn(String.format(
					"[" + getLogFilePath() + "] " +  format, args));
		}
	}

	/**
	 * ログの日付が範囲内にあるかチェックする。
	 * @param log
	 * @return
	 */
	protected boolean isInRange(net.mikaboshi.intra_mart.tools.log_stats.entity.Log log) {

		if (log == null || log.date == null) {
			return false;
		}

		Date begin = this.parameter.getBegin();

		if (begin != null && begin.getTime() > log.date.getTime()) {
			return false;
		}

		Date end = this.parameter.getEnd();

		if (end != null && end.getTime() < log.date.getTime()) {
			return false;
		}

		return true;
	}

}
