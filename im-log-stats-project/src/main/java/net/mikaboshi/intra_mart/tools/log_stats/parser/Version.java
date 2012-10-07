package net.mikaboshi.intra_mart.tools.log_stats.parser;

/**
 * ログファイルのバージョン
 */
public enum Version {

	V60("6.0"),

	V61("6.1"),

	V70("7.0"),

	V71("7.1"),

	V72("7.2");

	private final String name;

	private Version(String name) {
		this.name = name;
	}

	public static Version toEnum(String name) {

		for (Version version : Version.values()) {
			if (version.name.equals(name)) {
				return version;
			}
		}

		return null;
	}

	public boolean isVersion6() {
		return this == V60 || this == V61;
	}

	public boolean isVersion7() {
		return this == V70 || this == V71 || this == V72;
	}

	public String getName() {
		return name;
	}
}
