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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 例外ログ
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
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
