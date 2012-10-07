package net.mikaboshi.intra_mart.tools.log_stats.parser;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;

/**
 * 例外ログパーサ
 */
public abstract class ExceptionLogParser extends GenericLogParser implements LogParser<ExceptionLog> {

	public ExceptionLogParser(ParserParameter parameter) {
		super(parameter);
	}

	public static ExceptionLogParser create(ParserParameter parameter) {

		Version version = parameter.getVersion();

		if (version.isVersion6()) {

			return new ExceptionLogParserV6(parameter);

		} else if (version.isVersion7()) {

			return new ExceptionLogParserV7(parameter);
		}

		throw new IllegalArgumentException("Unsupported version : " + version);
	}
}
