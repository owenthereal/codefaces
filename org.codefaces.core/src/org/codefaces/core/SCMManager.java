package org.codefaces.core;

import org.codefaces.core.connectors.SCMConnectorManager;
import org.codefaces.core.internal.CodeFacesCoreActivator;
import org.codefaces.core.operations.SCMOperationHandlerManager;

public class SCMManager {
	private final SCMConnectorManager connectorManager;

	private final SCMOperationHandlerManager operationHandlerManager;

	public SCMConnectorManager getConnectorManager() {
		return connectorManager;
	}

	public SCMOperationHandlerManager getOperationHandlerManager() {
		return operationHandlerManager;
	}

	public SCMManager(SCMConnectorManager connectorManager,
			SCMOperationHandlerManager operationHandlerManager) {
		this.connectorManager = connectorManager;
		this.operationHandlerManager = operationHandlerManager;
	}
	
	public static SCMManager getInstance() {
		return CodeFacesCoreActivator.getDefault().getSCMManager();
	}
}
