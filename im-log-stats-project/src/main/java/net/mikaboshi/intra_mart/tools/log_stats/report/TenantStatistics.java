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

import java.util.List;

import net.mikaboshi.intra_mart.tools.log_stats.entity.ConcurrentRequest;


/**
 * テナント別統計
 *
 * @version 1.0.16
 * @since 1.0.16
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public class TenantStatistics extends BasicStatistics {

	/**
	 * テナントID
	 */
	private String tenantId;

	/**
	 * コンストラクタ
	 * @param transitionLogOnly 画面遷移ログのみ（リクエストログなし）かどうか
	 */
	public TenantStatistics(boolean transitionLogOnly) {
		super(transitionLogOnly);
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 同時リクエスト数の最大値を調べる。
	 * @param concurrentRequestList
	 */
	public void countMaxConcurrentRequest(List<ConcurrentRequest> concurrentRequestList) {

		if (this.tenantId == null) {
			return;
		}

		for (ConcurrentRequest req : concurrentRequestList) {

			if (!this.tenantId.equals(req.getTenantId())) {
				continue;
			}

			updateMaxConcurrentRequest(req.getCount());
		}
	}
}
