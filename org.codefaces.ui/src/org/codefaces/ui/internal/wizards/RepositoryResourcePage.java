package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.internal.commons.RepoFolderOpenListener;
import org.codefaces.ui.internal.commons.RepoFolderViewFilter;
import org.codefaces.ui.internal.commons.RepoResourceComparator;
import org.codefaces.ui.internal.commons.RepoResourceContentProvider;
import org.codefaces.ui.internal.commons.RepoResourceLabelProvider;
import org.codefaces.ui.wizards.RepoSettings;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class RepositoryResourcePage extends WizardPage {
	private class TreeViewSelectionChangedListener implements
			ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			Object selectedElement = ((IStructuredSelection) event
					.getSelection()).getFirstElement();
			if (!(selectedElement instanceof RepoResource)) {
				return;
			}

			RepoResource selection = (RepoResource) selectedElement;
			setPageComplete(selection != null);
			settings.put(RepoSettings.REPO_RESOURCE_INPUT, selection);
		}
	}

	private static final String DESCRIPTION = "Select the folder to be imported";

	private static final String TITLE = "Select folder";

	private TreeViewer repoStructureViewer;

	private RepoSettings settings;

	protected RepositoryResourcePage(RepoSettings settings) {
		super(TITLE);
		setTitle(TITLE);
		setDescription(DESCRIPTION);

		this.settings = settings;
	}

	private void bindRepoStructureViewer() {
		repoStructureViewer.addOpenListener(new RepoFolderOpenListener());
		repoStructureViewer
				.addSelectionChangedListener(new TreeViewSelectionChangedListener());
	}

	@Override
	public void createControl(Composite parent) {
		Composite dialogAreaComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		dialogAreaComposite.setLayout(layout);
		dialogAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dialogAreaComposite.setFont(parent.getFont());
		setControl(dialogAreaComposite);

		createRepoStructureViewer(dialogAreaComposite);
		bindRepoStructureViewer();

		setPageComplete(false);
	}

	private void createRepoStructureViewer(Composite parent) {
		repoStructureViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		repoStructureViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));

		repoStructureViewer
				.setContentProvider(new RepoResourceContentProvider());
		repoStructureViewer.setLabelProvider(new RepoResourceLabelProvider());
		repoStructureViewer
				.setFilters(new ViewerFilter[] { new RepoFolderViewFilter() });
		repoStructureViewer.setComparator(new RepoResourceComparator());
	}

	private void populateRepoStructureViewer(Repo repo) {
		RepoFolderRoot root = null;
		if (repo != null) {
			root = repo.getRoot();
		}

		repoStructureViewer.setInput(root);
		repoStructureViewer.setSelection(null);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			new ConnectorToRepositoryJob().schedule();
		}
	}

	private void connectToRepository() {
		Object typePara = settings.get(RepoSettings.REPO_KIND);
		Assert.isTrue(typePara instanceof String);

		Object locationPara = settings.get(RepoSettings.REPO_URL);
		Assert.isTrue(locationPara instanceof String);

		Object userNamePara = settings.get(RepoSettings.REPO_USER);
		Assert.isTrue(userNamePara == null || userNamePara instanceof String);

		Object passwordPara = settings.get(RepoSettings.REPO_PASSWORD);
		Assert.isTrue(passwordPara == null || passwordPara instanceof String);

		final String type = (String) typePara;
		final String location = (String) locationPara;
		final String username = (String) userNamePara;
		final String password = (String) passwordPara;

		Repo repo = Repo.create(type, location, username, password);
		settings.put(RepoSettings.REPO, repo);
	}

	private class ConnectorToRepositoryJob extends Job {
		public ConnectorToRepositoryJob() {
			super("Connectoring to repository");
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					populateRepoStructureViewer(null);
					setErrorMessage(null);
				}
			});

			try {
				UICallBack.runNonUIThreadWithFakeContext(
						RepositoryResourcePage.this.getShell().getDisplay(),
						new Runnable() {
							@Override
							public void run() {
								connectToRepository();
							}
						});

				runOnUIThread(new Runnable() {
					@Override
					public void run() {
						populateRepoStructureViewer((Repo) settings
								.get(RepoSettings.REPO));
					}
				});

			} catch (final Exception e) {
				runOnUIThread(new Runnable() {
					@Override
					public void run() {
						setErrorMessage(e.getMessage());
					}
				});
			}

			return Status.OK_STATUS;
		}

		private void runOnUIThread(Runnable runnable) {
			RepositoryResourcePage.this.getShell().getDisplay()
					.asyncExec(runnable);
		}

	}
}
