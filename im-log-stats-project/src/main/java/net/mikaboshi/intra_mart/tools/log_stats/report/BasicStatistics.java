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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest;
import net.mikaboshi.intra_mart.tools.log_stats.entity.ExceptionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.RequestLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionLog;
import net.mikaboshi.intra_mart.tools.log_stats.entity.TransitionType;

/**
 * 統計情報基底クラス
 *
 * @version 1.0.16
 * @since 1.0.16
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public abstract class BasicStatistics {

	/**
	 * リクエスト数合計
	 */
	private int requestCount = 0;

	/**
	 * リクエスト処理時間のリスト
	 */
	private List<Long> requestPageTimes = new ArrayList<Long>();

	/**
	 * ユニークなセッションID
	 */
	private Set<String> uniqueSessionIds = new HashSet<String>();

	/**
	 * 画面遷移数合計
	 */
	private int transitionCount = 0;

	/**
	 * ユニークなユーザID
	 */
	private Set<String> uniqueUserIds = new HashSet<String>();

	/**
	 * 画面遷移例外数
	 */
	private int transitionExceptionCount = 0;

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）
	 */
	private int activeSessionCount = 0;

	/**
	 * 例外数
	 */
	private int exceptionCount = 0;

	/**
	 * 画面遷移ログのみ（リクエストログなし）ならばtrue
	 */
	private final boolean transitionLogOnly;

	/**
	 * 最大同時リクエスト数
	 */
	private int maxConcurrentRequest = 0;

	/**
	 * コンストラクタ
	 * @param transitionLogOnly 画面遷移ログのみ（リクエストログなし）かどうか
	 */
	public BasicStatistics(boolean transitionLogOnly) {
		this.transitionLogOnly = transitionLogOnly;
	}

	/**
	 * リクエストログを追加する
	 * @param log
	 */
	public void add(RequestLog log) {

		if (log == null) {
			return;
		}

		this.requestCount++;
		this.requestPageTimes.add(log.requestPageTime);
		this.uniqueSessionIds.add(log.clientSessionId);
	}

	/**
	 * 画面遷移ログを追加する
	 * @param log
	 */
	public void add(TransitionLog log) {

		if (log == null) {
			return;
		}

		this.transitionCount++;
		this.uniqueSessionIds.add(log.clientSessionId);
		this.uniqueUserIds.add(log.transitionAccessUserId);

		if (this.transitionLogOnly && log.type == TransitionType.REQUEST) {
			this.requestCount++;
			this.requestPageTimes.add(log.transitionTimeResponse);
			this.uniqueSessionIds.add(log.clientSessionId);
		}

		if (!"-".equals(log.transitionExceptionName)) {
			this.transitionExceptionCount++;
		}
	}

	/**
	 * 例外ログを追加する
	 * @param log
	 */
	public void add(ExceptionLog log) {

		if (log == null) {
			return;
		}

		this.exceptionCount++;
	}

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）を設定する
	 * @param activeSessionCount
	 */
	public void setActiveSessionCount(int activeSessionCount) {
		this.activeSessionCount = activeSessionCount;
	}

	/**
	 * 有効なセッション数（このスパンに登場しないセッションも含む）を取得する
	 * @return
	 */
	public int getActiveSessionCount() {
		return activeSessionCount;
	}

	/**
	 * リクエスト数を取得する。
	 * @return
	 */
	public int getRequestCount() {
		return requestCount;
	}

	/**
	 * 画面遷移数を取得する。
	 * @return
	 */
	public int getTransitionCount() {
		return transitionCount;
	}

	/**
	 * ユニークセッション数を取得する。
	 * @return
	 */
	public int getUniqueSessionCount() {
		return this.uniqueSessionIds.size();
	}

	/**
	 * ユニークユーザ数を取得する。
	 * @return
	 */
	public int getUniqueUserCount() {
		return this.uniqueUserIds.size();
	}

	/**
	 * リクエスト処理時間のリストを取得する。
	 */
	public List<Long> getRequestPageTimes() {
		return requestPageTimes;
	}

	/**
	 * 例外数合計を取得する。
	 * @return
	 */
	public int getExceptionCount() {
		return exceptionCount;
	}

	/**
	 * 画面遷移例外数合計を取得する。
	 * @return
	 */
	public int getTransitionExceptionCount() {
		return transitionExceptionCount;
	}

	public abstract void countMaxConcurrentRequest(List<ConcurrentRequest> concurrentRequestList);

	/**
	 * 同時リクエスト数の最大値を取得する。
	 * @return
	 */
	public int getMaxConcurrentRequest() {
		return maxConcurrentRequest;
	}

	/**
	 * 同時リクエスト数の最大値を更新する。
	 * @param count
	 */
	protected void updateMaxConcurrentRequest(int count) {
		if (count > this.maxConcurrentRequest) {
			this.maxConcurrentRequest = count;
		}
	}

}
