package org.codefaces.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RepoParameters {
	private Map<String, String> parameters = new HashMap<String, String>();

	public static final String REPO = "repo";

	public static final String BRANCH = "branch";

	public void addParameter(String key, String parameter) {
		parameters.put(key, parameter);
	}

	public Map<String, String> getParametersMap() {
		return Collections.unmodifiableMap(parameters);
	}

	public String getParameter(String key) {
		return parameters.get(key);
	}
}
