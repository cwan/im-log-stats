package net.mikaboshi.intra_mart.tools.log_stats.report;

/**
 * 処理時間レポート
 */
public class PageTimeStat {

	/** リクエスト回数 */
	public int count = 0;

	/** リクエスト処理時間合計 */
	public long pageTimeSum = 0L;

	/** リクエスト処理時間平均 */
	public long pageTimeAverage = 0L;

	/** リクエスト処理時間中央値 */
	public long pageTimeMedian = 0L;

	/** リクエスト処理時間最小値 */
	public long pageTimeMin = 0L;

	/** リクエスト処理時間90% Line */
	public long pageTime90Percent = 0L;

	/** リクエスト処理時間最大値 */
	public long pageTimeMax = 0L;

	/** リクエスト処理時間標準偏差 */
	public long pageTimeStandardDeviation = 0L;

	/** リクエスト回数比率 */
	public double countRate = 0.0d;

	/** リクエスト処理時間比率 */
	public double pageTimeRate = 0.0d;
}
