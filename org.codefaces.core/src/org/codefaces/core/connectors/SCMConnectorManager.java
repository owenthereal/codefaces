package org.codefaces.core.connectors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codefaces.core.internal.CodeFacesCoreActivator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class SCMConnectorManager {
	private Map<String, SCMConnectorDescriber> connectorMap = new HashMap<String, SCMConnectorDescriber>();

	public SCMConnectorManager() {
		readExtensions();
	}

	public void addConnector(SCMConnectorDescriber describer) {
		connectorMap.put(describer.getKind(), describer);
	}

	public SCMConnector getConnector(String kind) {
		SCMConnectorDescriber describer = connectorMap.get(kind);
		if (describer != null) {
			return describer.getConnector();
		}

		return null;
	}

	public Collection<SCMConnectorDescriber> getConnectorDescribers() {
		return Collections.unmodifiableCollection(connectorMap.values());
	}

	private void readExtensions() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.codefaces.core",
						"scmConnectors");
		for (IConfigurationElement element : elements) {
			try {
				String id = element.getAttribute("id");
				String kind = element.getAttribute("kind");
				SCMConnector connector = (SCMConnector) element
						.createExecutableExtension("class");
				addConnector(new SCMConnectorDescriber(id, kind, connector));
			} catch (CoreException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesCoreActivator.PLUGIN_ID,
						"Errors occurs when loading SCMConnectors extensions",
						e);
				CodeFacesCoreActivator.getDefault().getLog().log(status);
			}
		}
	}
}
