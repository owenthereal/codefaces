package org.codefaces.ui.internal.wizards;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.Wizard;

public class SelectRepoWizard extends Wizard{
	
	private static final String WINDOW_TITLE = "Import Repository";
	
	private SelectConnectorWizardPage selectConnectorPage; 
	private EnterRepoInfoWizardPage enterRepoInfoPage;
	private RepoSettings settings;
	
	public SelectRepoWizard(){
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(WINDOW_TITLE);
		settings = new RepoSettings();
	}
	
	
	@Override
	public void addPages() {
		selectConnectorPage = new SelectConnectorWizardPage("Select Connector", settings);
		enterRepoInfoPage = new EnterRepoInfoWizardPage("Enter Repository Infomation", settings);
		addPage(selectConnectorPage);
		addPage(enterRepoInfoPage);
	}
	
	@Override
	public boolean performFinish() {
		Assert.isNotNull(settings.get(RepoSettings.URL_REPO));
		Assert.isNotNull(settings.get(RepoSettings.REPO_BASE_DIECTORY));
		return true;
	}
	
	/**
	 * @return the settings that entered by the user
	 */
	public RepoSettings getRepoSettings(){
		return settings;
	}
}
