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

package net.mikaboshi.intra_mart.tools.log_stats.entity;

/**
 * 同時リクエスト集計用
 *
 * @version 1.0.16
 * @since 1.0.13
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class ConcurrentRequest {

	public enum EventType {

		/** 受信時刻 */
		ACCEPT_TIME,

		/** 返信時刻 */
		RESPONSE_TIME;
	}

	private final EventType type;

	private final long time;

	private int count = 0;

	private String tenantId;

	public ConcurrentRequest(EventType type, long time, String tenantId) {
		this.type = type;
		this.time = time;
		this.tenantId = tenantId;
	}

	public EventType getType() {
		return type;
	}

	public long getTime() {
		return time;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	/**
	 * テナントIDを取得する。
	 * @since 1.0.16
	 * @return
	 */
	public String getTenantId() {
		return tenantId;
	}
}
