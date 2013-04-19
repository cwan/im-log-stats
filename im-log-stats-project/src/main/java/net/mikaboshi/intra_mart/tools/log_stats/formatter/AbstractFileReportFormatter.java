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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.report.Report;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * レポートをファイルで生成する抽象クラス。
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public abstract class AbstractFileReportFormatter implements ReportFormatter {

	private static final Log logger = LogFactory.getLog(AbstractFileReportFormatter.class);

	/** レポートファイルの文字コード */
	protected String charset = Charset.defaultCharset().toString();

	/** 出力先レポートファイル */
	protected final File output;

	/** レポートファイルWriter */
	protected PrintWriter writer;

	/** パーサパラメータ */
	protected final ParserParameter parserParameter;

	/** レポートパラメータ */
	protected final ReportParameter reportParameter;

	public AbstractFileReportFormatter(
				File output,
				String charset,
				ParserParameter parserParameter,
				ReportParameter reportParameter) {

		if (output == null) {
			throw new NullPointerException();
		}

		this.output = output;

		if (charset != null) {
			this.charset = charset;
		}

		if (parserParameter == null) {
			throw new NullPointerException();
		}

		this.parserParameter = parserParameter;

		if (reportParameter == null) {
			throw new NullPointerException();
		}

		this.reportParameter = reportParameter;
	}

	public AbstractFileReportFormatter(
				File output,
				ParserParameter parserParameter,
				ReportParameter reportParameter) {
		this(output, null, parserParameter, reportParameter);
	}

	public void format(Report report) throws IOException {

		if (report == null) {
			throw new NullPointerException();
		}

		OutputStream out = null;

		try {
			out = new FileOutputStream(this.output);
			this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, this.charset)));

			doFormat(report);

			logger.info("Generate : " + this.output.getAbsolutePath());

		} finally {
			IOUtils.closeQuietly(this.writer);
			IOUtils.closeQuietly(out);
		}
	}

	public abstract void doFormat(Report report) throws IOException;

}
