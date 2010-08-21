package org.codefaces.core.github.internal.connectors;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.internal.ajax.AjaxClientAdapter;

public class GitHubConnector implements SCMConnector {
	private final SCMHttpClient client;

	private static final String KIND = "GitHub";

	public GitHubConnector(SCMHttpClient client) {
		this.client = client;
	}

	public GitHubConnector() {
		this(new AjaxClientAdapter());
	}

	public String getResponseBody(String url) {
		return client.getResponseBody(url);
	}

	@Override
	public String getKind() {
		return KIND;
	}
}
