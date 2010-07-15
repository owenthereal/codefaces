package org.codefaces.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codefaces.core.SCMManager;
import org.codefaces.core.connectors.SCMConnectorDescriber;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.Images;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
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

	private class ConnectToRepoJob extends UIJob {

		private String url;

		public ConnectToRepoJob() {
			super("");
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			setErrorMessage(null);
			getButton(IDialogConstants.OK_ID).setEnabled(false);

			try {
				monitor.beginTask("Connecting to repository: " + url, 100);
				IStructuredSelection selection = (IStructuredSelection) connectorInputViewer
						.getSelection();

				final Repo repo = Repo.create(
						(String) selection.getFirstElement(), url);
				monitor.worked(30);

				monitor.setTaskName("Fetching branches...");
				Collection<RepoBranch> branches = repo.getBranches();
				final Object[] input = branches.toArray();
				monitor.worked(70);

				updateStructuredViewer(branchInputViewer, input);
			} catch (Exception e) {
				updateStructuredViewer(branchInputViewer, null);
				setErrorMessage(e.getMessage());
			}

			return Status.OK_STATUS;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	private static final String SAMPLE_URL = "http://github.com/jingweno/ruby_grep";

	private static final String DESCRIPTION = "Enter a GitHub Repository URL, e.g., "
			+ SAMPLE_URL;

	private static final String NO_BRANCH_IS_SELECTED = "No branch is selected.";

	public static final String TITLE = "Checkout projects from a repository";

	private static final String WINDOW_TITLE = "Import from a repository";

	private ComboViewer branchInputViewer;

	private Button connectButton;

	private ComboViewer connectorInputViewer;

	private ConnectToRepoJob connectToRepoJob = new ConnectToRepoJob();

	private RepoBranch selectedBranch;

	private Text urlInputText;

	public RepoUrlInputDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | getDefaultOrientation());
	}

	private void bindControls() {
		urlInputText.setFocus();

		if (connectorInputViewer.getSelection().isEmpty()) {
			connectButton.setEnabled(false);
		}

		connectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				connectToRepo();
			}
		});

		branchInputViewer
				.addSelectionChangedListener(new BranchSelectionChangedListener());
	}

	private void connectToRepo() {
		if (connectToRepoJob.getState() != Job.NONE) {
			connectToRepoJob.cancel();
		}

		connectToRepoJob.setUrl(urlInputText.getText());
		connectToRepoJob.schedule();
	}

	private void createBranchInputSection(Composite dialogAreaComposite) {
		branchInputViewer = new ComboViewer(new CCombo(dialogAreaComposite,
				SWT.BORDER | SWT.READ_ONLY));
		branchInputViewer.getControl().setLayoutData(
				new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
		branchInputViewer.setContentProvider(new ArrayContentProvider());
		branchInputViewer.setLabelProvider(new BranchLabelProvider());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	private void createConnectorSection(Composite dialogAreaComposite) {
		connectorInputViewer = new ComboViewer(new CCombo(dialogAreaComposite,
				SWT.BORDER | SWT.READ_ONLY));
		connectorInputViewer.getControl().setLayoutData(
				new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
		connectorInputViewer.setContentProvider(new ArrayContentProvider());
		connectorInputViewer.setLabelProvider(new LabelProvider());
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

		Label connectorInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		connectorInputLabel.setText("Connector:");
		createConnectorSection(dialogAreaComposite);

		Label urlInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		urlInputLabel.setText("Repository:");
		createUrlInputSection(dialogAreaComposite);

		Label branchInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		branchInputLabel.setText("Branch:");
		createBranchInputSection(dialogAreaComposite);

		populateControls();
		bindControls();

		setTitle(TITLE);
		setMessage(DESCRIPTION);
		setWindowTitle(WINDOW_TITLE);
		applyDialogFont(dialogAreaComposite);

		return composite;
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

		urlInputText = new Text(inputTextomposite, SWT.SEARCH);
		urlInputText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		connectButton = new Button(inputTextomposite, SWT.BORDER | SWT.PUSH);
		connectButton.setImage(Images
				.getImageFromRegistry(Images.IMG_CONNECTION));
		connectButton.setToolTipText("Connect to repository");
	}

	public RepoBranch getSelectedBranch() {
		return selectedBranch;
	}

	@Override
	protected void okPressed() {
		if (!branchInputViewer.getSelection().isEmpty()) {
			super.okPressed();
		}
	}

	private void populateControls() {
		List<String> connectorKinds = new ArrayList<String>();
		for (SCMConnectorDescriber describer : SCMManager.getInstance()
				.getConnectorManager().getConnectorDescribers()) {
			connectorKinds.add(describer.getKind());
		}

		updateStructuredViewer(connectorInputViewer,
				connectorKinds.toArray(new String[0]));
	}

	private void setWindowTitle(String windowTitle) {
		if (getShell() == null) {
			return;
		}

		getShell().setText(windowTitle);

	}

	private void updateStructuredViewer(StructuredViewer viewer,
			final Object[] input) {
		viewer.setInput(input);
		if (input == null || input.length == 0) {
			viewer.setSelection(null);
		} else {
			viewer.setSelection(new StructuredSelection(input[0]));
		}
	}
}
