package org.codefaces.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.codefaces.core.SCMManager;
import org.codefaces.core.connectors.SCMConnectorDescriber;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.Images;
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
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RepoUrlInputDialog extends TitleAreaDialog {
	private final class BranchLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			RepoFolder branch = (RepoFolder) element;
			return branch.getName();
		}
	}

	private final class BranchSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			RepoFolder selectedBranch = (RepoFolder) ((IStructuredSelection) event
					.getSelection()).getFirstElement();
			selectBranch(selectedBranch);
		}
	}

	private final class ConnectorSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			selectedConnector = (String) ((IStructuredSelection) event
					.getSelection()).getFirstElement();
			if (selectedConnector == null) {
				connectButton.setEnabled(false);
			} else {
				connectButton.setEnabled(true);
			}
		}
	}

	private static class ConnectToRepoResponseDTO {
		private Repo repo;

		private String errorMessages;

		public Repo getRepo() {
			return repo;
		}

		public void setRepo(Repo repo) {
			this.repo = repo;
		}

		public String getErrorMessages() {
			return errorMessages;
		}

		public void setErrorMessages(String errorMessages) {
			this.errorMessages = errorMessages;
		}
	}

	private static class ConnectToRepoRequestDTO {
		private String kind;

		private String url;

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	private class ConnectToRepoJob extends Job {
		public ConnectToRepoJob() {
			super("Fetching Branches");
			setSystem(true);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			try {
				ConnectToRepoRequestDTO requesetDTO = null;
				while ((requesetDTO = waitingQueue.take()) != null) {
					runFetchBranchesJob(requesetDTO.getKind(),
							requesetDTO.getUrl(), monitor);
				}
			} catch (InterruptedException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when making a request", e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}

			return Status.OK_STATUS;
		}

		protected void runFetchBranchesJob(final String kind, final String url,
				final IProgressMonitor monitor) {
			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					setErrorMessage(null);
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setMessage("Loading...");
					updateStructuredViewer(branchInputViewer, null);
				}
			});

			final ConnectToRepoResponseDTO responseDTO = new ConnectToRepoResponseDTO();
			UICallBack.runNonUIThreadWithFakeContext(getShell().getDisplay(),
					new Runnable() {
						@Override
						public void run() {
							fetchBranches(responseDTO, kind, url, monitor);
						}
					});

			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					String errorMessages = responseDTO.getErrorMessages();
					if (errorMessages == null) {
						setMessage(DESCRIPTION);
						updateStructuredViewer(branchInputViewer, responseDTO
								.getRepo().getRoot().getChildren().toArray());
					} else {
						setMessage(DESCRIPTION);
						setErrorMessage(errorMessages);
						updateStructuredViewer(branchInputViewer, null);
					}
				}
			});
		}

		private void fetchBranches(ConnectToRepoResponseDTO transporter,
				String kind, String url, final IProgressMonitor monitor) {
			try {
				monitor.beginTask("Connecting to repository: " + url, 100);
				final Repo repo = Repo.create(kind, url, null, null);
				monitor.worked(30);

				monitor.setTaskName("Fetching branches...");
				repo.getRoot().getChildren();
				monitor.worked(70);

				transporter.setRepo(repo);
			} catch (final Exception e) {
				transporter.setErrorMessages(e.getMessage());
			}
		}

		private final void runOnUIThread(final Runnable runnable) {
			Shell shell = getShell();
			if (shell == null) {
				return;
			}

			Display display = shell.getDisplay();
			if (display != null && !display.isDisposed()) {
				display.syncExec(runnable);
			}
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

	private RepoFolder selectedBranch;

	protected String selectedConnector;

	private Text urlInputText;

	private BlockingQueue<ConnectToRepoRequestDTO> waitingQueue = new LinkedBlockingDeque<ConnectToRepoRequestDTO>();

	public RepoUrlInputDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | getDefaultOrientation());
	}

	private void bindControls() {
		urlInputText.setFocus();

		connectorInputViewer
				.addSelectionChangedListener(new ConnectorSelectionChangedListener());

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
		try {
			ConnectToRepoRequestDTO requestDTO = new ConnectToRepoRequestDTO();
			requestDTO.setKind(selectedConnector);
			requestDTO.setUrl(urlInputText.getText());
			waitingQueue.put(requestDTO);
		} catch (InterruptedException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when connecting to repoistory with connector "
							+ selectedConnector + " and url "
							+ urlInputText.getText(), e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}
	}

	protected void cancelRunningConnectingJob() {
		if (connectToRepoJob.getState() != Job.NONE) {
			connectToRepoJob.cancel();
		}
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

		bindControls();
		populateControls();

		setTitle(TITLE);
		setMessage(DESCRIPTION);
		setWindowTitle(WINDOW_TITLE);
		applyDialogFont(dialogAreaComposite);

		connectToRepoJob.schedule();
		composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				cancelRunningConnectingJob();
			}
		});

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

	public RepoFolder getSelectedBranch() {
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

	private void selectBranch(RepoFolder branch) {
		selectedBranch = branch;
		if (branch == null) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else {
			setErrorMessage(null);
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
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
		if (input == null) {
			viewer.setSelection(null);
		} else if (input.length == 0) {
			viewer.setSelection(null);
			setErrorMessage(NO_BRANCH_IS_SELECTED);
		} else {
			viewer.setSelection(new StructuredSelection(input[0]));
		}
	}
}
