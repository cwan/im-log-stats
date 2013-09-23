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

package net.mikaboshi.intra_mart.tools.log_stats.report;

/**
 * 処理時間レポート
 *
 * @version 1.0.15
 * @author <a href="https://github.com/cwan">cwan</a>
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
	public long pageTimeP90 = 0L;

	/** リクエスト処理時間最大値 */
	public long pageTimeMax = 0L;

	/** リクエスト処理時間標準偏差 */
	public long pageTimeStandardDeviation = 0L;

	/** リクエスト回数比率 */
	public double countRate = 0.0d;

	/** リクエスト処理時間比率 */
	public double pageTimeRate = 0.0d;
}
