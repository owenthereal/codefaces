package org.codefaces.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.codefaces.core.SCMManager;
import org.codefaces.core.connectors.SCMConnectorDescriber;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.wizards.SCMConnectorUIDescriber;
import org.codefaces.ui.internal.wizards.SCMConnectorUIManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RepositorySettingsPage extends WizardPage {
	private final class ConnectorSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			String selectedConnector = (String) ((IStructuredSelection) event
					.getSelection()).getFirstElement();

			getSettings().put(RepoSettings.REPO_KIND, selectedConnector);

			switchToSettingsSection(selectedConnector);
			verifyPageComplete();
		}
	}

	private static final String TITLE = "Enter repository location information";

	private ComboViewer connectorViewer;

	private RepoSettings settings;

	private RepositorySettingsSection settingsSection;

	private Group settingsSectionComposite;

	private Composite dialogAreaComposite;

	public RepositorySettingsPage(RepoSettings settings) {
		super(TITLE);
		setTitle(TITLE);

		this.settings = settings;
	}

	private void bindConnectorViewer() {
		connectorViewer
				.addSelectionChangedListener(new ConnectorSelectionChangedListener());
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	protected void createConnectorViewer(Composite parent) {
		connectorViewer = new ComboViewer(new CCombo(parent, SWT.BORDER
				| SWT.READ_ONLY));
		connectorViewer.getControl().setLayoutData(
				new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
		connectorViewer.setContentProvider(new ArrayContentProvider());
		connectorViewer.setLabelProvider(new LabelProvider());
	}

	@Override
	public void createControl(Composite parent) {
		dialogAreaComposite = new Composite(parent, SWT.NONE);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = IDialogConstants.HORIZONTAL_MARGIN;
		layout.marginWidth = IDialogConstants.VERTICAL_MARGIN;
		layout.verticalSpacing = 15;
		layout.horizontalSpacing = IDialogConstants.HORIZONTAL_SPACING;
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setFont(parent.getFont());
		setControl(dialogAreaComposite);

		Group connectorKindGroup = new Group(dialogAreaComposite, SWT.NONE);
		connectorKindGroup.setText("Kind");
		connectorKindGroup.setLayout(new GridLayout());
		connectorKindGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		connectorKindGroup.setFont(dialogAreaComposite.getFont());

		createConnectorViewer(connectorKindGroup);
		bindConnectorViewer();

		populateConnectorViewer();
	}

	private void switchToSettingsSection(String connectorKind) {
		if (settingsSectionComposite != null) {
			settingsSectionComposite.dispose();
		}

		SCMConnectorUIManager connectorUIManager = CodeFacesUIActivator
				.getDefault().getConnectorUIManager();
		SCMConnectorUIDescriber connectorUIDescriber = connectorUIManager
				.getConnectorUIDescriber(connectorKind);
		if (connectorUIDescriber == null) {
			settingsSection = null;
			return;
		}

		settingsSectionComposite = new Group(dialogAreaComposite, SWT.NONE);
		settingsSectionComposite.setText("Connection");

		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		settingsSectionComposite.setLayoutData(layoutData);

		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = IDialogConstants.HORIZONTAL_MARGIN;
		layout.marginWidth = IDialogConstants.VERTICAL_MARGIN;
		layout.verticalSpacing = IDialogConstants.VERTICAL_SPACING;
		layout.horizontalSpacing = IDialogConstants.HORIZONTAL_SPACING;
		settingsSectionComposite.setLayout(layout);

		settingsSectionComposite.setFont(dialogAreaComposite.getFont());

		settingsSection = connectorUIDescriber.createSettingsSection();
		settingsSection.createSettingsSection(this, settingsSectionComposite,
				settings);

		dialogAreaComposite.layout();
		settingsSection.setFocus();

		setDescription(connectorUIDescriber.getDescription());
	}

	protected ComboViewer getConnectorViewer() {
		return connectorViewer;
	}

	@Override
	public IWizardPage getNextPage() {
		setErrorMessage(null);

		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				if (settingsSection != null) {
					settingsSection.handleConnection(monitor);
				}
			}
		};

		try {
			getContainer().run(false, true, op);
		} catch (Exception e) {
			setErrorMessage(e.getCause().getMessage());
			return null;
		}

		return super.getNextPage();
	}

	protected RepoSettings getSettings() {
		return settings;
	}

	private void populateConnectorViewer() {
		List<String> connectorKinds = new ArrayList<String>();
		for (SCMConnectorDescriber describer : SCMManager.getInstance()
				.getConnectorManager().getConnectorDescribers()) {
			connectorKinds.add(describer.getKind());
		}
		setConnectorViewerInput(connectorKinds.toArray(new String[0]));
	}

	private void setConnectorViewerInput(final String[] connectorsAvailable) {
		connectorViewer.setInput(connectorsAvailable);
		if (connectorsAvailable.length > 0) {
			connectorViewer.setSelection(new StructuredSelection(
					connectorsAvailable[0]));
		}
	}

	public void verifyPageComplete() {
		setPageComplete(getSettings().get(RepoSettings.REPO_KIND) != null
				&& settingsSection != null && settingsSection.isSettingsValid());
	}
}
