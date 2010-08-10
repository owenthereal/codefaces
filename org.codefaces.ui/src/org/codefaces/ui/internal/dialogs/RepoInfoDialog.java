package org.codefaces.ui.internal.dialogs;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RepoInfoDialog extends TitleAreaDialog {

	private static final String TITLE = "Repository";
	private static final String DESCRIPTION = "Repository Information";
	private static final String WINDOW_TITLE = "Properties";
	private final RepoProject project;

	public RepoInfoDialog(Shell parentShell, RepoProject project) {
		super(parentShell);
		this.project = project;
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | getDefaultOrientation());
	}

	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite dialogAreaComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dialogAreaComposite.setFont(composite.getFont());

		createRepoInfoSection(dialogAreaComposite, project);

		setTitle(TITLE);
		setMessage(DESCRIPTION);
		setWindowTitle(WINDOW_TITLE);
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.get().OK_LABEL, true);
	}

	private void createRepoInfoSection(Composite parent, RepoProject project) {
		Label lblRepoURL = new Label(parent, SWT.NONE);
		lblRepoURL.setText("Location: ");
		Text repoURLText = new Text(parent, SWT.SEARCH);
		repoURLText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		Repo repo = project.getRoot().getRepo();
		repoURLText.setText(repo.getUrl());
		repoURLText.setEditable(false);

		String username = repo.getCredential().getUser();
		Label lblRepoUser = new Label(parent, SWT.NONE);
		lblRepoUser.setText("User name:");
		Text repoUserText = new Text(parent, SWT.NONE);
		repoUserText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		repoUserText.setText(username == null ? "" : username);
		repoUserText.setEditable(false);
	}

	private void setWindowTitle(String windowTitle) {
		if (getShell() == null) {
			return;
		}
		getShell().setText(windowTitle);
	}

}
