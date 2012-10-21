/*
 * Licensed under the public Apache License, Version 2.0 (the "License");
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

package net.mikaboshi.intra_mart.tools.log_stats.parser;

import net.mikaboshi.intra_mart.tools.log_stats.exception.ParserErrorLimitException;

/**
 * パーサエラーの件数を数えるクラス。
 *
 * @version 1.0.10
 * @since 1.0.10
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ParserErrorCounter {

	private int limit = 1000;

	private int count = 0;

	/**
	 * デフォルトのコンストラクタ。
	 * 上限 = 1000 が適用される。
	 */
	public ParserErrorCounter() {
	}

	/**
	 * 上限を明示的に指定するコンストラクタ。
	 * limit に0以下を指定した場合、上限なしとなる。
	 *
	 * @param limit
	 */
	public ParserErrorCounter(int limit) {
		this.limit = limit;
	}


	/**
	 * エラー数を1カウントアップする。
	 * エラーの上限を超えた場合、ParserErrorLimitException をスローする。
	 *
	 * @throws ParserErrorLimitException
	 */
	public synchronized void increment() throws ParserErrorLimitException {

		if (this.limit > 0 && ++this.count > this.limit) {
			throw new ParserErrorLimitException(this.limit);
		}
	}
}
