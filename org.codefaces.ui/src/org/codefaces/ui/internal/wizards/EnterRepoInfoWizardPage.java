package org.codefaces.ui.internal.wizards;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.internal.Images;
import org.codefaces.ui.viewers.DefaultRepoResourceComparator;
import org.codefaces.ui.viewers.DefaultRepoResourceLabelProvider;
import org.codefaces.ui.viewers.DefaultRepoResourceTreeViewManager;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EnterRepoInfoWizardPage extends WizardPage {

	private final class GitHubRepoStructureTreeViewSelectionChangedListener
			implements ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ITreeSelection selections = (ITreeSelection) event
					.getSelection();
			if (selections.size() == 1) {
				RepoResource selectedResource = (RepoResource) selections
						.getFirstElement();
				if (GitHubUtil.isBranchOrTag(selectedResource)) {
					saveSettingsAndUpdatePageCompleteStatus(true,
							urlRepo,
							(RepoFolder) selectedResource);
					return;
				}
			}
			
			saveSettingsAndUpdatePageCompleteStatus(false, null, null);
		}
	}

	private static final String TITLE = "Import Repository";
	private static final String DESCRIPTION = "Fill in repository information";

	private static final String SAMPLE_URL = "http://github.com/jingweno/ruby_grep";
	private static final String URL_INPUT_SECTION_LABEL_TEXT = "Enter a GitHub Repository URL, e.g., "
		+ SAMPLE_URL;

	private static final String REPO_VIEW_SECTION_LABEL_TEXT = "Select a branch or tag to import.";
	
	private RepoSettings settings;
	private String repoType;

	private Text urlInputText;
	private Button connectButton;
	private TreeViewer repoStructureViewer;
	private DefaultRepoResourceTreeViewManager manager;
	
	private Repo urlRepo;

	protected EnterRepoInfoWizardPage(String pageName, RepoSettings settings) {
		super(pageName);
		Assert.isNotNull(settings);
		this.settings = settings;
		setTitle(TITLE);
		setDescription(DESCRIPTION);
		saveSettingsAndUpdatePageCompleteStatus(false, null, null);
	}

	@Override
	public void createControl(Composite parent) {
		Composite dialogAreaComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dialogAreaComposite.setFont(parent.getFont());

		createUrlInputSection(dialogAreaComposite);
		createRepoStructureViewerSection(dialogAreaComposite);
		setControl(dialogAreaComposite);
		bindControls();
		
		// we get the settings here because this method is called lazily 
		retrieveRepoType(settings);
	}
		
	private void createUrlInputSection(Composite parent) {
		Label urlInputLabel = new Label(parent, SWT.NONE);
		urlInputLabel.setText(URL_INPUT_SECTION_LABEL_TEXT);
	
		Composite inputTextomposite = new Composite(parent, SWT.NONE);
		GridLayout inputTextLayout = new GridLayout(2, false);
		inputTextLayout.marginWidth = 0;
		inputTextLayout.marginHeight = 8;
		inputTextLayout.verticalSpacing = 0;
		inputTextLayout.horizontalSpacing = 5;
		inputTextomposite.setLayout(inputTextLayout);
		inputTextomposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputTextomposite.setFont(parent.getFont());

		urlInputText = new Text(inputTextomposite, SWT.BORDER);
		urlInputText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		connectButton = new Button(inputTextomposite, SWT.BORDER | SWT.PUSH);
		connectButton.setImage(Images
				.getImageFromRegistry(Images.IMG_CONNECTION));
		connectButton.setToolTipText("Connect to repository");
	}


	private void createRepoStructureViewerSection(Composite parent){
		Label repoTreeViewLabel = new Label(parent, SWT.NONE);
		repoTreeViewLabel.setText(REPO_VIEW_SECTION_LABEL_TEXT);
		
		Composite repoStructureComposite = new Composite(parent, SWT.NONE);
		repoStructureComposite.setLayout(new FillLayout());
		repoStructureComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		repoStructureViewer = new TreeViewer(repoStructureComposite, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		manager = new DefaultRepoResourceTreeViewManager(repoStructureViewer);
		repoStructureViewer.setContentProvider(new GitHubRepoResourceContentProvider(manager));
		repoStructureViewer.setLabelProvider(new DefaultRepoResourceLabelProvider(manager));
		repoStructureViewer.setComparator(new DefaultRepoResourceComparator());
	}

	private void bindControls(){
		urlInputText.setFocus();
		
		connectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Repo repo = null;
				try {
					repo = connectToRepo(urlInputText.getText());
					repo.getRoot().getChildren();
					updateRepoStructureViewer(repo.getRoot());
					setErrorMessage(null);
				} catch (Exception e) {
					updateRepoStructureViewer(null);
					setErrorMessage(e.getMessage());
				}
				urlRepo = repo;
			}
		});

		repoStructureViewer
				.addOpenListener(new GitHubRepoResourceFolderOpenListener());
		repoStructureViewer
				.addSelectionChangedListener(new GitHubRepoStructureTreeViewSelectionChangedListener());
	}

	/**
	 * update the repo structure viewer
	 * @param rootFolder the new root folder
	 */
	private void updateRepoStructureViewer(RepoFolder rootFolder){
		if(manager != null){
			manager.dispose();
		}
		manager = new DefaultRepoResourceTreeViewManager(repoStructureViewer);
		repoStructureViewer.setContentProvider(new GitHubRepoResourceContentProvider(manager));
		repoStructureViewer.setLabelProvider(new DefaultRepoResourceLabelProvider(manager));
		repoStructureViewer.setInput(rootFolder);
	}

	/**
	 * Try to connect to the repository based on the given url. Set the error
	 * message if any exception occurs.
	 * 
	 * @param url
	 *            the url
	 * @return the repo created by the url, null if unsuccessful
	 * @throws MalformedURLException if url is Malformed
	 */
	private Repo connectToRepo(String url) throws MalformedURLException{
		return Repo.create(repoType, url);
	}

	
	/**
	 * Obtain the repository type from settings
	 */
	private void retrieveRepoType(RepoSettings settings){
		Object type = settings.get(RepoSettings.REPO_TYPE);
		Assert.isNotNull(type);
		Assert.isTrue(type instanceof String);
		repoType = (String)type;
	}

	/**
	 * Set page complete to <code>isPageComplete</code>, save the opened url
	 * repo and selected repo root to the setting. Please notice that the open
	 * repo is the repo associated by the url that user entered. And the
	 * selectedRepoBaseDirectory is the base folder that user selected and would
	 * be imported to the explorer. So the Repo of the
	 * selectedRepoBaseDirectory can be different from the urlRepo.
	 * 
	 * @param isPageComplete
	 *            set true if the page is complete.
	 * @param urlRepo
	 *            the repository associated to the url that user entered. If
	 *            isPageComplete is set to false, this parameter is ignored
	 * @param selectedRepoBaseDirectory
	 *            the folder that user would like to explore. If
	 *            isPageComplete is set to false, this parameter is ignored
	 */
	private void saveSettingsAndUpdatePageCompleteStatus(
			boolean isPageComplete, Repo urlRepo,
			RepoFolder selectedRepoBaseDirectory) {
		setPageComplete(isPageComplete);
		if(isPageComplete){
			settings.put(RepoSettings.URL_REPO, urlRepo);
			settings.put(RepoSettings.REPO_BASE_DIECTORY, selectedRepoBaseDirectory);
		}
		else{
			settings.put(RepoSettings.URL_REPO, null);
			settings.put(RepoSettings.REPO_BASE_DIECTORY, null);
		}
	}

	@Override
	public void dispose(){
		if(manager!=null){
			manager.dispose();
		}
	}
	
}
