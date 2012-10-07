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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.mikaboshi.intra_mart.tools.log_stats.util.LongListFactory;

import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.lang.StringUtils;

/**
 * セッション情報
 *
 * @version 1.0.8
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class SessionMap {

	/**
	 * セッションID => 処理時間
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Long>> sessionPageTimesMap = LazyMap.decorate(
			new HashMap<String, List<Long>>(),
			LongListFactory.INSTANCE);

	/**
	 * セッションID　=>　ユーザID
	 */
	private Map<String, String> sessionUserIdMap = new HashMap<String, String>();

	/**
	 * セッションID => 最終アクセス時刻
	 */
	private Map<String, Date> lastAccessSessionMap = new HashMap<String, Date>();

	/**
	 * セッションID => 初回アクセス時刻
	 */
	private Map<String, Date> firstAccessSessionMap = new HashMap<String, Date>();

	/**
	 * セッションID => ログアウト時刻
	 */
	private Map<String, Date> logoutSessionMap = new HashMap<String, Date>();

	/**
	 * セッションIDからユーザIDを取得する
	 * @param sessionId
	 * @return
	 */
	public String getUserId(String sessionId) {
		return sessionUserIdMap.get(sessionId);
	}

	/**
	 * セッションIDに紐付くユーザIDを設定する。
	 * @param sessionId
	 * @param userId
	 */
	public void putUserId(String sessionId, String userId) {
		if (isValidId(sessionId) && isValidId(userId)) {
			this.sessionUserIdMap.put(sessionId, userId);
		}
	}

	/**
	 * セッションIDから、リクエスト処理時間のリストを取得する。
	 * @param sessionId
	 * @return
	 */
	public List<Long> getPageTimes(String sessionId) {
		return sessionPageTimesMap.get(sessionId);
	}

	/**
	 * 処理時間マップを取得する。
	 * @return
	 */
	public Map<String, List<Long>> getSessionPageTimesMap() {
		return sessionPageTimesMap;
	}

	/**
	 * リクエスト処理時間を追加する。
	 * @param sessionId
	 * @param pageTime
	 */
	public void addPageTime(String sessionId, long pageTime) {
		if (isValidId(sessionId)) {
			this.sessionPageTimesMap.get(sessionId).add(pageTime);
		}
	}

	/**
	 * セッションIDから、初回アクセス日時を取得する。
	 * @param sessionId
	 * @return
	 */
	public Date getFirstAccessDate(String sessionId) {
		return firstAccessSessionMap.get(sessionId);
	}

	public void putFirstAccessDate(String sessionId, Date date) {
		if (!isValidId(sessionId) || date == null) {
			return;
		}

		Date prev = this.firstAccessSessionMap.get(sessionId);

		if (prev == null || prev.after(date)) {
			this.firstAccessSessionMap.put(sessionId, date);
		}
	}

	/**
	 * セッションIDから、最終アクセス日時を取得する。
	 * @param sessionId
	 * @return
	 */
	public Date getLastAccessDate(String sessionId) {
		return lastAccessSessionMap.get(sessionId);
	}

	/**
	 * 最終アクセス日時マップを取得する。
	 * @return
	 */
	public Map<String, Date> getLastAccessSessionMap() {
		return lastAccessSessionMap;
	}

	/**
	 * 最終アクセス日時を設定する。
	 * @param sessionId
	 * @param date
	 */
	public void putLastAccessDate(String sessionId, Date date) {
		if (!isValidId(sessionId) || date == null) {
			return;
		}

		Date prev = this.lastAccessSessionMap.get(sessionId);

		if (prev == null || prev.before(date)) {
			this.lastAccessSessionMap.put(sessionId, date);
		}
	}

	/**
	 * セッションIDから、ログアウト日時を取得する。
	 * @param sessionId
	 * @return
	 */
	public Date getLogoutDate(String sessionId) {
		return logoutSessionMap.get(sessionId);
	}

	/**
	 * ログアウト日時を設定する。
	 * @param sessionId
	 * @param date
	 */
	public void putLogoutDate(String sessionId, Date date) {
		if (isValidId(sessionId)) {
			this.logoutSessionMap.put(sessionId, date);
		}
	}

	/**
	 * セッションID、ユーザIDが有効ならばtrueを返す。
	 * @param id
	 * @return
	 */
	private boolean isValidId(String id) {
		return StringUtils.isNotBlank(id) && !"-".equals(id.trim());
	}
}
