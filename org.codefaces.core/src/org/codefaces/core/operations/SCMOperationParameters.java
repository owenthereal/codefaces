package org.codefaces.core.operations;

import java.util.HashMap;
import java.util.Map;

public class SCMOperationParameters {
	private Map<String, Object> parameterMap;
	
	public static SCMOperationParameters newInstance() {
		return new SCMOperationParameters();
	}

	protected SCMOperationParameters() {
		this(new HashMap<String, Object>());
	}

	protected SCMOperationParameters(
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
