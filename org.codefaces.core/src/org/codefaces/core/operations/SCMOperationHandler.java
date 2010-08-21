package org.codefaces.core.operations;

import org.codefaces.core.connectors.SCMConnector;

public interface SCMOperationHandler {
	Object execute(SCMConnector connector, SCMOperationParameters parameter);
}
