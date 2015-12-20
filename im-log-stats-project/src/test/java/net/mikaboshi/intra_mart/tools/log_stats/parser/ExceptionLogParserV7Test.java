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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.Level;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ExceptionLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ExceptionLogParserV7;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ExceptionLogParserV7Test {

	@Test
	public void test_メッセージ1行() throws ParseException {

		String logText = StringUtils.join(new String[] {
					"log.generating.time=Sun Nov 13 15:55:39 KST 2011",
					"log.level=ERROR",
					"log.logger.name=jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject",
					"log.id=5i0slu795nope",
					"log.thread.id=http-APP:localhost:8088-8088-0$312771783",
					"log.thread.group=main",
					"log.message=JNDI名(jdbc/oracle)は既に登録されています。",
					"",
					"jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: JNDI名(jdbc/oracle)は既に登録されています。",
					"\tat jp.co.intra_mart.foundation.database.ResinDataSourceConfigurater.bind(ResinDataSourceConfigurater.java:111)",
					"\tat jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject.jsFunction_bind(ResinDataSourceConfiguraterObject.java:152)"
				}, IOUtils.LINE_SEPARATOR);

		ExceptionLogParser exceptionLogParser = new ExceptionLogParserV7(new ParserParameter());

		ExceptionLog log = exceptionLogParser.parse(logText);

		assertEquals(
				new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse("Sun Nov 13 15:55:39 KST 2011"),
				log.date);

		assertEquals(Level.ERROR, log.level);
		assertEquals("jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject", log.logger);
		assertEquals("5i0slu795nope", log.logId);
		assertEquals("http-APP:localhost:8088-8088-0$312771783", log.thread);
		assertEquals("main", log.logThreadGroup);
		assertEquals("JNDI名(jdbc/oracle)は既に登録されています。", log.message);
		assertEquals("jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: JNDI名(jdbc/oracle)は既に登録されています。", log.getFirstLineOfStackTrace());
	}

	@Test
	public void test_メッセージなし() throws ParseException {

		String logText = StringUtils.join(new String[] {
					"log.generating.time=Sun Nov 13 15:55:39 KST 2011",
					"log.level=ERROR",
					"log.logger.name=jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject",
					"log.id=5i0slu795nope",
					"log.thread.id=http-APP:localhost:8088-8088-0$312771783",
					"log.thread.group=main",
					"log.message=",
					"",
					"jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: ",
					"\tat jp.co.intra_mart.foundation.database.ResinDataSourceConfigurater.bind(ResinDataSourceConfigurater.java:111)",
					"\tat jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject.jsFunction_bind(ResinDataSourceConfiguraterObject.java:152)"
				}, IOUtils.LINE_SEPARATOR);

		ExceptionLogParser exceptionLogParser = new ExceptionLogParserV7(new ParserParameter());

		ExceptionLog log = exceptionLogParser.parse(logText);

		assertEquals(
				new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse("Sun Nov 13 15:55:39 KST 2011"),
				log.date);

		assertEquals(Level.ERROR, log.level);
		assertEquals("jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject", log.logger);
		assertEquals("5i0slu795nope", log.logId);
		assertEquals("http-APP:localhost:8088-8088-0$312771783", log.thread);
		assertEquals("main", log.logThreadGroup);
		assertEquals("", log.message);
		assertEquals("jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: ", log.getFirstLineOfStackTrace());
	}

	@Test
	public void test_メッセージ複数行() throws ParseException {

		String logText = StringUtils.join(new String[] {
					"log.generating.time=Sun Nov 13 15:55:39 KST 2011",
					"log.level=ERROR",
					"log.logger.name=jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject",
					"log.id=5i0slu795nope",
					"log.thread.id=http-APP:localhost:8088-8088-0$312771783",
					"log.thread.group=main",
					"log.message=メッセージ1行目",
					"メッセージ2行目",
					"メッセージ3行目",
					"",
					"jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: メッセージ1行目",
					"メッセージ2行目",
					"メッセージ3行目",
					"\tat jp.co.intra_mart.foundation.database.ResinDataSourceConfigurater.bind(ResinDataSourceConfigurater.java:111)",
					"\tat jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject.jsFunction_bind(ResinDataSourceConfiguraterObject.java:152)"
				}, IOUtils.LINE_SEPARATOR);

		ExceptionLogParser exceptionLogParser = new ExceptionLogParserV7(new ParserParameter());

		ExceptionLog log = exceptionLogParser.parse(logText);

		assertEquals(
				new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse("Sun Nov 13 15:55:39 KST 2011"),
				log.date);

		assertEquals(Level.ERROR, log.level);
		assertEquals("jp.co.intra_mart.system.javascript.imapi.ResinDataSourceConfiguraterObject", log.logger);
		assertEquals("5i0slu795nope", log.logId);
		assertEquals("http-APP:localhost:8088-8088-0$312771783", log.thread);
		assertEquals("main", log.logThreadGroup);
		assertEquals("メッセージ1行目" + IOUtils.LINE_SEPARATOR + "メッセージ2行目" + IOUtils.LINE_SEPARATOR + "メッセージ3行目", log.message);
		assertEquals("jp.co.intra_mart.foundation.database.exception.DataSourceConfigurationException: メッセージ1行目", log.getFirstLineOfStackTrace());
	}
}
