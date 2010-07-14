package org.codefaces.core.services;

import org.codefaces.httpclient.SCMHttpClient;

public interface SCMQuery<T> {
	T execute(SCMHttpClient client, SCMQueryParameter parameter);

	public static final String PARA_URL = "URL";

	public static final String PARA_REPO = "REPO";

	public static final String PARA_REPO_RESOURCE = "REPO_RESOURCE";

	public static final String PARA_REPO_FOLDER = "REPO_FOLDER";
	
	public static final String PARA_REPO_FILE = "REPO_FILE";
	
	public static final String PARA_USERNAME = "REPO_USER_NAME";
	
	public static final String PARA_PASSWORD = "REPO_PASSWORD";
}
