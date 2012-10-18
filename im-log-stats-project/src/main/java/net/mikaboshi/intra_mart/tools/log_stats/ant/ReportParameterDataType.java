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

package net.mikaboshi.intra_mart.tools.log_stats.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ReportType;
import net.mikaboshi.intra_mart.tools.log_stats.formatter.ReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.formatter.TemplateFileReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

/**
 * ログ統計レポート設定のネスト要素
 *
 * @version 1.0.10
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ReportParameterDataType extends DataType {

	/** レポートタイプ */
	private ReportType type = ReportType.HTML;

	/** 期間別統計の単位（分）  */
	private long span = 0L;

	/** セッションタイムアウト時間（分） */
	private int sessionTimeout = 0;

	/** リクエスト処理時間ランクの出力件数  */
	private int requestPageTimeRankSize = 0;

	/** リクエストURLランクの出力件数  */
	private int requestUrlRankSize = 0;

	/** セッションランクの出力件数  */
	private int sessionRankSize = 0;

	/** レポート名 */
	private String name = null;

	/** 署名 */
	private String signature = null;

	/**
	 * カスタムテンプレートファイルパス
	 */
	private String templateFile = null;

	/**
	 * カスタムテンプレートファイルの文字コード
	 */
	private String templateCharset = null;

	/** レポート出力先パス */
	private String output = "report_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

	/** レポートの文字コード */
	private String charset = Charset.defaultCharset().toString();

	public void setType(String s) {
		this.type = ReportType.toEnum(s);

		if (this.type == null) {
			throw new BuildException("Unsupported report type : " + s);
		}
	}

	/**
	 * @param span セットする span
	 */
	public void setSpan(long span) {
		this.span = span;
	}

	/**
	 * @param sessionTimeout セットする sessionTimeout
	 */
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * @param requestPageTimeRankSize セットする requestPageTimeRankSize
	 */
	public void setRequestPageTimeRankSize(int requestPageTimeRankSize) {
		this.requestPageTimeRankSize = requestPageTimeRankSize;
	}

	/**
	 * @param requestUrlRankSize セットする requestUrlRankSize
	 */
	public void setRequestUrlRankSize(int requestUrlRankSize) {
		this.requestUrlRankSize = requestUrlRankSize;
	}

	/**
	 * @param sessionRankSize セットする sessionRankSize
	 */
	public void setSessionRankSize(int sessionRankSize) {
		this.sessionRankSize = sessionRankSize;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param signature セットする signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public void setTemplateCharset(String templateCharset) {
		this.templateCharset = templateCharset;
	}

	public void setOutput(String output) {

		try {
			this.output = AntParameterUtil.getReportOutput(output);
		} catch (IllegalArgumentException e) {
			throw new BuildException("Parameter 'output' is invalid : " + output);
		}
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 設定内容を元に、ReportFormatterのインスタンスを取得する。
	 * @return
	 * @throws BuildException
	 */
	public ReportFormatter getReportFormatter(
				ParserParameter parserParameter,
				ReportParameter reportParameter) throws BuildException {

		File outputFile = new File(this.output);

		TemplateFileReportFormatter reportFormatter =
				new TemplateFileReportFormatter(
								outputFile,
								this.charset,
								parserParameter,
								reportParameter);

		if (this.type == ReportType.HTML || this.type == ReportType.VISUALIZE || this.type == ReportType.CSV || this.type == ReportType.TSV) {

			String templateFileRecourcePath = "/net/mikaboshi/intra_mart/tools/log_stats/formatter/";

			if (this.type == ReportType.HTML) {
				templateFileRecourcePath += "html_file_report_template.html";
			} else if (this.type == ReportType.VISUALIZE) {
				templateFileRecourcePath += "visualize_file_report_template.html";
			} else {
				templateFileRecourcePath += "xsv_file_report_template.txt";

				if (this.type == ReportType.CSV) {
					reportFormatter.setSeparator(",");
				} else {
					reportFormatter.setSeparator("\t");
				}
			}

			reportFormatter.setTempleteResourceFilePath(templateFileRecourcePath);
			reportFormatter.setTemplateFileCharset("Windows-31J");

		} else if (this.type == ReportType.TEMPLATE) {

			if (this.templateFile == null) {
				throw new BuildException("Paranmeter 'templeteFile' is required at 'report' tag.");
			}

			// カスタムテンプレートの設定
			try {
				reportFormatter.setTemplateFile(new File(this.templateFile));
				reportFormatter.setTemplateFileCharset(this.templateCharset);

			} catch (FileNotFoundException e) {
				throw new BuildException(e);
			}

		} else {
			throw new AssertionError();
		}

		return reportFormatter;
	}

	/**
	 * 自身の内容をReportFormatterインスタンスに変換する。
	 * @param version
	 * @return
	 */
	public ReportParameter toReportParameter(Version version) {

		ReportParameter parameter = new ReportParameter();

		if (this.span > 0L) {
			parameter.setSpan(this.span);
		}

		if (this.sessionTimeout > 0) {
			parameter.setSessionTimeout(this.sessionTimeout);
		}

		if (this.requestPageTimeRankSize > 0) {
			parameter.setRequestPageTimeRankSize(this.requestPageTimeRankSize);
		}

		if (this.requestUrlRankSize > 0) {
			parameter.setRequestUrlRankSize(this.requestUrlRankSize);
		}

		if (this.sessionRankSize > 0) {
			parameter.setSessionRankSize(this.sessionRankSize);
		}

		if (this.name != null) {
			parameter.setName(this.name);
		}

		if (this.signature != null) {
			parameter.setSignature(this.signature);
		}

		if (version != null) {
			parameter.setVersion(version);
		}

		return parameter;
	}
}
