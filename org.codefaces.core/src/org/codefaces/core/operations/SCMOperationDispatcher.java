package org.codefaces.core.operations;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.connectors.SCMConnectorManager;

public class SCMOperationDispatcher {
	private static SCMConnectorManager CONNECTOR_MANAGER;

	private static SCMOperationHandlerManager OPERATION_HANDLER_MANAGER;

	public static void init(SCMConnectorManager connectorManager,
			SCMOperationHandlerManager operationHandlerManager) {
		SCMOperationDispatcher.CONNECTOR_MANAGER = connectorManager;
		SCMOperationDispatcher.OPERATION_HANDLER_MANAGER = operationHandlerManager;
	}

	@SuppressWarnings("unchecked")
	public static <T> T execute(SCMOperation query) {
		String kind = query.getKind();
		
		SCMConnector connector = CONNECTOR_MANAGER.getConnector(kind);
		if (connector == null) {
			return null;
		}

		SCMOperationHandler handler = OPERATION_HANDLER_MANAGER.getOperationHandler(
				kind, query.getOperationId());
		if (handler == null) {
			return null;
		}

		return (T) handler.execute(connector, query.getParameters());
	}
}
