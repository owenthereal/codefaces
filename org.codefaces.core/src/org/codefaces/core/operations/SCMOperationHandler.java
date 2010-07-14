package org.codefaces.core.operations;

import org.codefaces.core.connectors.SCMConnector;

public interface SCMOperationHandler {
	Object execute(SCMConnector connector, SCMOperationParameters parameter);

	public static final String PARA_URL = "URL";

	public static final String PARA_REPO = "REPO";

	public static final String PARA_REPO_RESOURCE = "REPO_RESOURCE";

	public static final String PARA_REPO_FILE_NAME = "REPO_FILE_NAME";

	public static final String PARA_REPO_FOLDER = "REPO_FOLDER";
}
