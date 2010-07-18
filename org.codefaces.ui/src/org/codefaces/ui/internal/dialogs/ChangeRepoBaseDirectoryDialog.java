package org.codefaces.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ChangeRepoBaseDirectoryDialog extends TitleAreaDialog{

	private static final String DIALOG_LABEL_TEXT = "Select new base directory:";

	public ChangeRepoBaseDirectoryDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Composite createDialogArea(Composite parent){
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite dialogAreaComposite = new Composite(composite, SWT.NONE);		
		dialogAreaComposite.setLayout(new GridLayout(2, false));

		Label dialogLabel = new Label(dialogAreaComposite, SWT.NONE);
		GridData diallogLabelGridData = new GridData();
		diallogLabelGridData.horizontalSpan = 2;
		dialogLabel.setData(diallogLabelGridData);
		dialogLabel.setText(DIALOG_LABEL_TEXT);
		
		return composite;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
}
