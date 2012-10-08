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

package net.mikaboshi.intra_mart.tools.log_stats.ant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import net.mikaboshi.intra_mart.tools.log_stats.ant.LogLayoutDataType.RequestLogLayout;
import net.mikaboshi.intra_mart.tools.log_stats.ant.LogLayoutDataType.TransitionLogLayout;
import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

/**
 * ログファイルパーサ設定のネスト要素
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ParserParameterDataType extends DataType {

	/** begin, endのフォーマット */
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/** ログファイルの文字コード */
	private String charset = null;

	/** リクエストログレイアウト */
	private RequestLogLayout requestLogLayout = null;

	/** 画面遷移ログレイアウト */
	private TransitionLogLayout transitionLogLayout = null;

	/** 開始日時（これ以前のログは切り捨てる） */
	private Date begin = null;

	/** 終了日時（これ以降のログは切り捨てる） */
	private Date end = null;

	/** 例外のグルーピング方法 */
	private ExceptionLog.GroupingType exceptionGroupingType = null;

	public void addConfigured(RequestLogLayout requestLogLayout) {
		this.requestLogLayout = requestLogLayout;
	}

	public void addConfigured(TransitionLogLayout transitionLogLayout) {
		this.transitionLogLayout = transitionLogLayout;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setBegin(String begin) throws BuildException {

		if (begin != null) {

			begin = StringUtils.replaceChars(begin, '/', '-');

			if (begin.length() == 10) {
				begin += " 00:00";
			}
		}

		try {
			this.begin = this.dateFormat.parse(begin);
		} catch (ParseException e) {
			throw new BuildException("begin's date format is invalid : " + begin);
		}
	}

	public void setEnd(String end) throws BuildException {

		if (end != null) {

			end = StringUtils.replaceChars(end, '/', '-');

			if (end.length() == 10) {
				end += " 24:00";
			}
		}

		try {
			this.end = this.dateFormat.parse(end);
		} catch (ParseException e) {
			throw new BuildException("end's date format is invalid : " + end);
		}
	}

	public void setExceptionGroupingType(String exceptionGroupingType) {

		if (exceptionGroupingType != null) {
			if ("first-line".equalsIgnoreCase(exceptionGroupingType)) {
				this.exceptionGroupingType = ExceptionLog.GroupingType.FIEST_LINE;

			} else if ("cause".equalsIgnoreCase(exceptionGroupingType)) {
				this.exceptionGroupingType = ExceptionLog.GroupingType.CAUSE;

			} else {
				throw new BuildException("exceptionGroupingType is invalid : " + exceptionGroupingType);
			}
		}
	}

	/**
	 * 自身の内容をParserParameterインスタンスに変換する。
	 * @param version
	 * @return
	 */
	public ParserParameter toParserParameter(Version version) {

		ParserParameter parameter = new ParserParameter();

		parameter.setVersion(version);

		if (this.charset != null) {
			parameter.setCharset(this.charset);
		}

		if (this.requestLogLayout != null) {
			parameter.setRequestLogLayout(this.requestLogLayout.getLayout());
		}

		if (this.transitionLogLayout != null) {
			parameter.setTransitionLogLayout(this.transitionLogLayout.getLayout());
		}

		parameter.setBegin(this.begin);
		parameter.setEnd(this.end);

		if (this.exceptionGroupingType != null) {
			parameter.setExceptionGroupingType(this.exceptionGroupingType);
		}

		return parameter;
	}

}
