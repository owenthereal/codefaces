package org.codefaces.core.connectors;


public class SCMIOException extends RuntimeException {
	private static final long serialVersionUID = -4979080649956075092L;

	public SCMIOException() {
		super();
	}

	public SCMIOException(String s) {
		super(s);
	}

	public SCMIOException(Throwable cause) {
		initCause(cause);
	}

	public SCMIOException(String message, Throwable cause) {
		super(message);
		initCause(cause);
	}
}
