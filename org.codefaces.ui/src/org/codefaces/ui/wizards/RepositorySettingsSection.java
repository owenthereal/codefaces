package org.codefaces.ui.wizards;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.Repo;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RepositorySettingsSection {
	protected Text locationText;

	protected RepoSettings settings;

	protected RepositorySettingsPage settingsPage;

	private void bindLocationText() {
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				settings.put(RepoSettings.REPO_URL, locationText.getText());
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

		monitor.beginTask("Connecting to " + type + " repository "
				+ locationText.getText(), 100);
		Repo repo = Repo.create(type, locationText.getText(), null, null);
		getSettings().put(RepoSettings.REPO, repo);
		monitor.done();
	}

	public boolean isSettingsValid() {
		return !StringUtils.isEmpty(locationText.getText());
	}

	public void createSettingsSection(RepositorySettingsPage settingsPage,
			Composite parentComposite, RepoSettings settings) {
		this.settingsPage = settingsPage;
		this.settings = settings;
		Label locationInputLabel = new Label(parentComposite, SWT.NONE);
		locationInputLabel.setText("Location: ");
		createLocationText(parentComposite);
		bindLocationText();

		createAdditionalControls(parentComposite);
	}

	protected void createAdditionalControls(Composite parentComposite) {
		// do nothing
	}

	protected RepoSettings getSettings() {
		return settings;
	}

	public void setFocus() {
		locationText.setFocus();
	}
}
