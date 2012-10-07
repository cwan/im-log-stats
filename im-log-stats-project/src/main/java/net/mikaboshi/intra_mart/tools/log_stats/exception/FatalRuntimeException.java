package net.mikaboshi.intra_mart.tools.log_stats.exception;

public class FatalRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7368239629813870028L;

	public FatalRuntimeException() {
	}

	public FatalRuntimeException(String message) {
		super(message);
	}

	public FatalRuntimeException(Throwable cause) {
		super(cause);
	}

	public FatalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
