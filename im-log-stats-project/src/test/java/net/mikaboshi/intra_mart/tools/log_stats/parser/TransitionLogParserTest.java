package net.mikaboshi.intra_mart.tools.log_stats.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;


import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;
import net.mikaboshi.intra_mart.tools.log_stats.parser.LogLayoutDefinitions;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.TransitionLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TransitionLogParserTest {

	@Test
	public void test_V61標準() {

		String layout = LogLayoutDefinitions.getStandardTransitionLogLayout(Version.V61);
		layout = LogLayoutDefinitions.convertV6toV7(layout);

		TransitionLogParser parser = new TransitionLogParser(new ParserParameter(), layout);

		parser.setLogFile(new File("C:/test.log"));

		String logText =
			StringUtils.join(new String[] {
						"[2012-01-05 18:46:10,213]",
						"FORWARD",
						"0:0:0:0:0:0:0:1",
						"0:0:0:0:0:0:0:1",
						"system",
						"abccjNJunB67b7RyUqTst",
						"/system/security/common/page_requester.jssp",
						"367",
						"-",
						"-",
						"/system.admin"
					},
					"\t");

		TransitionLog log = parser.parse(logText);

		assertEquals("2012-01-05 18:46:10,213", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(log.date));
		assertEquals(TransitionType.FORWARD, log.type);
		assertEquals("0:0:0:0:0:0:0:1", log.requestRemoteAddress);
		assertEquals("0:0:0:0:0:0:0:1", log.requestRemoteHost);
		assertEquals("system", log.transitionAccessUserId);
		assertEquals("abccjNJunB67b7RyUqTst", log.clientSessionId);
		assertEquals("/system/security/common/page_requester.jssp", log.transitionPathPageNext);
		assertEquals(367L, log.transitionTimeResponse);
		assertEquals("-", log.transitionExceptionName);
		assertEquals("-", log.transitionExceptionMessage);
		assertEquals("/system.admin", log.transitionPathPagePrevious);
	}

}
