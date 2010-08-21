package org.codefaces.core.operations;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.core.internal.CodeFacesCoreActivator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class SCMOperationHandlerManager {
	private Map<String, Map<String, SCMOperationHandler>> kindOperationMap = new HashMap<String, Map<String, SCMOperationHandler>>();

	public SCMOperationHandlerManager() {
		readExtensions();
	}

	public void registerOperationHandler(String kind, String operationId,
			SCMOperationHandler handler) {
		Map<String, SCMOperationHandler> operationHandlerMap = kindOperationMap
				.get(kind);
		if (operationHandlerMap == null) {
			operationHandlerMap = new HashMap<String, SCMOperationHandler>();
			kindOperationMap.put(kind, operationHandlerMap);
		}

		operationHandlerMap.put(operationId, handler);
	}

	public SCMOperationHandler getOperationHandler(String kind,
			String operationId) {
		Map<String, SCMOperationHandler> operationHandlerMap = kindOperationMap
				.get(kind);

		if (operationHandlerMap != null) {
			return operationHandlerMap.get(operationId);
		}

		return null;
	}

	private void readExtensions() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.codefaces.core",
						"scmOperationHandlers");
		for (IConfigurationElement element : elements) {
			try {
				String kind = element.getAttribute("kind");
				String id = element.getAttribute("id");
				SCMOperationHandler handler = (SCMOperationHandler) element
						.createExecutableExtension("class");
				registerOperationHandler(kind, id, handler);
			} catch (CoreException e) {
				IStatus status = new Status(
						Status.ERROR,
						CodeFacesCoreActivator.PLUGIN_ID,
						"Errors occurs when loading SCMOperationHandlers extensions",
						e);
				CodeFacesCoreActivator.getDefault().getLog().log(status);
			}
		}
	}
}
