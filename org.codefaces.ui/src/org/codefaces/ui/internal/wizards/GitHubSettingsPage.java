package org.codefaces.ui.internal.wizards;

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

	protected GitHubSettingsPage(RepoSettings settings) {
		super(settings);
	}

	protected void bindLocationText() {
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

	protected void createInputSection(Composite dialogAreaComposite) {
		Label locationInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		locationInputLabel.setText("Location: ");
		createLocationText(dialogAreaComposite);
		bindLocationText();
	}

	protected void createLocationText(Composite parent) {
		locationText = new Text(parent, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
	}
}
