package net.mikaboshi.intra_mart.tools.log_stats.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 例外ログ
 */
public class ExceptionLog extends Log {

	/**
	 * スタックトレース
	 */
	public String stackTrace = null;

	/**
	 * スタックトレースの1行目を取得する。
	 * @return
	 */
	public String getFirstLineOfStackTrace() {

		if (this.stackTrace == null) {
			return null;
		}

		for (int i = 0; i < this.stackTrace.length(); i++) {

			char c = this.stackTrace.charAt(i);

			if (c == '\r' || c == '\n' || c == '\t') {
				return this.stackTrace.substring(0, i);
			}
		}

		return this.stackTrace;
	}

	// レベル・メッセージ・スタックトレース1行目で同一性をチェックする

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof ExceptionLog)) {
			return false;
		}

		ExceptionLog log2 = (ExceptionLog) obj;

		return new EqualsBuilder()
			.append(this.level, log2.level)
			.append(this.getFirstLineOfStackTrace(), log2.getFirstLineOfStackTrace())
			.append(this.message, log2.message)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder()
				.append(this.level)
				.append(this.getFirstLineOfStackTrace())
				.append(this.message)
				.toHashCode();
	}
}
