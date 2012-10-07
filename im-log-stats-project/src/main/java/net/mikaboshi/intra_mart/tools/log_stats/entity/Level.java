package net.mikaboshi.intra_mart.tools.log_stats.entity;

/**
 * ログレベル
 */
public enum Level {
	
	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR;
	
	public static Level toEnum(String s) {
		
		if (s == null) {
			return null;
		}
		
		s = s.trim().toUpperCase();
		
		if ("INFO".equals(s)) {
			return INFO;
		} else if ("ERROR".equals(s)) {
			return ERROR;
		} else if ("WARN".equals(s)) {
			return WARN;
		} else if ("DEBUG".equals(s)) {
			return DEBUG;
		} else if ("TRACE".equals(s)) {
			return TRACE;
		} else {
			return null;
		}
	}
}
