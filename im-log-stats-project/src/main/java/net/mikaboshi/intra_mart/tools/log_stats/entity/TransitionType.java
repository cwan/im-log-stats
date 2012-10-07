package net.mikaboshi.intra_mart.tools.log_stats.entity;

/**
 * 画面遷移タイプ
 */
public enum TransitionType {
	
	REQUEST,
	FORWARD,
	INCLUDE;
	
	public static TransitionType toEnum(String s) {
		
		if (s == null) {
			return null;
		}
		
		s = s.trim().toUpperCase();
		
		if ("REQUEST".equals(s)) {
			return REQUEST;
		} else if ("FORWARD".equals(s)) {
			return FORWARD;
		} else if ("INCLUDE".equals(s)) {
			return INCLUDE;
		} else {
			return null;
		}
	}
}
