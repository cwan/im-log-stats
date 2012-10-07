package net.mikaboshi.intra_mart.tools.log_stats.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;


import net.mikaboshi.intra_mart.tools.log_stats.formatter.ReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.formatter.SimpleTextFileReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.formatter.TemplateFileReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

/**
 * ログ統計レポート設定のネスと要素
 */
public class ReportParameterDataType extends DataType {

	/** レポートタイプ（html|template|csv|tsv） */
	private String type = "html";

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
	private String template = null;

	/**
	 * カスタムテンプレートファイルの文字コード
	 */
	private String templateCharset = null;

	/** レポート出力先パス */
	private String output = "report_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

	/** レポートの文字コード */
	private String charset = Charset.defaultCharset().toString();

	public void setType(String type) {
		this.type = type;
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

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setTemplateCharset(String templateCharset) {
		this.templateCharset = templateCharset;
	}

	public void setOutput(String output) {
		this.output = output;
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

		ReportFormatter reportFormatter = null;

		File outputFile = new File(this.output);

		if ("html".equalsIgnoreCase(this.type) || "template".equalsIgnoreCase(this.type)) {

			reportFormatter = new TemplateFileReportFormatter(
					outputFile, this.charset, parserParameter, reportParameter);

			// カスタムテンプレートの設定
			if (this.template != null) {

				try {
					((TemplateFileReportFormatter) reportFormatter).setTemplateFile(new File(this.template));
				} catch (FileNotFoundException e) {
					throw new BuildException(e);
				}
			}

			if (this.templateCharset != null) {
				((TemplateFileReportFormatter) reportFormatter).setTemplateFileCharset(this.templateCharset);
			}

		} else if ("csv".equalsIgnoreCase(this.type)) {

			reportFormatter = new SimpleTextFileReportFormatter(
					outputFile, this.charset, ',', parserParameter, reportParameter);

		} else if ("tsv".equalsIgnoreCase(this.type)) {

			reportFormatter = new SimpleTextFileReportFormatter(
					outputFile, this.charset, '\t', parserParameter, reportParameter);

		} else {
			throw new BuildException("Unsupported report type : " + this.type);
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
