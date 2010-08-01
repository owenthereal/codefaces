package org.codefaces.ui.internal.wizards.github;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.Repo;
import org.codefaces.ui.wizards.RepoSettings;
import org.codefaces.ui.wizards.RepositorySettingsPage;
import org.codefaces.ui.wizards.RepositorySettingsSection;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GitHubSettingsSection implements RepositorySettingsSection {
	private Text locationText;

	private RepoSettings settings;

	private RepositorySettingsPage settingsPage;

	private String location;

	private void bindLocationText() {
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String location = locationText.getText();
				GitHubSettingsSection.this.location = location;
				settingsPage.verifyPageComplete();
			}
		});
	}

	private void createLocationText(Composite parent) {
		locationText = new Text(parent, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
	}

	public void handleConnection(IProgressMonitor monitor) {
		Object typePara = getSettings().get(RepoSettings.REPO_KIND);
		Assert.isTrue(typePara instanceof String);

		String type = (String) typePara;

		monitor.beginTask("Connecting to " + type + " repository " + location,
				100);
		Repo repo = Repo.create(type, location);
		getSettings().put(RepoSettings.REPO, repo);
		monitor.done();
	}

	public boolean isSettingsValid() {
		return !StringUtils.isEmpty(location);
	}

	@Override
	public void createSettingsSection(RepositorySettingsPage settingsPage,
			Composite parentComposite, RepoSettings settings) {
		this.settingsPage = settingsPage;
		this.settings = settings;
		Label locationInputLabel = new Label(parentComposite, SWT.NONE);
		locationInputLabel.setText("Location: ");
		createLocationText(parentComposite);
		bindLocationText();
	}

	private RepoSettings getSettings() {
		return settings;
	}

	@Override
	public void setFocus() {
		locationText.setFocus();
	}
}
