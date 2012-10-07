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

import java.util.Date;
import java.util.Map;

import net.mikaboshi.intra_mart.tools.log_stats.entity.LogLayoutItemType;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;


/**
 * リクエストログパーサ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
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
