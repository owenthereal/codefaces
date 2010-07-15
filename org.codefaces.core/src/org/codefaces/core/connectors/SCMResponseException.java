package org.codefaces.core.connectors;

public class SCMResponseException extends SCMIOException {
	private static final long serialVersionUID = -7186627969477257933L;

	private final int statusCode;

	public SCMResponseException(int statusCode, final String s, Throwable cause) {
		super(s);
		initCause(cause);
		this.statusCode = statusCode;
	}

	public SCMResponseException(final String s, Throwable cause) {
		this(-1, s, cause);
	}

	public SCMResponseException(final String s) {
		this(-1, s, null);
	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
