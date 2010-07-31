package org.codefaces.ui.internal.wizards;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.Wizard;

public class ImportRepoWizard extends Wizard {
	private static final String WINDOW_TITLE = "Import Repository";

	private RepoSettings settings;

	public ImportRepoWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(WINDOW_TITLE);
		settings = new RepoSettings();
	}

	@Override
	public void addPages() {
		addPage(new GitHubSettingsPage(settings));
		addPage(new RepositoryResourcePage(settings));
	}

	@Override
	public boolean performFinish() {
		Assert.isNotNull(settings.get(RepoSettings.REPO_URL));
		Assert.isNotNull(settings.get(RepoSettings.REPO_RESOURCE_INPUT));
		return true;
	}

	public RepoSettings getRepoSettings() {
		return settings;
	}
}
