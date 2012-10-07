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
 * 致命的な実行時例外
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class FatalRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7368239629813870028L;

	public FatalRuntimeException() {
	}

	public FatalRuntimeException(String message) {
		super(message);
	}

	public FatalRuntimeException(Throwable cause) {
		super(cause);
	}

	public FatalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
