package org.codefaces.core.operations;

public class SCMOperation {
	public static final String CONNECTION_OPERAION = "org.codefaces.core.operations.SCMOperation.connection";

	public static final String FETCH_BRANCHES_OPERAION = "org.codefaces.core.operations.SCMOperation.fetchBranches";

	public static final String FETCH_CHILDREN_OPERAION = "org.codefaces.core.operations.SCMOperation.fetchChildren";

	public static final String FETCH_FILE_INFO_OPERAION = "org.codefaces.core.operations.SCMOperation.fetchFileInfo";

	private final String operationId;

	private SCMOperationParameters parameter;

	private final String kind;

	protected SCMOperation(String kind, String operationId) {
		this.kind = kind;
		this.operationId = operationId;
		this.parameter = new SCMOperationParameters();
	}
	
	public static SCMOperation newInstance(String kind, String operationId) {
		return new SCMOperation(kind, operationId);
	}

	public SCMOperation addParameter(String key, Object value) {
		parameter.addParameter(key, value);
		return this;
	}
	
	public SCMOperationParameters getParameters() {
		return parameter;
	}

	public String getOperationId() {
		return operationId;
	}

	public String getKind() {
		return kind;
	}

	public <T> T execute() {
		return SCMOperationDispatcher.execute(this);
	}
}
