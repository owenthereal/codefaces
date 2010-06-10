package org.codefaces.httpclient;

public class RepoResponseException extends RepoIOException {
	private static final long serialVersionUID = -7186627969477257933L;

	private final int statusCode;

	public RepoResponseException(int statusCode, final String s, Throwable cause) {
		super(s);
		initCause(cause);
		this.statusCode = statusCode;
	}

	public RepoResponseException(final String s, Throwable cause) {
		this(-1, s, cause);
	}

	public RepoResponseException(final String s) {
		this(-1, s, null);
	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
