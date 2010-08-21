package org.codefaces.ui.internal.wizards;

import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.wizards.RepositorySettingsSection;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SCMConnectorUIDescriber {
	private String description;

	private String id;

	private String kind;

	private final IConfigurationElement element;

	public SCMConnectorUIDescriber(String kind, String id, String description,
			IConfigurationElement element) {
		this.kind = kind;
		this.id = id;
		this.description = description;
		this.element = element;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}

	public RepositorySettingsSection createSettingsSection() {
		try {
			return (RepositorySettingsSection) element
					.createExecutableExtension("settingsSection");
		} catch (CoreException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when loading SCMConnectorsUI extensions", e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}

		return null;
	}
}
