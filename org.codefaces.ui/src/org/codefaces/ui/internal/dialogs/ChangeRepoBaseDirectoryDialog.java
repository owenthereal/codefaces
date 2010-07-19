package org.codefaces.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ChangeRepoBaseDirectoryDialog extends TitleAreaDialog{

	private static final String TITLE = "Select base directory:";
	private static final String DESCRIPTION = "Select a directory as the base directory.";
	private static final String WINDOW_TITLE = "Select Base Directory";
	
	private TreeViewer viewer;

	public ChangeRepoBaseDirectoryDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | getDefaultOrientation());
	}

	@Override
	protected Composite createDialogArea(Composite parent){
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

		viewer = new TreeViewer(dialogAreaComposite, SWT.NONE);
		viewer.setInput(null);
		
		setTitle(TITLE);
		setMessage(DESCRIPTION);
		setWindowTitle(WINDOW_TITLE);
		
		return composite;
	}
	
	/**
	 * Set the shell window title of this dialog
	 * @param windowTitle the window title
	 */
	private void setWindowTitle(String windowTitle) {
		if (getShell() == null) {
			return;
		}
		getShell().setText(windowTitle);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
}
