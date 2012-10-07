package net.mikaboshi.intra_mart.tools.log_stats.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.DataType;

/**
 * ログレイアウトのネスト要素
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
