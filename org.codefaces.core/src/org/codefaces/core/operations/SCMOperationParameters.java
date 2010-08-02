package org.codefaces.core.operations;

import java.util.HashMap;
import java.util.Map;

public class SCMOperationParameters {
	private Map<SCMOperationParameter, Object> parameterMap;
	
	public static SCMOperationParameters newInstance() {
		return new SCMOperationParameters();
	}

	protected SCMOperationParameters() {
		this(new HashMap<SCMOperationParameter, Object>());
	}

	protected SCMOperationParameters(
			Map<SCMOperationParameter, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public void addParameter(SCMOperationParameter key, Object value) {
		parameterMap.put(key, value);
	}


	public Object getParameter(SCMOperationParameter key) {
		return parameterMap.get(key);
	}
}
