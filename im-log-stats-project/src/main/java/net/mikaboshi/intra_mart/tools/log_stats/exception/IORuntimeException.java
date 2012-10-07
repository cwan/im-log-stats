package net.mikaboshi.intra_mart.tools.log_stats.exception;

/**
 * I/O実行時例外クラス。
 */
public class IORuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6400036493067474065L;

	public IORuntimeException() {
	}

	public IORuntimeException(String message) {
		super(message);
	}

	public IORuntimeException(Throwable cause) {
		super(cause);
	}

	public IORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
