package net.mikaboshi.intra_mart.tools.log_stats.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mikaboshi.intra_mart.tools.log_stats.formatter.SimpleTextFileReportFormatter;
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
		
		SimpleTextFileReportFormatter reportFormatter =
										new SimpleTextFileReportFormatter(
												new File("temp/report.csv"),
												"Windows-31J",
												',',
												parserParameter,
												reportParameter);

		generator.execute(reportFormatter);
	}

}
