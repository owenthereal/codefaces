package org.codefaces.ui.dialogs;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.services.RepoService;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.Images;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;

public class RepoUrlInputDialog extends TitleAreaDialog {
	private final class BranchLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			RepoBranch branch = (RepoBranch) element;
			return branch.getName();
		}
	}

	private final class BranchSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (event.getSelection().isEmpty()) {
				selectedBranch = null;
				setErrorMessage(NO_BRANCH_IS_SELECTED);
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			} else {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				selectedBranch = (RepoBranch) selection.getFirstElement();
				setErrorMessage(null);
				getButton(IDialogConstants.OK_ID).setEnabled(true);
			}
		}
	}

	private static final String WINDOW_TITLE = "Import from a repository";

	private static final String NO_BRANCH_IS_SELECTED = "No branch is selected.";

	private Text urlInputViewer;

	private ComboViewer branchInputViewer;

	public static final String TITLE = "Checkout projects from a repository";

	private static final String SAMPLE_URL = "http://github.com/jingweno/ruby_grep";

	public static final String DESCRIPTION = "Enter a GitHub Repository URL, e.g., "
			+ SAMPLE_URL;

	private Button connectButton;

	private RepoBranch selectedBranch;

	public RepoUrlInputDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | getDefaultOrientation());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
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

		Label urlInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		urlInputLabel.setText("Repository:");
		createUrlInputSection(dialogAreaComposite);

		Label branchInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		branchInputLabel.setText("Branch:");
		createBranchInputSection(dialogAreaComposite);
		

		setTitle(TITLE);
		setMessage(DESCRIPTION);
		setWindowTitle(WINDOW_TITLE);
		applyDialogFont(dialogAreaComposite);

		return composite;
	}

	private void createBranchInputSection(Composite dialogAreaComposite) {
		branchInputViewer = new ComboViewer(new CCombo(dialogAreaComposite,
				SWT.BORDER | SWT.READ_ONLY));
		branchInputViewer.getControl().setLayoutData(
				new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
		branchInputViewer.setContentProvider(new ArrayContentProvider());
		branchInputViewer.setLabelProvider(new BranchLabelProvider());
		branchInputViewer
				.addSelectionChangedListener(new BranchSelectionChangedListener());
		branchInputViewer.getCCombo().setEnabled(false);
		
	}

	private void createUrlInputSection(Composite dialogAreaComposite) {
		Composite inputTextomposite = new Composite(dialogAreaComposite,
				SWT.NONE);
		GridLayout inputTextLayout = new GridLayout(2, false);
		inputTextLayout.marginWidth = 0;
		inputTextLayout.marginHeight = 8;
		inputTextLayout.verticalSpacing = 0;
		inputTextLayout.horizontalSpacing = 5;
		inputTextomposite.setLayout(inputTextLayout);
		inputTextomposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		inputTextomposite.setFont(dialogAreaComposite.getFont());

		//urlInputViewer = new ComboViewer(new CCombo(inputTextomposite,
		//		SWT.BORDER));
		urlInputViewer = new Text(inputTextomposite, SWT.SEARCH);
		//urlInputViewer.getControl().setLayoutData(
		//		new GridData(GridData.GRAB_HORIZONTAL
		//				| GridData.HORIZONTAL_ALIGN_FILL));
		//urlInputViewer.getControl().setFocus();
		urlInputViewer.setLayoutData(
						new GridData(GridData.GRAB_HORIZONTAL
								| GridData.HORIZONTAL_ALIGN_FILL));
		urlInputViewer.setFocus();
		

		connectButton = new Button(inputTextomposite, SWT.BORDER | SWT.PUSH);
		connectButton.setImage(Images.getImage(Images.IMG_CONNECTION));
		connectButton.setToolTipText("Connect to repository");
		connectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				//new ConnectToRepoJob(urlInputViewer.getCCombo().getText())
				//		.schedule();
				new ConnectToRepoJob(urlInputViewer.getText()).schedule();
			}
		});
	}

	private class ConnectToRepoJob extends UIJob {

		private final String url;

		public ConnectToRepoJob(String url) {
			super("");
			this.url = url;
		}

		private void updateBranchViewer(final Object[] input) {
			branchInputViewer.setInput(input);
			if (input == null || input.length == 0) {
				branchInputViewer.setSelection(null);
				branchInputViewer.getCCombo().setEnabled(false);
			} else {
				branchInputViewer.getCCombo().setEnabled(true);
				branchInputViewer
						.setSelection(new StructuredSelection(input[0]));
			}
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			setErrorMessage(null);

			try {
				monitor.beginTask("Connecting to repository: " + url, 100);
				RepoService repoService = CodeFacesUIActivator.getDefault()
						.getRepoService();

				final Repo repo = repoService.createRepo(url);
				monitor.worked(30);

				monitor.setTaskName("Fetching branches...");
				Collection<RepoBranch> branches = repo.getBranches();
				final Object[] input = branches.toArray();
				monitor.worked(70);

				updateBranchViewer(input);
			} catch (Exception e) {
				updateBranchViewer(null);
				setErrorMessage(e.getMessage());
			}

			return Status.OK_STATUS;
		}
	}

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

	@Override
	protected void okPressed() {
		if (!branchInputViewer.getSelection().isEmpty()) {
			super.okPressed();
		}
	}

	public RepoBranch getSelectedBranch() {
		return selectedBranch;
	}
}
