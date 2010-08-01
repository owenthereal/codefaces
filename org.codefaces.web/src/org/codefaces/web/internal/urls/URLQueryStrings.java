package org.codefaces.web.internal.urls;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class URLQueryStrings {
	private Map<String, String> parameters = new HashMap<String, String>();

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
