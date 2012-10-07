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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.DataType;

/**
 * ログレイアウトのネスト要素
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public abstract class LogLayoutDataType extends DataType {

	private List<String> textList = new ArrayList<String>();

	public void addText(String s) {
		this.textList.add(s);
	}

	public List<String> getTextList() {
		return textList;
	}

	public String getLayout() {

		return StringUtils.join(this.textList, "").trim();
	}

	/** リクエストログレイアウト */
	public static class RequestLogLayout extends LogLayoutDataType {}

	/** 画面遷移ログレイアウト */
	public static class TransitionLogLayout extends LogLayoutDataType {}
}
