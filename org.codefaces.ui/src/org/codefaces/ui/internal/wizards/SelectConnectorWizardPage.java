package org.codefaces.ui.internal.wizards;

import java.util.ArrayList;
import java.util.List;

import org.codefaces.core.SCMManager;
import org.codefaces.core.connectors.SCMConnectorDescriber;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SelectConnectorWizardPage extends WizardPage {
	
	private static final String DESCRIPTION = "Import a repository using a source control client connector.";
	private static final String TITLE = "Select";
	private static final String LABEL_TEXT = "Select a connector:";
	private ComboViewer connectorInputViewer;
	private String selectedConnector;
	private RepoSettings settings;
	
	
	private final class ConnectorSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			selectedConnector = (String) ((IStructuredSelection) event
					.getSelection()).getFirstElement();
			if (selectedConnector == null) {
				saveSettingsAndUpdatePageCompleteStatus(false);
			} else {
				saveSettingsAndUpdatePageCompleteStatus(true);
			}
		}
	}

	protected SelectConnectorWizardPage(String pageName, RepoSettings settings){
		super(pageName);
		Assert.isNotNull(settings);
		this.settings = settings;
		
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		saveSettingsAndUpdatePageCompleteStatus(false);
	}
	

	@Override
	public void createControl(Composite parent) {
		Composite dialogAreaComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dialogAreaComposite.setFont(parent.getFont());

		createConnectorSection(dialogAreaComposite);
		populateConnectorViewer();
		bindControl();
		
		// Required to avoid an error in the system
		setControl(dialogAreaComposite);
	}
	
	/**
	 * fill in the parent composite with the connector section
	 */
	private void createConnectorSection(Composite parent) {
		Label connectorInputLabel = new Label(parent, SWT.NONE);
		connectorInputLabel.setText(LABEL_TEXT);
		GridData connectorInputLabelGridData = new GridData();
		connectorInputLabelGridData.horizontalSpan = 2;
		connectorInputLabel.setLayoutData(connectorInputLabelGridData);
		
		connectorInputViewer = new ComboViewer(new CCombo(parent,
				SWT.BORDER | SWT.READ_ONLY));
		connectorInputViewer.getControl().setLayoutData(
				new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
		connectorInputViewer.setContentProvider(new ArrayContentProvider());
		connectorInputViewer.setLabelProvider(new LabelProvider());
	}
	
	/**
	 * Fill in the content of the connector viewer
	 */
	private void populateConnectorViewer() {
		List<String> connectorKinds = new ArrayList<String>();
		for (SCMConnectorDescriber describer : SCMManager.getInstance()
				.getConnectorManager().getConnectorDescribers()) {
			connectorKinds.add(describer.getKind());
		}
		updateConnectorViewer(connectorKinds.toArray(new String[0]));
	}

	
	/**
	 * bind the listener to the viewer
	 */
	private void bindControl() {
		connectorInputViewer
				.addSelectionChangedListener(new ConnectorSelectionChangedListener());
	}
	

	/**
	 * Update the connector viewer with the given input
	 */
	private void updateConnectorViewer(final String[] connectorsAvailable) {
		connectorInputViewer.setInput(connectorsAvailable);
		if (connectorsAvailable == null) {
			connectorInputViewer.setSelection(null);
		} else if (connectorsAvailable.length == 0) {
			connectorInputViewer.setSelection(null);
		} else {
			selectedConnector = connectorsAvailable[0];
			connectorInputViewer.setSelection(new StructuredSelection(selectedConnector));
			saveSettingsAndUpdatePageCompleteStatus(true);
		}
	}
	
	/**
	 * Set page complete to <code>isPageComplete</code> and save the selected connector to 
	 * the settings.
	 */
	private void saveSettingsAndUpdatePageCompleteStatus(boolean isPageComplete){
		setPageComplete(isPageComplete);
		if(isPageComplete){
			settings.put(RepoSettings.REPO_TYPE, selectedConnector);
		}
		else{
			settings.put(RepoSettings.REPO_TYPE, null);
		}
	}

}
