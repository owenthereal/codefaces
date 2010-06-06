package org.codefaces.httpclient.http;

import java.io.IOException;

public class RepoIOException extends IOException {
	private static final long serialVersionUID = -4979080649956075092L;

	public RepoIOException() {
		super();
	}

	public RepoIOException(String s) {
		super(s);
	}

	public RepoIOException(Throwable cause) {
		initCause(cause);
	}

	public RepoIOException(String message, Throwable cause) {
		super(message);
		initCause(cause);
	}
}
