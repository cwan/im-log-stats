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

package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.nio.charset.Charset;
import java.util.Date;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;

/**
 * ログファイルのパースに関するパラメータ。
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ParserParameter {

	/** ログファイルの文字コード */
	private String charset = Charset.defaultCharset().toString();

	/** リクエストログのレイアウト */
	private String requestLogLayout = null;

	/** 画面遷移ログのレイアウト */
	private String transitionLogLayout = null;

	/** 開始日時（これ以前のログは切り捨てる） */
	private Date begin = null;

	/** 終了日時（これ以降のログは切り捨てる） */
	private Date end = null;

	/** ログのバージョン */
	private Version version = Version.V72;

	/** 例外のグルーピング方法 */
	private ExceptionLog.GroupingType exceptionGroupingType = ExceptionLog.GroupingType.CAUSE;

	/**
	 * @return charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset セットする charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * リクエストログレイアウトを取得する。
	 * 未設定の場合は、バージョン標準のレイアウトを取得する。
	 * @return requestLogLayout
	 */
	public String getRequestLogLayout() {

		if (this.requestLogLayout == null) {
			return LogLayoutDefinitions.getStandardRequestLogLayout(getVersion());
		} else {
			return requestLogLayout;
		}
	}

	/**
	 * @param requestLogLayout セットする requestLogLayout
	 */
	public void setRequestLogLayout(String requestLogLayout) {
		this.requestLogLayout = requestLogLayout;
	}

	/**
	 * 画面遷移ログレイアウトを取得する。
	 * 未設定の場合は、バージョン標準のレイアウトを取得する。
	 * @return transitionLogLayout
	 */
	public String getTransitionLogLayout() {

		if (this.transitionLogLayout == null) {
			return LogLayoutDefinitions.getStandardTransitionLogLayout(getVersion());
		} else {
			return transitionLogLayout;
		}
	}

	/**
	 * @param transitionLogLayout セットする transitionLogLayout
	 */
	public void setTransitionLogLayout(String transitionLogLayout) {
		this.transitionLogLayout = transitionLogLayout;
	}

	/**
	 * @return begin
	 */
	public Date getBegin() {
		return begin;
	}

	/**
	 *
	 * @return
	 * @since 1.0.8
	 */
	public ExceptionLog.GroupingType getExceptionGroupingType() {
		return exceptionGroupingType;
	}

	/**
	 * @param begin セットする begin
	 */
	public void setBegin(Date begin) {
		this.begin = begin;
	}

	/**
	 * @return end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end セットする end
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * @return version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @param version セットする version
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 *
	 * @param exceptionGroupingType
	 * @since 1.0.8
	 */
	public void setExceptionGroupingType(
			ExceptionLog.GroupingType exceptionGroupingType) {
		this.exceptionGroupingType = exceptionGroupingType;
	}

}
