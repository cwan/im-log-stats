package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.util.Map;

import net.mikaboshi.intra_mart.tools.log_stats.entity.LogLayoutItemType;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;


/**
 * 画面遷移ログパーサ
 */
public class TransitionLogParser extends LineLogParser implements LogParser<TransitionLog> {

	public TransitionLogParser(ParserParameter parameter, String layout) {

		super(parameter, layout);
	}

	public TransitionLog parse(String line) {

		Map<LogLayoutItemType, Object> lineMap = parseLine(line);

		if (lineMap == null) {
			return null;
		}

		TransitionLog log = new TransitionLog();

		populate(lineMap, log);

		if (!isInRange(log)) {
			return null;
		}

		log.type = (TransitionType) lineMap.get(LogLayoutItemType.MDC_TRANSITION_LOG_TYPE_ID);
		log.requestRemoteHost = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_REMOTE_HOST);
		log.requestRemoteAddress = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_REMOTE_ADDRESS);
		log.transitionAccessUserId = (String) lineMap.get(LogLayoutItemType.MDC_TRANSITION_ACCESS_USER_ID);
		log.transitionPathPageNext = (String) lineMap.get(LogLayoutItemType.MDC_TRANSITION_PATH_PAGE_NEXT);
		log.transitionExceptionName = (String) lineMap.get(LogLayoutItemType.MDC_TRANSITION_EXCEPTION_NAME);
		log.transitionExceptionMessage = (String) lineMap.get(LogLayoutItemType.MDC_TRANSITION_EXCEPTION_MESSAGE);
		log.transitionPathPagePrevious = (String) lineMap.get(LogLayoutItemType.MDC_TRANSITION_PATH_PAGE_PREVIOUS);

		Long transitionTimeResponse = (Long) lineMap.get(LogLayoutItemType.MDC_TRANSITION_TIME_RESPONSE);

		if (transitionTimeResponse != null) {
			log.transitionTimeResponse = transitionTimeResponse;
		}

		return log;
	}
}
