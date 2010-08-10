package org.codefaces.ui.wizards;

import java.util.HashMap;
import java.util.Map;

public class RepoSettings {
	public static final String REPO_KIND = "REPO_KIND";

	public static final String REPO_RESOURCE_INPUT = "REPO_RESOURCE_INPUT";

	public static final String REPO_USER = "REPO_USER";

	public static final String REPO_PASSWORD = "REPO_PASSWORD";

	public static final String REPO = "REPO";
	
	public static final String REPO_URL = "REPO_URL";

	private Map<String, Object> settings;

	public RepoSettings() {
		settings = new HashMap<String, Object>();
	}

	public Object get(String key) {
		return settings.get(key);
	}

	public void put(String key, Object value) {
		settings.put(key, value);
	}
}
