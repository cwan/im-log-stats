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

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class AntParameterUtilTest {

	@Test
	public void getReportOutput_パターンなし() {

		String input = "report_20121012.html";
		String actual = AntParameterUtil.getReportOutput(input);

		assertEquals(input, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getReportOutput_不正なフォーマット() {

		String input = "report_{T}.html";
		AntParameterUtil.getReportOutput(input);
	}

	@Test
	public void getReportOutput_パターン1つ() {

		String input = "report_{yyyyMMdd_HHmm}.html";
		String actual = AntParameterUtil.getReportOutput(input);
		String expected = "report_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".html";

		assertEquals(expected, actual);
	}

	@Test
	public void getReportOutput_パターン2つ() {

		String input = "report_{yyyyMMdd}-{HHmm}.html";
		String actual = AntParameterUtil.getReportOutput(input);
		String expected = "report_" + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()) + ".html";

		assertEquals(expected, actual);
	}

	@Test
	public void parseToday_todayなし() {

		String input = "2012/10/01";
		String actual = AntParameterUtil.parseToday(input);

		assertEquals(input, actual);
	}

	@Test
	public void parseToday_todayのみ() {

		String input = "today";
		String actual = AntParameterUtil.parseToday(input);
		String expecred = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

		assertEquals(expecred, actual);
	}

	@Test
	public void parseToday_todayの1日前() {

		String input = "today - 1";
		String actual = AntParameterUtil.parseToday(input);

		Date date = new Date();
		date = new Date(date.getTime() - 1L * 24L * 60L * 60L * 1000L);

		String expecred = new SimpleDateFormat("yyyy/MM/dd").format(date);

		assertEquals(expecred, actual);
	}

	@Test
	public void parseToday_todayの1日前_スペース無し() {

		String input = "today-1";
		String actual = AntParameterUtil.parseToday(input);

		Date date = new Date();
		date = new Date(date.getTime() - 1L * 24L * 60L * 60L * 1000L);

		String expecred = new SimpleDateFormat("yyyy/MM/dd").format(date);

		assertEquals(expecred, actual);
	}

	@Test
	public void parseToday_todayの100日前() {

		String input = "today - 100";
		String actual = AntParameterUtil.parseToday(input);

		Date date = new Date();
		date = new Date(date.getTime() - 100L * 24L * 60L * 60L * 1000L);

		String expecred = new SimpleDateFormat("yyyy/MM/dd").format(date);

		assertEquals(expecred, actual);
	}

	@Test
	public void parseToday_null() {

		String input = null;
		String actual = AntParameterUtil.parseToday(input);

		assertNull(actual);
	}
}
