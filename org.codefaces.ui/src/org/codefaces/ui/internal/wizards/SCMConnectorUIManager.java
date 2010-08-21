package org.codefaces.ui.internal.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class SCMConnectorUIManager {
	private Map<String, SCMConnectorUIDescriber> connectorUIMap = new HashMap<String, SCMConnectorUIDescriber>();

	public SCMConnectorUIManager() {
		readExtensions();
	}

	private void readExtensions() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.codefaces.ui",
						"scmConnectorsUI");
		for (IConfigurationElement element : elements) {
			String kind = element.getAttribute("kind");
			String id = element.getAttribute("id");
			String description = element.getAttribute("description");
			addConnectorUI(new SCMConnectorUIDescriber(kind, id, description,
					element));
		}
	}

	private void addConnectorUI(SCMConnectorUIDescriber describer) {
		connectorUIMap.put(describer.getKind(), describer);
	}

	public SCMConnectorUIDescriber getConnectorUIDescriber(String kind) {
		return connectorUIMap.get(kind);
	}
}
