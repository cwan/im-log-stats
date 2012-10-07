package net.mikaboshi.intra_mart.tools.log_stats.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ログレイアウト項目種別
 */
public enum LogLayoutItemType {

	/** ロガー名 */
	LOGGER_NAME(false, "c", "lo", "logger"),

	/** 出力日時 */
	DATE(false, "d", "date"),

	/** ログメッセージ */
	MESSAGE(false, "m", "msg", "message"),

	/** ログレベル  */
	LEVEL(false, "p", "le", "level"),

	/** スレッド名 */
	THREAD(false, "t", "thread"),

	/** MDC */
	MDC(false, "X", "mdc"),

	/** 例外出力の抑止 */
	NOPEX(false, "nopex", "nopexception"),

	/** 改行 */
	NEW_LINE(false, "n"),

	MDC_CLIENT_SESSION_ID(true, "client.session.id"),

	MDC_REQUEST_REMOTE_HOST(true, "request.remote.host"),

	MDC_SAPCONNECTOR_ID_TIME(true, "sapconnector.id.time"),

	MDC_DB_CONNECTION_IDENTIFIER(true, "db.connection.identifier"),

	MDC_REQUEST_REMOTE_ADDRESS(true, "request.remote.address"),

	MDC_SECURITY_ID_SESSION(true, "security.id.session"),

	MDC_DB_CONNECTION_RES(true, "db.connection.res"),

	MDC_REQUEST_URL(true, "request.url"),

	MDC_SECURITY_ID_ACCOUNT(true, "security.id.account"),

	MDC_DB_SQL(true, "db.sql"),

	MDC_REQUEST_URL_REFERER(true, "request.url.referer"),

	MDC_SECURITY_ID_GROUP(true, "security.id.group"),

	MDC_DB_TIME(true, "db.time"),

	MDC_REQUEST_PAGE_TIME(true, "request.page.time"),

	MDC_SECURITY_ID_LOGINTYPE(true, "security.id.logintype"),

	MDC_LOG_ID(true, "log.id"),

	MDC_REQUEST_ACCEPT_TIME(true, "request.accept.time"),

	MDC_TRANSITION_LOG_TYPE_ID(true, "transition.log.type.id"),

	MDC_LOG_REPORT_SEQUENCE(true, "log.report.sequence"),

	MDC_SAPCONNECTOR_ID_IDENTIFIER(true, "sapconnector.id.identifier"),

	MDC_TRANSITION_ACCESS_USER_ID(true, "transition.access.user.id"),

	MDC_LOG_THREAD_GROUP(true, "log.thread.group"),

	MDC_SAPCONNECTOR_IM_ID_ACCOUNT(true, "sapconnector.im.id.account"),

	MDC_TRANSITION_PATH_PAGE_NEXT(true, "transition.path.page.next"),

	MDC_MEMORY_FREE(true, "memory.free"),

	MDC_SAPCONNECTOR_IM_ID_GROUP(true, "sapconnector.im.id.group"),

	MDC_TRANSITION_TIME_RESPONSE(true, "transition.time.response"),

	MDC_MEMORY_TOTAL(true, "memory.total"),

	MDC_SAPCONNECTOR_SAP_ID_HOST(true, "sapconnector.sap.id.host"),

	MDC_TRANSITION_EXCEPTION_NAME(true, "transition.exception.name"),

	MDC_MEMORY_INITIAL(true, "memory.initial"),

	MDC_SAPCONNECTOR_SAP_ID_CLIENT(true, "sapconnector.sap.id.client"),

	MDC_TRANSITION_EXCEPTION_MESSAGE(true, "transition.exception.message"),

	MDC_MEMORY_MAX(true, "memory.max"),

	MDC_SAPCONNECTOR_SAP_ID_USER(true, "sapconnector.sap.id.user"),

	MDC_TRANSITION_PATH_PAGE_PREVIOUS(true, "transition.path.page.previous"),

	MDC_REQUEST_ID(true, "request.id"),

	MDC_SAPCONNECTOR_ID_BAPINAME(true, "sapconnector.id.bapiname"),

	MDC_REQUEST_METHOD(true, "request.method"),

	MDC_REQUEST_QUERY_STRING(true, "request.query_string");


	private static Map<String, LogLayoutItemType> nonMdcMap = null;

	private static Map<String, LogLayoutItemType> mdcMap = null;

	private final boolean isMdc;

	private final String[] names;

	private LogLayoutItemType(boolean isMdc, String ... names) {

		this.isMdc = isMdc;

		if (names == null || names.length == 0) {
			throw new IllegalArgumentException();
		}

		this.names = names;
	}

	/**
	 * ログレイアウトパターン名に該当するLogLayoutItemTypeを取得する。
	 * 不明な場合はnullを返す。
	 * @param name ログレイアウトパターン名
	 * @param mdcName MDC名
	 * @return
	 */
	public static LogLayoutItemType toEnum(String name, String mdcName) {

		synchronized (LogLayoutItemType.class) {

			if (nonMdcMap == null) {
				// 初回使用時にマップを作成する

				nonMdcMap = new ConcurrentHashMap<String, LogLayoutItemType>();
				mdcMap = new ConcurrentHashMap<String, LogLayoutItemType>();

				for (LogLayoutItemType type : LogLayoutItemType.values()) {

					for (String s : type.names) {

						if (type.isMdc) {
							mdcMap.put(s, type);
						} else {
							nonMdcMap.put(s, type);
						}

					}
				}
			}
		}

		LogLayoutItemType type = nonMdcMap.get(name);

		if (type == null) {

			return null;

		} else if (type == MDC) {

			return mdcMap.get(mdcName);

		} else {

			return type;
		}
	}

	public boolean isMdc() {
		return isMdc;
	}

	public String getLayoutItemName() {

		StringBuilder sb = new StringBuilder("%");

		if (this.isMdc) {

			sb.append(MDC.names[0]);
			sb.append("{");
			sb.append(this.names[0]);
			sb.append("}");

		} else {

			sb.append(this.names[0]);
		}

		return sb.toString();
	}
}
