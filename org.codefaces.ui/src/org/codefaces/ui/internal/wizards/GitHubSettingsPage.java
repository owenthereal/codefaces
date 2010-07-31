package org.codefaces.ui.internal.wizards;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.Repo;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GitHubSettingsPage extends RepositorySettingsPage {
	private Text locationText;
	
	private static final String SAMPLE_URL = "http://github.com/jingweno/ruby_grep";

	private static final String DESCRIPTION = "Enter a GitHub Repository URL, e.g., "
			+ SAMPLE_URL;

	protected GitHubSettingsPage(RepoSettings settings) {
		super(settings);
	}

	private void bindLocationText() {
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String location = locationText.getText();
				getSettings().put(RepoSettings.REPO_URL, location);
				verifyPageComplete();
			}
		});

		getConnectorViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						locationText.setFocus();
					}
				});
	}

	private void createLocationText(Composite parent) {
		locationText = new Text(parent, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
	}

	protected void createSettingsSection(Composite dialogAreaComposite) {
		Label locationInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		locationInputLabel.setText("Location: ");
		createLocationText(dialogAreaComposite);
		bindLocationText();
	}

	protected void handleConnection(IProgressMonitor monitor) {
		Object typePara = getSettings().get(RepoSettings.REPO_KIND);
		Object locationPara = getSettings().get(RepoSettings.REPO_URL);
		Assert.isTrue(typePara instanceof String);
		Assert.isTrue(locationPara instanceof String);

		String type = (String) typePara;
		String location = (String) locationPara;

		monitor.beginTask("Connecting to " + type + " repository " + location,
				100);
		Repo repo = Repo.create(type, location);
		getSettings().put(RepoSettings.REPO, repo);
		monitor.done();
	}

	protected boolean isSettingsValid() {
		return !StringUtils.isEmpty((String) getSettings().get(
				RepoSettings.REPO_URL));
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}
