package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


import net.mikaboshi.intra_mart.tools.log_stats.entity.Level;
import net.mikaboshi.intra_mart.tools.log_stats.entity.LogLayoutItemType;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/**
 * 行ログパーサ
 */
public abstract class LineLogParser extends GenericLogParser {

	private static final Pattern LOG_LAYOUT_PATTERN =
			Pattern.compile("^(%(?:\\-\\d+)?([a-zA-Z0-9\\.]+)(?:\\{(.+?)\\})?).*");

	/** 現在解析中のログファイルの行番号 */
	private int nLine = 0;

	Pattern logLayoutPattern;

	private LayoutItem[] layoutItems;

	/** リクエストの受付時刻フォーマット */
	private final DateFormat requestAcceptTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	public LineLogParser(ParserParameter parameter, String layout) throws PatternSyntaxException {

		super(parameter);
		init(layout);
	}

	protected void init(String layout) {

		if (layout == null) {
			throw new NullPointerException();
		}

		List<LayoutItem> layoutItemList = new ArrayList<LineLogParser.LayoutItem>();

		StringBuilder sb = new StringBuilder();

		sb.append("^");

		int head = 0;

		while (head < layout.length()) {

			Matcher matcher = LOG_LAYOUT_PATTERN.matcher(layout.substring(head));

			if (matcher.matches()) {

				String name = matcher.group(2);
				String option = matcher.group(3);

				head += matcher.group(1).length();

				LayoutItem layoutItem = new LayoutItem();

				layoutItem.type = LogLayoutItemType.toEnum(name, option);

				if (layoutItem.type == null) {
					throw new IllegalArgumentException("Illegal log layout name : " + name + "(" + option + ")");
				}

				if (layoutItem.type == LogLayoutItemType.NOPEX || layoutItem.type == LogLayoutItemType.NEW_LINE) {
					// 例外出力制御、改行はログに現れないので無視
					continue;
				}

				sb.append("(.*?)");

				if (layoutItem.type == LogLayoutItemType.DATE) {
					layoutItem.dateFormat = new SimpleDateFormat(option);
				}

				layoutItemList.add(layoutItem);

			} else {

				char c = layout.charAt(head++);

				if (c == '\t') {
					sb.append("\\t");

				} else if (c == '\\' || c == '[' || c == ']' || c == '^' || c == '$' || c == '-' || c == '&' ||
						c == '.' || c == '+' || c == '*' || c == '{' || c == '}' || c == ',' || c == '|' ||
						c == '(' || c == ')' || c == ':' || c == '=' || c == '<' || c == '>' || c == '!') {

					sb.append("\\");
					sb.append(c);

				} else {

					sb.append(c);
				}
			}
		}

		sb.append("$");

		this.layoutItems = layoutItemList.toArray(new LayoutItem[layoutItemList.size()]);

		this.logLayoutPattern = Pattern.compile(sb.toString());
	}

	/**
	 * 1行を読み取って、Mapで返す。
	 * ログ行パターンに一致しない場合は、警告ログを出力し、nullを返す。
	 * @param line
	 * @return
	 */
	protected Map<LogLayoutItemType, Object> parseLine(String line) {

		this.nLine++;

		if (line == null) {
			throw new IllegalArgumentException("line is null");
		}

		Matcher matcher = this.logLayoutPattern.matcher(line);

		if (!matcher.matches()) {
			warn("Unparsable line");
			return null;
		}

		Map<LogLayoutItemType, Object> lineMap = new HashMap<LogLayoutItemType, Object>();

		for (int i = 0; i < this.layoutItems.length; i++) {

			LayoutItem item = this.layoutItems[i];
			String value = matcher.group(i + 1);

			if (value != null) {
				value = value.trim();
			}

			switch (item.type) {

				case DATE:

					try {
						lineMap.put(item.type, item.dateFormat.parse(value));
					} catch (ParseException e) {
						warn("Invalid date : %s", value);
					}

					break;

				case LEVEL:

					lineMap.put(item.type, Level.toEnum(value));
					break;

				case MDC_LOG_REPORT_SEQUENCE:

					try {
						// V6だとカンマ区切りになっている
						value = StringUtils.remove(value, ',');

						lineMap.put(item.type, Integer.parseInt(value));
					} catch (NumberFormatException e) {
						warn("Invalid integer : %s", value);
					}

					break;

				case MDC_REQUEST_PAGE_TIME:
				case MDC_TRANSITION_TIME_RESPONSE:

					try {
						// V6だとカンマ区切りになっている
						value = StringUtils.remove(value, ',');

						lineMap.put(item.type, Long.parseLong(value));

					} catch (NumberFormatException e) {
						warn("Invalid long : %s", value);
					}

					break;

				case MDC_REQUEST_ACCEPT_TIME:

					try {
						lineMap.put(item.type, this.requestAcceptTimeFormat.parse(value));
					} catch (ParseException e) {
						warn("Invalid request accept time : %s", value);
					}

					break;

				case MDC_TRANSITION_LOG_TYPE_ID:

					lineMap.put(item.type, TransitionType.toEnum(value));
					break;

				default:

					lineMap.put(item.type, value);
			}
		}

		return lineMap;
	}

	/**
	 * 共通のログ項目を設定する。
	 * @param lineMap
	 * @param log
	 */
	protected static void populate(
			Map<LogLayoutItemType, Object> lineMap,
			net.mikaboshi.intra_mart.tools.log_stats.entity.Log log) {

		log.logger = (String) lineMap.get(LogLayoutItemType.LOGGER_NAME);
		log.date = (Date) lineMap.get(LogLayoutItemType.DATE);
		log.message = (String) lineMap.get(LogLayoutItemType.MESSAGE);
		log.level = (Level) lineMap.get(LogLayoutItemType.LEVEL);
		log.thread = (String) lineMap.get(LogLayoutItemType.THREAD);
		log.requestId = (String) lineMap.get(LogLayoutItemType.MDC_REQUEST_ID);
		log.logId = (String) lineMap.get(LogLayoutItemType.MDC_LOG_ID);

		Integer seq = (Integer) lineMap.get(LogLayoutItemType.MDC_LOG_REPORT_SEQUENCE);

		if (seq != null) {
			log.logReportSequence = seq;
		}

		log.logThreadGroup = (String) lineMap.get(LogLayoutItemType.MDC_LOG_THREAD_GROUP);
		log.clientSessionId = (String) lineMap.get(LogLayoutItemType.MDC_CLIENT_SESSION_ID);
	}

	/**
	 * INFOレベルのログを出力する
	 */
	@Override
	protected void info(String format, Object ... args) {

		Log logger = getLogger();

		if (logger.isInfoEnabled()) {
			logger.info(String.format(
					"[" + getLogFilePath() + "#" + this.nLine + "] " +  format, args));
		}
	}

	/**
	 * WARNレベルのログを出力する
	 */
	@Override
	protected void warn(String format, Object ... args) {

		Log logger = getLogger();

		if (logger.isWarnEnabled()) {
			logger.warn(String.format(
					"[" + getLogFilePath() + "#" + this.nLine + "] " +  format, args));
		}
	}

	private static class LayoutItem {
		LogLayoutItemType type = null;
		DateFormat dateFormat = null;
	}
}
