package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * ログファイルを1行ずつ読み込む。
 * </p>
 * <p>
 * 最後に{@link #close()}を実行すること。
 * </p>
 */
public class LogFileReader {

	private static final org.apache.commons.logging.Log logger = LogFactory.getLog(LogFileReader.class);

	/** ログファイルのリスト */
	private final List<File> logFiles;

	/** ログファイルパーサ */
	private final LogParser<?> logParser;

	/** 現在解析中のログファイルを示すlogFilesのインデックス */
	private int iLogFiles = -1;

	/** 現在解析中のログファイルのLineIterator */
	private LineIterator lineIterator = null;

	/** ログファイルの文字コード */
	private String charset = null;

	/** 現在解析中のログファイルの行番号 */
	private int iLine = 0;

	/**
	 * コンストラクタ
	 *
	 * @param logFiles ログファイルのコレクション。
	 * @param logParser ログパーサ。
	 * @param charset ログファイルの文字コード
	 */
	public LogFileReader(Collection<File> logFiles, LogParser<?> logParser, String charset) {

		if (logFiles == null) {
			throw new NullPointerException();
		}

		if (logParser == null) {
			throw new NullPointerException();
		}

		if (charset == null) {
			throw new NullPointerException();
		}

		this.logFiles = new ArrayList<File>(logFiles);
		this.logParser = logParser;
		this.charset = charset;
	}

	/**
	 * ログファイルに次の行があるかチェックする。
	 *
	 * @return 次の行があるならばtrue、なければfalseを返す。
	 * @throws IOException
	 */
	public boolean hasNext() throws IOException {

		if (this.lineIterator == null) {

			if (CollectionUtils.isEmpty(this.logFiles)) {
				return false;
			}

			this.iLogFiles = 0;
			openLogFile();
		}

		if (this.lineIterator.hasNext()) {
			return true;
		}

		if (this.iLogFiles < this.logFiles.size() - 1) {

			this.lineIterator.close();
			this.iLogFiles++;
			openLogFile();

			return hasNext();

		} else {
			return false;
		}
	}

	/**
	 * 次の行を取得する。
	 *
	 * @return 行文字列。最後まで読み込んだ後はnullを返す。
	 * @throws IOException
	 */
	public String nextLine() throws IOException {

		if (!hasNext()) {
			return null;
		}

		if (++this.iLine % 10000 == 0) {
			logger.info(this.logFiles.get(this.iLogFiles).getPath() + " : " + this.iLine + " lines had been read ...");
		}

		return this.lineIterator.nextLine();
	}

	/**
	 * ログファイルを閉じる。
	 * 使い終わったとに、明示的にこのメソッドを実行する必要がある。
	 */
	public void close() {

		if (this.lineIterator != null) {
			this.lineIterator.close();
		}
	}

	/**
	 * ソート済みのファイルリストを取得する。
	 * @return
	 */
	public List<File> getLogFiles() {
		return logFiles;
	}

	private Reader getFileReader(File file) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(new FileInputStream(file), this.charset);
	}

	private void openLogFile() throws IllegalArgumentException, UnsupportedEncodingException, FileNotFoundException {

		File logFile = this.logFiles.get(this.iLogFiles);
		this.logParser.setLogFile(logFile);
		this.lineIterator = new LineIterator(getFileReader(logFile));

		logger.info("Open log file : " + logFile.getPath());
		this.iLine = 0;
	}


}
