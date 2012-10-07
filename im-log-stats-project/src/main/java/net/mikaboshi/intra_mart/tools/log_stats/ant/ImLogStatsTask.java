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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import net.mikaboshi.intra_mart.tools.log_stats.formatter.ReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;
import net.mikaboshi.intra_mart.tools.log_stats.report.LogReportGenerator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * intra-martログ統計レポート作成Antタスク。
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ImLogStatsTask extends Task {

	/** ログファイルパーサのパラメータ（ネスト要素） */
	private ParserParameterDataType parserParameterDataType = new ParserParameterDataType();

	/** レポートのパラメータ （ネスト要素）*/
	private ReportParameterDataType reportParameterDataType = new ReportParameterDataType();

	/** リクエストログファイル （ネスト要素） */
	private List<FileSet> requestLogFiles = new ArrayList<FileSet>();

	/** 画面遷移ログファイル（ネスト要素） */
	private List<FileSet> transitionLogFiles = new ArrayList<FileSet>();

	/** 例外ログファイル（ネスト要素） */
	private List<FileSet> exceptionLogFiles = new ArrayList<FileSet>();

	/** バージョン（属性） */
	private String version = Version.V72.getName();

	/** 遅延時間（ミリ秒） */
	private long delay = 0L;

	public ImLogStatsTask() {
		super();
	}

	@Override
	public void execute() throws BuildException {

		Version version = Version.toEnum(this.version);

		if (version == null) {
			throw new BuildException("Unsupported version : " + this.version);
		}

		LogReportGenerator generator = new LogReportGenerator();

		// パラメータの設定
		generator.setParserParameter(this.parserParameterDataType.toParserParameter(version));
		generator.setReportParameter(this.reportParameterDataType.toReportParameter(version));

		ReportFormatter reportFormatter =
				this.reportParameterDataType.getReportFormatter(
											generator.getParserParameter(),
											generator.getReportParameter());

		generator.setRequestLogFiles(getFiles(this.requestLogFiles));
		generator.setTransitionLogFiles(getFiles(this.transitionLogFiles));
		generator.setExceptionLogFiles(getFiles(this.exceptionLogFiles));

		generator.setDelay(this.delay);

		// 実行
		try {
			generator.execute(reportFormatter);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	/**
	 * パーサパラメータを設定する。
	 * @param parameterDataType
	 */
	public void addConfigured(ParserParameterDataType parameterDataType) {
		this.parserParameterDataType = parameterDataType;
	}

	/**
	 * レポートパラメータを設定する。
	 * @param reportParameterDataType
	 */
	public void addConfigured(ReportParameterDataType reportParameterDataType) {
		this.reportParameterDataType = reportParameterDataType;
	}

	/**
	 * リクエストログファイルセットを追加する。
	 * @param fileset
	 */
	public void addRequestLogFiles(FileSet fileset) {
		this.requestLogFiles.add(fileset);
	}

	/**
	 * 画面遷移ログファイルセットを追加する。
	 * @param fileset
	 */
	public void addTransitionLogFiles(FileSet fileset) {
		this.transitionLogFiles.add(fileset);
	}

	/**
	 * 例外ログファイルセットを追加する。
	 * @param fileset
	 */
	public void addExceptionLogFiles(FileSet fileset) {
		this.exceptionLogFiles.add(fileset);
	}

	/**
	 * バージョンを設定する。
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * FileSetのリストからFileを抽出して、コレクションで返す。
	 * @param fileSetList
	 * @return
	 */
	private static Collection<File> getFiles(List<FileSet> fileSetList) {

		Set<File> files = new HashSet<File>();

		if (fileSetList == null) {
			return files;
		}

		for (FileSet fs : fileSetList) {

			for (@SuppressWarnings("unchecked") Iterator<Resource> iter = fs.iterator(); iter.hasNext();) {

				Resource resource = iter.next();

				if (resource instanceof FileResource && !resource.isDirectory()) {
					File file = ((FileResource) resource).getFile();
					files.add(file);
				}
			}
		}

		return files;
	}
}
