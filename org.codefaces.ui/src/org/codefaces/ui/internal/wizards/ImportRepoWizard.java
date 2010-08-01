package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.views.ProjectExplorerViewPart;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

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

		RepoFolder baseDirectory = (RepoFolder) getRepoSettings().get(
				RepoSettings.REPO_RESOURCE_INPUT);

		if (baseDirectory != null) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(ProjectExplorerViewPart.ID);
				Workspace.getCurrent().update(baseDirectory);
			} catch (PartInitException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when openning view "
								+ ProjectExplorerViewPart.ID, e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}
		}

		return true;
	}

	public RepoSettings getRepoSettings() {
		return settings;
	}
}
