package net.mikaboshi.intra_mart.tools.log_stats.formatter;

import java.io.IOException;

import net.mikaboshi.intra_mart.tools.log_stats.report.Report;


/**
 * ログレポートフォーマッタ
 */
public interface ReportFormatter {

	/**
	 * レポートを生成する。
	 * @param report
	 * @throws IOException
	 */
	public abstract void format(Report report) throws IOException;
}
