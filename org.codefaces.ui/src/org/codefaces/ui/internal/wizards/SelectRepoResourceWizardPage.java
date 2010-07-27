package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.internal.commons.RepoFolderOpenListener;
import org.codefaces.ui.internal.commons.RepoFolderViewFilter;
import org.codefaces.ui.internal.commons.RepoResourceComparator;
import org.codefaces.ui.internal.commons.RepoResourceContentProvider;
import org.codefaces.ui.internal.commons.RepoResourceLabelProvider;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.progress.UIJob;

public class SelectRepoResourceWizardPage extends WizardPage {
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

	private PopulateRepoStructureViewerJob populateRepoStructureViewerJob;

	protected SelectRepoResourceWizardPage(RepoSettings settings) {
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

		populateRepoStructureViewerJob = new PopulateRepoStructureViewerJob();

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

	private void populateRepoStructureViewer() {
		Object repo = settings.get(RepoSettings.REPO);
		Assert.isTrue(repo instanceof Repo);

		try {
			repoStructureViewer.setInput(((Repo) repo).getRoot());
			repoStructureViewer.setSelection(null);
			setErrorMessage(null);
		} catch (Exception e) {
			repoStructureViewer.setInput(null);
			repoStructureViewer.setSelection(null);
			setErrorMessage(e.getMessage());
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			if (populateRepoStructureViewerJob.getState() != Job.NONE) {
				populateRepoStructureViewerJob.cancel();
			}
			populateRepoStructureViewerJob.schedule();
		}
	}

	private class PopulateRepoStructureViewerJob extends UIJob {

		public PopulateRepoStructureViewerJob() {
			super("");
			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			populateRepoStructureViewer();

			return Status.OK_STATUS;
		}

	}
}
