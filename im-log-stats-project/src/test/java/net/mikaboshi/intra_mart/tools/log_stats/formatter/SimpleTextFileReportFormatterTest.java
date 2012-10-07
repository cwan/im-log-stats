package net.mikaboshi.intra_mart.tools.log_stats.formatter;

import static org.junit.Assert.assertEquals;

import java.io.File;


import net.mikaboshi.intra_mart.tools.log_stats.formatter.SimpleTextFileReportFormatter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.report.ReportParameter;

import org.junit.Before;

public class SimpleTextFileReportFormatterTest {

	private SimpleTextFileReportFormatter formatter = null;

	@Before
	public void setUp() {
		this.formatter = new SimpleTextFileReportFormatter(new File("dummy"), null, ',', new ParserParameter(), new ReportParameter());
	}

	@org.junit.Test
	public void test_add_エスケープなし() {

		String s = "ABCあいうえお";

		assertEquals(s, this.formatter.escape(s));
	}

	@org.junit.Test
	public void test_add_null() {

		String s = null;

		assertEquals("", this.formatter.escape(s));
	}

	@org.junit.Test
	public void test_add_エスケープCSV() {

		String s = "abc,def,ghi";

		assertEquals("\"abc,def,ghi\"", this.formatter.escape(s));
	}

	@org.junit.Test
	public void test_add_エスケープCSVクォート() {

		String s = "a\"bc,def,gh\"i";

		assertEquals("\"a\"\"bc,def,gh\"\"i\"", this.formatter.escape(s));
	}

	@org.junit.Test
	public void test_add_エスケープなしTSV() {

		this.formatter = new SimpleTextFileReportFormatter(new File("dummy"), null, '\t', new ParserParameter(), new ReportParameter());

		String s = "abc,def,ghi";

		assertEquals("abc,def,ghi", this.formatter.escape(s));
	}

	@org.junit.Test
	public void test_add_エスケープTSV改行() {

		this.formatter = new SimpleTextFileReportFormatter(new File("dummy"), null, '\t', new ParserParameter(), new ReportParameter());

		String s = "abc,def,ghi\r\njkl";

		assertEquals("\"abc,def,ghi\r\njkl\"", this.formatter.escape(s));
	}

}
