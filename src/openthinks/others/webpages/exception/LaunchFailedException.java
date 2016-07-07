package openthinks.others.webpages.exception;

public class LaunchFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4476290985455752080L;

	public LaunchFailedException() {
	}

	public LaunchFailedException(String message) {
		super(message);
	}

	public LaunchFailedException(Throwable cause) {
		super(cause);
	}

	public LaunchFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LaunchFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
