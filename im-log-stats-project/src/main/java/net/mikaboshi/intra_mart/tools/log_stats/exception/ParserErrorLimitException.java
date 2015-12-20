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

package net.mikaboshi.intra_mart.tools.log_stats.exception;

/**
 * パーサエラーの上限数を超えた時にスローされる例外クラス。
 *
 * @version 1.0.10
 * @since 1.0.10
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ParserErrorLimitException extends RuntimeException {

	private static final long serialVersionUID = -1794287316225077318L;

	public ParserErrorLimitException(int n) {
		super("Pasing is aborted due to too many errors (" + n + ")");
	}
}
