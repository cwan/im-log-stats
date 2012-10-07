package net.mikaboshi.intra_mart.tools.log_stats.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;


import net.mikaboshi.intra_mart.tools.log_stats.entity.Level;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.parser.LogLayoutDefinitions;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.RequestLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class RequestLogParserTest {

	@Test
	public void test_V72標準() {

		RequestLogParser parser = new RequestLogParser(
					new ParserParameter(),
					LogLayoutDefinitions.getStandardRequestLogLayout(Version.V72));

		parser.setLogFile(new File("C:/test.log"));

		String logText =
			StringUtils.join(new String[] {
						"[2011-11-13 15:46:58.258]",
						"[http-APP:localhost:8088-8088-13$412302005]",
						"18",
						"INFO ",
						"REQUEST_LOG.jp.co.intra_mart.system.servlet.filter.RequestLogFilter",
						"5i0slu6q9cib2",
						"-",
						"abceQ4K8o3xXrgffBRBot",
						"0:0:0:0:0:0:0:1",
						"POST",
						"http://localhost:8088/imart/system(2f)setting(2f)system(2f)login(5f)groups(2f)main.jssps",
						"im_mark=r-7xij29*vhp35p",
						"http://localhost:8088/imart/SystemMenu5.menu",
						"281",
						"2011-11-13 15:46:57,977",
						"5i0slu6q94p952g"
					},
					"\t");

		RequestLog log = parser.parse(logText);

		assertEquals("2011-11-13 15:46:58.258", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(log.date));
		assertEquals("http-APP:localhost:8088-8088-13$412302005", log.thread);
		assertEquals(18, log.logReportSequence);
		assertEquals(Level.INFO, log.level);
		assertEquals("REQUEST_LOG.jp.co.intra_mart.system.servlet.filter.RequestLogFilter", log.logger);
		assertEquals("5i0slu6q9cib2", log.logId);
		assertEquals("abceQ4K8o3xXrgffBRBot", log.clientSessionId);
		assertEquals("0:0:0:0:0:0:0:1", log.requestRemoteHost);
		assertEquals("POST", log.requestMethod);
		assertEquals("http://localhost:8088/imart/system(2f)setting(2f)system(2f)login(5f)groups(2f)main.jssps", log.requestUrl);
		assertEquals("r-7xij29*vhp35p", log.getQueryValue("im_mark"));
		assertEquals("http://localhost:8088/imart/SystemMenu5.menu", log.requestUrlReferer);
		assertEquals(281L, log.requestPageTime);
		assertEquals("2011-11-13 15:46:57,977", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(log.requestAcceptTime));
		assertEquals("5i0slu6q94p952g", log.requestId);
	}

	@Test
	public void test_V70標準() {

		RequestLogParser parser = new RequestLogParser(
					new ParserParameter(),
					LogLayoutDefinitions.getStandardRequestLogLayout(Version.V70));

		parser.setLogFile(new File("C:/test.log"));

		String logText =
			StringUtils.join(new String[] {
						"[2010-11-30 15:07:31.700]",
						"[hmux-10.1.83.82:6802-9$26690565]",
						"1209",
						"INFO ",
						"REQUEST_LOG.jp.co.intra_mart.system.servlet.filter.RequestLogFilter",
						"5hx97pjy0tw3c",
						"-",
						"abcMjnWQrAjtv75-mgAYs",
						"10.48.9.119",
						"http://snr1ap00.joho.shinryo/imart/jchkg042000.do",
						"http://snr1ap00.joho.shinryo/imart/jchkg900010.do",
						"78",
						"2010-11-30 15:07:31,622",
						"5hx97pjy0rq12c2"
					},
					"\t");

		RequestLog log = parser.parse(logText);

		assertEquals("2010-11-30 15:07:31.700", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(log.date));
		assertEquals("hmux-10.1.83.82:6802-9$26690565", log.thread);
		assertEquals(1209, log.logReportSequence);
		assertEquals(Level.INFO, log.level);
		assertEquals("REQUEST_LOG.jp.co.intra_mart.system.servlet.filter.RequestLogFilter", log.logger);
		assertEquals("5hx97pjy0tw3c", log.logId);
		assertEquals("abcMjnWQrAjtv75-mgAYs", log.clientSessionId);
		assertEquals("10.48.9.119", log.requestRemoteHost);
		assertEquals("http://snr1ap00.joho.shinryo/imart/jchkg042000.do", log.requestUrl);
		assertEquals("http://snr1ap00.joho.shinryo/imart/jchkg900010.do", log.requestUrlReferer);
		assertEquals(78L, log.requestPageTime);
		assertEquals("2010-11-30 15:07:31,622", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(log.requestAcceptTime));
		assertEquals("5hx97pjy0rq12c2", log.requestId);
	}

}
