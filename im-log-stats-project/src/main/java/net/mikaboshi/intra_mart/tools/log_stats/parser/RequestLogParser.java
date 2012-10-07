package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.util.Date;
import java.util.Map;

import net.mikaboshi.intra_mart.tools.log_stats.entity.LogLayoutItemType;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;


/**
 * リクエストログパーサ
 */
public class RequestLogParser extends LineLogParser implements LogParser<RequestLog> {

	public RequestLogParser(ParserParameter parameter, String layout) {

		super(parameter, layout);
	}

	public RequestLog parse(String line) {

		Map<LogLayoutItemType, Object> lineMap = parseLine(line);

		if (lineMap == null) {
			return null;
		}

		RequestLog log = new RequestLog();

		populate(lineMap, log);

		if (!isInRange(log) || log.isIN()) {
			return null;
		}

		log.requestRemoteHost = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_REMOTE_HOST);
		log.requestRemoteAddress = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_REMOTE_ADDRESS);
		log.requestMethod = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_METHOD);
		log.requestUrl = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_URL);
		log.requestQueryString = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_QUERY_STRING);
		log.requestUrlReferer = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_URL_REFERER);
		log.requestAcceptTime = (Date) lineMap.get(LogLayoutItemType.MDC_REQUEST_ACCEPT_TIME);

		Long requestPageTime = (Long) lineMap.get(LogLayoutItemType.MDC_REQUEST_PAGE_TIME);

		if (requestPageTime != null) {
			log.requestPageTime = requestPageTime;
		}

		return log;
	}
}
