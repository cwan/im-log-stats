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

package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mikaboshi.intra_mart.tools.log_stats.entity.LogLayoutItemType;


/**
 * ログレイアウトの定義
 *
 * @version 1.0.18
 * @author <a href="https://github.com/cwan">cwan</a>
 */
public final class LogLayoutDefinitions {

	/** Ver.6.0, 6.1標準のリクエストログレイアウト */
	private static final String REQUEST_V6x = "{DATE}{TAB}{SEQUENCE}{TAB}{THREADID}{TAB}{SESSION}{TAB}{HOST}{TAB}{URL}{TAB}{REFERRER}{TAB}{TIME}{TAB}{ACCEPT_TIME}";

	/** Ver.7.0, 7.1標準のリクエストログレイアウト */
	private static final String REQUEST_V70_V71 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{client.session.id}\t%X{request.remote.host}\t%X{request.url}\t%X{request.url.referer}\t%X{request.page.time}\t%X{request.accept.time}\t%X{request.id}%nopex%n";

	/** Ver.7.2, 8.0.0-8.0.6標準のリクエストログレイアウト */
	private static final String REQUEST_V72_V80 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{client.session.id}\t%X{request.remote.host}\t%X{request.method}\t%X{request.url}\t%X{request.query_string}\t%X{request.url.referer}\t%X{request.page.time}\t%X{request.accept.time}\t%X{request.id}%nopex%n";

	/** Ver.8.0.7～標準のリクエストログレイアウト */
	private static final String REQUEST_V807 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{tenant.id}\t%X{log.id}\t-\t%X{client.session.id}\t%X{request.remote.host}\t%X{request.method}\t%X{request.url}\t%X{request.query_string}\t%X{request.url.referer}\t%X{request.page.time}\t%X{request.accept.time}\t%X{request.id}%nopex%n";

	/** Ver.6.0, 6.1標準の画面遷移ログレイアウト */
	private static final String TRANSITION_V6x = "{DATE}{TAB}{TYPE}{TAB}{REMOTE_ADDRESS}{TAB}{REMOTE_HOST}{TAB}{USER_ID}{TAB}{SESSION_ID}{TAB}{NEXT_PAGE}{TAB}{RESPONSE_TIME}{TAB}{EXCEPTION_NAME}{TAB}{EXCEPTION_MSG}{TAB}{PREVIOUS_PAGE}";

	/** Ver.7.0標準の画面遷移ログレイアウト */
	private static final String TRANSITION_V70 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{transition.log.type.id}\t%X{request.remote.address}\t%X{request.remote.host}\t%X{transition.access.user.id}\t%X{client.session.id}\t%X{transition.path.page.next}\t%X{transition.time.response}\t%X{transition.exception.name}\t%X{transition.exception.message}\t%X{transition.path.page.previous}\t%X{request.id}%nopex%n";

	/** Ver.7.1, 7.2, 8.0.0-8.0.6標準の画面遷移ログレイアウト */
	private static final String TRANSITION_V71_V72_V80 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{log.id}\t-\t%X{transition.log.type.id}\t%X{request.remote.address}\t%X{request.remote.host}\t%X{transition.access.user.id}\t%X{client.session.id}\t%X{transition.path.page.next}\t%X{transition.time.response}\t%X{transition.exception.name}\t%X{transition.exception.message}\t%X{transition.path.page.previous}\t%X{request.id}%nopex%n";

	/** Ver.8.0.7～標準の画面遷移ログレイアウト */
	private static final String TRANSITION_V807 = "[%d{yyyy-MM-dd HH:mm:ss.SSS}]\t[%thread]\t%X{log.report.sequence}\t%-5level\t%logger{255}\t%X{tenant.id}\t%X{log.id}\t-\t%X{transition.log.type.id}\t%X{request.remote.address}\t%X{request.remote.host}\t%X{transition.access.user.id}\t%X{client.session.id}\t%X{transition.path.page.next}\t%X{transition.time.response}\t%X{transition.exception.name}\t%X{transition.exception.message}\t%X{transition.path.page.previous}\t%X{request.id}%nopex%n";

	private static final Pattern V6_LAYOUT_PATTERN = Pattern.compile("^\\{(.+?)\\}.*");

	/**
	 * V6のログレイアウトをV7に変換する。
	 * @param v6layout
	 * @return
	 */
	public static final String convertV6toV7(String v6layout) {

		StringBuilder v7layout = new StringBuilder();

		int head = 0;

		while (head < v6layout.length()) {

			Matcher matcher = V6_LAYOUT_PATTERN.matcher(v6layout.substring(head));

			if (!matcher.matches()) {
				throw new IllegalArgumentException("Invalid Ver.6 layout : " + v6layout);
			}

			String logVariable = matcher.group(1);

			if ("TAB".equals(logVariable)) {
				v7layout.append("\t");

			} else if ("DATE".equals(logVariable)) {
				v7layout.append("[%d{yyyy-MM-dd HH:mm:ss,SSS}]");

			} else if ("SEQUENCE".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_LOG_REPORT_SEQUENCE.getLayoutItemName());

			} else if ("THREADID".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.THREAD.getLayoutItemName());

			} else if ("THREADGROUP".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_LOG_THREAD_GROUP.getLayoutItemName());

			} else if ("SESSION".equals(logVariable) || "SESSION_ID".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_CLIENT_SESSION_ID.getLayoutItemName());

			} else if ("HOST".equals(logVariable) || "REMOTE_HOST".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_REMOTE_HOST.getLayoutItemName());

			} else if ("ADDRESS".equals(logVariable) || "REMOTE_ADDRESS".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_REMOTE_ADDRESS.getLayoutItemName());

			} else if ("URL".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_URL.getLayoutItemName());

			} else if ("REFERRER".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_URL_REFERER.getLayoutItemName());

			} else if ("TIME".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_PAGE_TIME.getLayoutItemName());

			} else if ("ACCEPT_TIME".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_ACCEPT_TIME.getLayoutItemName());

			} else if ("TRANSITIONID".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_REQUEST_ID.getLayoutItemName());

			} else if ("TYPE".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_LOG_TYPE_ID.getLayoutItemName());

			} else if ("USER_ID".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_ACCESS_USER_ID.getLayoutItemName());

			} else if ("NEXT_PAGE".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_PATH_PAGE_NEXT.getLayoutItemName());

			} else if ("RESPONSE_TIME".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_TIME_RESPONSE.getLayoutItemName());

			} else if ("EXCEPTION_NAME".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_EXCEPTION_NAME.getLayoutItemName());

			} else if ("EXCEPTION_MSG".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_EXCEPTION_MESSAGE.getLayoutItemName());

			} else if ("PREVIOUS_PAGE".equals(logVariable)) {
				v7layout.append(LogLayoutItemType.MDC_TRANSITION_PATH_PAGE_PREVIOUS.getLayoutItemName());

			} else {
				throw new IllegalArgumentException("Unknown log variable : " + logVariable);
			}

			head += logVariable.length() + 2;
		}

		return v7layout.toString();
	}

	/**
	 * 標準のリクエストログレイアウトを取得する。
	 * @param version
	 * @return
	 */
	public static String getStandardRequestLogLayout(Version version) {

		switch (version) {

			case V60:
			case V61:
				return REQUEST_V6x;

			case V70:
			case V71:
				return REQUEST_V70_V71;

			case V72:
			case V800:
			case V801:
			case V802:
			case V803:
			case V804:
			case V805:
			case V806:
				return REQUEST_V72_V80;

			case V80:
			case V807:
			case V808:
			case V809:
			case V8010:
			case V8011:
			case V8012:
			case V8013:
			case V8014:
				return REQUEST_V807;

			default:
				return null;
		}
	}

	/**
	 * 標準のリクエストログレイアウトを取得する。
	 * @param version
	 * @return
	 */
	public static String getStandardTransitionLogLayout(Version version) {

		switch (version) {

			case V60:
			case V61:
				return TRANSITION_V6x;

			case V70:
				return TRANSITION_V70;

			case V71:
			case V72:
			case V800:
			case V801:
			case V802:
			case V803:
			case V804:
			case V805:
			case V806:
				return TRANSITION_V71_V72_V80;

			case V80:
			case V807:
			case V808:
			case V809:
			case V8010:
			case V8011:
			case V8012:
			case V8013:
			case V8014:
				return TRANSITION_V807;

			default:
				return null;
		}
	}
}
