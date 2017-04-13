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

package net.mikaboshi.intra_mart.tools.log_stats.parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 行ログパーサのテストケース
 *
 * @version 1.0.21
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class LineLogParserTest {

	@Test(expected = NullPointerException.class)
	public void test_layout_null() {

		String layout = null;

		new LineLogParserMock(layout);
	}

	@Test
	public void test_layout_空文字() {

		String layout = "";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_レイアウト項目なし() {

		String layout = "ABC";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^ABC$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_レイアウト項目1つ() {

		String layout = "%logger\tABC";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^(.*?)\\tABC$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_複雑() {

		String layout = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^\\[(.*?)\\]\\t\\[(.*?)\\]\\t(.*?)\\t(.*?)$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_リクエストログV72標準() {

		String layout = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{client.session.id}\t%X{request.remote.host}\t%X{request.method}\t%X{request.url}\t%X{request.query_string}\t%X{request.url.referer}\t%X{request.page.time}\t%X{request.accept.time}\t%X{request.id}%nopex%n";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^\\[(.*?)\\]\\t\\[(.*?)\\]\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t\\-\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_画面遷移ログV72標準() {

		String layout = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{transition.log.type.id}\t%X{request.remote.address}\t%X{request.remote.host}\t%X{transition.access.user.id}\t%X{client.session.id}\t%X{transition.path.page.next}\t%X{transition.time.response}\t%X{transition.exception.name}\t%X{transition.exception.message}\t%X{transition.path.page.previous}\t%X{request.id}%nopex%n";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^\\[(.*?)\\]\\t\\[(.*?)\\]\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t\\-\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)\\t(.*?)$";

		assertEquals(expected, actual);
	}

	@Test
	public void test_layout_独自MDC() {
		String layout = "[%X{foo}]\t[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]";

		LineLogParser parser = new LineLogParserMock(layout);
		String actual = parser.logLayoutPattern.pattern();
		String expected = "^\\[(.*?)\\]\\t\\[(.*?)\\]\\t\\[(.*?)\\]$";

		assertEquals(expected, actual);
	}

	private static class LineLogParserMock extends LineLogParser {
		public LineLogParserMock(String layout) {
			super(new ParserParameter(), layout);
		}
	}
}
