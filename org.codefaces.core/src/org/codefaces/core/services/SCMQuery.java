package org.codefaces.core.services;

import org.codefaces.httpclient.SCMHttpClient;

public interface SCMQuery<T> {
	T execute(SCMHttpClient client, SCMQueryParameter parameter);

	public static final String PARA_URL = "URL";

	public static final String PARA_REPO = "REPO";

	public static final String PARA_REPO_RESOURCE = "REPO_RESOURCE";

	public static final String PARA_REPO_FILE_NAME = "REPO_FILE_NAME";

	public static final String PARA_REPO_FOLDER = "REPO_FOLDER";
}
