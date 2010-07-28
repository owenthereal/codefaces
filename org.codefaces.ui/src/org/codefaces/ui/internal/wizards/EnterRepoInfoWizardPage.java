package org.codefaces.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.SCMManager;
import org.codefaces.core.connectors.SCMConnectorDescriber;
import org.codefaces.core.models.Repo;
import org.eclipse.core.runtime.Assert;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EnterRepoInfoWizardPage extends WizardPage {
	private static final String TITLE = "Enter repository location information";

	private static final String SAMPLE_URL = "http://github.com/jingweno/ruby_grep";

	private static final String DESCRIPTION = "Enter a GitHub Repository URL, e.g., "
			+ SAMPLE_URL;

	private ComboViewer connectorViewer;

	private RepoSettings settings;

	private Text locationText;

	private final class ConnectorSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			String selectedConnector = (String) ((IStructuredSelection) event
					.getSelection()).getFirstElement();

			settings.put(RepoSettings.REPO_KIND, selectedConnector);
			locationText.setFocus();
			verifyPageComplete();
		}
	}

	protected EnterRepoInfoWizardPage(RepoSettings settings) {
		super(TITLE);
		setTitle(TITLE);
		setDescription(DESCRIPTION);

		this.settings = settings;
	}

	private void verifyPageComplete() {
		setPageComplete(settings.get(RepoSettings.REPO_KIND) != null
				&& !StringUtils.isEmpty((String) settings
						.get(RepoSettings.REPO_URL)));
	}

	@Override
	public void createControl(Composite parent) {
		Composite dialogAreaComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = IDialogConstants.HORIZONTAL_MARGIN;
		layout.marginWidth = IDialogConstants.VERTICAL_MARGIN;
		layout.verticalSpacing = IDialogConstants.VERTICAL_SPACING;
		layout.horizontalSpacing = IDialogConstants.HORIZONTAL_SPACING;
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dialogAreaComposite.setFont(parent.getFont());
		setControl(dialogAreaComposite);

		Label connectorLabel = new Label(dialogAreaComposite, SWT.NONE);
		connectorLabel.setText("Repository: ");
		createConnectorViewer(dialogAreaComposite);
		bindConnectorViewer();

		Label locationInputLabel = new Label(dialogAreaComposite, SWT.NONE);
		locationInputLabel.setText("Location: ");
		createLocationText(dialogAreaComposite);
		bindLocationText();

		populateConnectorViewer();
	}

	protected void bindLocationText() {
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String location = locationText.getText();
				settings.put(RepoSettings.REPO_URL, location);
				verifyPageComplete();
			}
		});
	}

	protected void createLocationText(Composite parent) {
		locationText = new Text(parent, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
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

	private void populateConnectorViewer() {
		List<String> connectorKinds = new ArrayList<String>();
		for (SCMConnectorDescriber describer : SCMManager.getInstance()
				.getConnectorManager().getConnectorDescribers()) {
			connectorKinds.add(describer.getKind());
		}
		setConnectorViewerInput(connectorKinds.toArray(new String[0]));
	}

	private void bindConnectorViewer() {
		connectorViewer
				.addSelectionChangedListener(new ConnectorSelectionChangedListener());
	}

	private void setConnectorViewerInput(final String[] connectorsAvailable) {
		connectorViewer.setInput(connectorsAvailable);
		if (connectorsAvailable.length > 0) {
			connectorViewer.setSelection(new StructuredSelection(
					connectorsAvailable[0]));
		}
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	@Override
	public IWizardPage getNextPage() {
		setErrorMessage(null);
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				Object typePara = settings.get(RepoSettings.REPO_KIND);
				Object locationPara = settings.get(RepoSettings.REPO_URL);
				Assert.isTrue(typePara instanceof String);
				Assert.isTrue(locationPara instanceof String);

				String type = (String) typePara;
				String location = (String) locationPara;

				monitor.beginTask("Connecting to " + type + " repository "
						+ location, 100);
				Repo repo = Repo.create(type, location);
				settings.put(RepoSettings.REPO, repo);
				monitor.done();
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
}
