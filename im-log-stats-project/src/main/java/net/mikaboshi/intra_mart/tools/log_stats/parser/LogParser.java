package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.io.File;

import net.mikaboshi.intra_mart.tools.log_stats.entity.Log;


/**
 * ログパーサインターフェース
 *
 * @param <T> Logクラス
 */
public interface LogParser<T extends Log> {

	/**
	 * ログ文字列を解析し、Logオブジェクトとして返す
	 * @param string
	 * @return
	 */
	public abstract T parse(String string);
	
	/**
	 * 現在解析中のログファイルを設定する
	 * @param logFile
	 */
	public abstract void setLogFile(File logFile);
}