package net.mikaboshi.intra_mart.tools.log_stats.util;

import java.util.List;

public final class MathUtil {

	private MathUtil() {}

	/**
	 * Longリストの要素の合計を取得する。
	 * @param longList
	 * @return
	 */
	public static long getSum(List<Long> longList) {

		if (longList == null) {
			return 0L;
		}

		long sum = 0L;

		for (long l : longList) {
			sum += l;
		}

		return sum;
	}

	/**
	 * Longリストの要素の平均を取得する。
	 * @param longList
	 * @param sum 合計
	 * @param devByZero リストの要素が0の場合の戻り値
	 * @return
	 */
	public static long getAverage(List<Long> longList, long sum, long devByZero) {

		if (longList == null || longList.isEmpty()) {
			return devByZero;
		}

		return Math.round((double) sum / (double) longList.size());
	}

	/**
	 * Longリストの要素の標準偏差を取得する。
	 * @param longList
	 * @param average
	 * @param devByZero リストの要素が0の場合の戻り値
	 * @return
	 */
	public static long getStandardDeviation(List<Long> longList, long average, long devByZero) {

		if (longList == null || longList.isEmpty()) {
			return devByZero;
		}

		double sd = 0.0d;

		for (long l: longList) {
			sd += Math.pow(l - average, 2);
		}

		sd = Math.sqrt(sd / (double) longList.size());

		return Math.round(sd);
	}

	/**
	 * 比率を計算する
	 * @param numerator 分子
	 * @param denominator 分母
	 * @param devByZeroValue denominatorが0の場合の戻り値
	 * @return
	 */
	public static double getRate(long numerator, long denominator, double devByZeroValue) {

		if (denominator == 0) {
			return devByZeroValue;
		}

		return (double) numerator / (double) denominator;
	}
}
