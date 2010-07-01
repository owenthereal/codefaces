package org.codefaces.core.services;

import java.util.HashMap;
import java.util.Map;

public class SCMQueryParameter {
	private Map<String, Object> parameterMap;
	
	public static SCMQueryParameter newInstance() {
		return new SCMQueryParameter();
	}

	protected SCMQueryParameter() {
		this(new HashMap<String, Object>());
	}

	protected SCMQueryParameter(
			Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public void addParameter(String key, Object value) {
		parameterMap.put(key, value);
	}


	public Object getParameter(String key) {
		return parameterMap.get(key);
	}
}
