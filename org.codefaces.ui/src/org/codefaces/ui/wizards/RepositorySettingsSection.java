package org.codefaces.ui.wizards;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;

public interface RepositorySettingsSection {
	void handleConnection(IProgressMonitor monitor);

	boolean isSettingsValid();

	void createSettingsSection(RepositorySettingsPage settingsPage,
			Composite parent, RepoSettings settings);

	void setFocus();
}
