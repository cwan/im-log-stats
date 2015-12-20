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

import java.util.Date;
import java.util.List;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest;


/**
 * 時間分割統計
 *
 * @version 1.0.16
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class TimeSpanStatistics extends BasicStatistics {

	/**
	 * スパン開始日時
	 */
	private Date startDate = null;

	/**
	 * スパン終了日時
	 * @since 1.0.13
	 */
	private Date endDate = null;

	/**
	 * コンストラクタ
	 * @param transitionLogOnly 画面遷移ログのみ（リクエストログなし）かどうか
	 */
	public TimeSpanStatistics(boolean transitionLogOnly) {
		super(transitionLogOnly);
	}

	/**
	 * スパン開始日時を設定する。
	 * @param startDate スパン開始日時
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * スパン開始日時を取得する。
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * スパン終了日時を設定する。
	 * @param endDate スパン終了日時
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * スパン終了日時を取得する。
	 * @return
	 * @since 1.0.13
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 同時リクエスト数の最大値を調べる。
	 * @param concurrentRequestList
	 * @since 1.0.13
	 */
	public void countMaxConcurrentRequest(List<ConcurrentRequest> concurrentRequestList) {

		long lStartTime = this.startDate.getTime();
		long lEndTime = this.endDate.getTime();

		for (ConcurrentRequest req : concurrentRequestList) {

			if (req.getTime() < lStartTime) {
				continue;
			}

			if (req.getTime() >= lEndTime) {
				break;
			}

			updateMaxConcurrentRequest(req.getCount());
		}
	}
}
