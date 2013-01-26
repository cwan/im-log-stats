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

package net.mikaboshi.intra_mart.tools.log_stats.entity;

import net.mikaboshi.intra_mart.tools.log_stats.util.LogStringUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 例外ログ
 *
 * @version 1.0.9
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ExceptionLog extends Log {

	/**
	 * 例外のグルーピングを何で行うか
	 *
	 * @since 1.0.8
	 */
	public static enum GroupingType {

		/** スタックトレースの1行目 */
		FIEST_LINE,

		/** スタックトレースの一番下のCaused by */
		CAUSE;
	}

	/**
	 * スタックトレース
	 */
	public String stackTrace = null;

	/**
	 * 例外のグルーピングを何で行うか
	 */
	public GroupingType groupingType = GroupingType.CAUSE;

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

	/**
	 * スタックトレースの発端のCause行を取得する。
	 * @return
	 * @since 1.0.8
	 */
	public String getCauseLineOfStackTrace() {

		if (this.stackTrace == null) {
			return null;
		}

		String cause = null;
		final String prefix = "Caused by: ";

		for (String line : LogStringUtil.lines(this.stackTrace)) {

			if (cause == null) {
				cause = line;
				continue;
			}

			if (line.startsWith(prefix)) {
				cause = line.substring(prefix.length());
				continue;
			}
		}

		return cause;
	}

	/**
	 * 例外ログをグルーピングする行を取得する。
	 * @return
	 * @since 1.0.8
	 */
	public String getGroupingLineOfStackTrace() {

		switch (this.groupingType) {
			case CAUSE:
				return getCauseLineOfStackTrace();
			case FIEST_LINE:
				return getFirstLineOfStackTrace();
			default:
				return null;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof ExceptionLog)) {
			return false;
		}

		ExceptionLog log2 = (ExceptionLog) obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.level, log2.level);

		switch (this.groupingType) {
			case CAUSE:
				equalsBuilder.append(this.getCauseLineOfStackTrace(), log2.getCauseLineOfStackTrace());
				break;

			case FIEST_LINE:
				equalsBuilder.append(this.getFirstLineOfStackTrace(), log2.getFirstLineOfStackTrace());
				equalsBuilder.append(this.message, log2.message);
				break;
		}

		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {

		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.level);

		switch (this.groupingType) {
			case CAUSE:
				hashCodeBuilder.append(this.getCauseLineOfStackTrace());
				break;

			case FIEST_LINE:
				hashCodeBuilder.append(this.getFirstLineOfStackTrace());
				hashCodeBuilder.append(this.message);
				break;
		}

		return hashCodeBuilder.toHashCode();
	}
}
