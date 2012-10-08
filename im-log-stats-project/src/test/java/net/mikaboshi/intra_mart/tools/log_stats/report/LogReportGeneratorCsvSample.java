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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mikaboshi.intra_mart.tools.log_stats.formatter.TemplateFileReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.report.LogReportGenerator;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;


public class LogReportGeneratorCsvSample {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		LogReportGenerator generator = new LogReportGenerator();

		List<File> requestLogFiles = new ArrayList<File>();
		requestLogFiles.add(new File("C:/imart/iwp724/log/platform/request.log"));
		generator.setRequestLogFiles(requestLogFiles);

		List<File> transitionLogFiles = new ArrayList<File>();
		transitionLogFiles.add(new File("C:/imart/iwp724/log/platform/transition.log"));
		generator.setTransitionLogFiles(transitionLogFiles);

		File[] exceptionFiles = new File("C:/imart/iwp724/log/platform/exception").listFiles();
		generator.setExceptionLogFiles(Arrays.asList(exceptionFiles));

		ParserParameter parserParameter = new ParserParameter();
		parserParameter.setCharset("Windows-31J");
		generator.setParserParameter(parserParameter);

		ReportParameter reportParameter = new ReportParameter();
		reportParameter.setSessionTimeout(10);
		reportParameter.setSpan(5L);
		reportParameter.setRequestPageTimeRankSize(50);
		reportParameter.setRequestUrlRankSize(40);
		reportParameter.setSessionRankSize(30);
		reportParameter.setName("テスト  ログ統計レポート");
		reportParameter.setSignature("LogReportGeneratorCsvSample");
		generator.setReportParameter(reportParameter);

		TemplateFileReportFormatter reportFormatter =
										new TemplateFileReportFormatter(
														new File("temp/report.csv"),
														"Windows-31J",
														parserParameter,
														reportParameter);

		generator.execute(reportFormatter);
	}

}
