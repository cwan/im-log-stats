package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.Level;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Ver.7xの例外ログパーサ
 */
public class ExceptionLogParserV7 extends ExceptionLogParser {

	/** 項目=値のパターン */
	private final static Pattern KEY_VALUE_PATTERN = Pattern.compile("^(.*?)=(.*?)$");

	private final static Pattern STACKTRACE_BIGIN_PATTERN = Pattern.compile("^\\S+?.*");

	/** log.generating.timeのフォーマット */
	private final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

	public ExceptionLogParserV7(ParserParameter parameter) {
		super(parameter);
	}

	protected String getValue(String s) {

		Matcher matcher = KEY_VALUE_PATTERN.matcher(s);

		if (matcher.matches()) {
			return matcher.group(2);
		}

		return null;
	}

	public ExceptionLog parse(String string) {

		if (string == null) {
			return null;
		}

		String[] lines = string.split("\\r?\\n");

		if (lines.length < 9) {
			warn("invalid exception log");
			return null;
		}

		ExceptionLog log = new ExceptionLog();

		try {
			log.date = dateFormat.parse(getValue(lines[0]));

		} catch (ParseException e) {
			warn("invalid date : " + lines[0]);
			return null;
		}

		if (!isInRange(log)) {
			return null;
		}

		log.level = Level.toEnum(getValue(lines[1]));

		log.logger = getValue(lines[2]);

		log.logId = getValue(lines[3]);

		log.thread = getValue(lines[4]);

		log.logThreadGroup = getValue(lines[5]);

		log.message = getValue(lines[6]);

		for (int i = 7; i < lines.length; i++) {

			if (lines[i].length() == 0 && i < lines.length - 1 &&
					STACKTRACE_BIGIN_PATTERN.matcher(lines[i + 1]).find()) {

				// 空行かつ次が空行ではない => ここからスタックトレース
				log.stackTrace = StringUtils.join(
						ArrayUtils.subarray(lines, i + 1, lines.length - 1), IOUtils.LINE_SEPARATOR);
				break;

			} else {
				// メッセージの続き
				log.message += IOUtils.LINE_SEPARATOR + lines[i];
			}
		}

		return log;
	}
}
