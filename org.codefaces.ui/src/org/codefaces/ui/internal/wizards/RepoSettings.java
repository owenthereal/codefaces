package org.codefaces.ui.internal.wizards;

import java.util.HashMap;
import java.util.Map;

public class RepoSettings {
	
	public static final String REPO_TYPE = "REPO_TYPE";
	
	public static final String URL_REPO = "REPO";
	
	public static final String REPO_BASE_DIECTORY = "REPO_ROOT";
	
	public static final String REPO_USER = "REPO_USER";
	
	public static final String REPO_PASSWORD = "REPO_PASSWORD";
	
	private Map<String, Object> settings;
	
	/**
	 * Constructor
	 */
	public RepoSettings(){
		settings = new HashMap<String, Object>();
	}
	
	public Object get(String key){
		return settings.get(key);
	}
	
	public void put(String key, Object value){
		settings.put(key, value);
	}
	
	
}
