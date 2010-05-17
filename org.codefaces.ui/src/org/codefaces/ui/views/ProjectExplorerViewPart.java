package org.codefaces.ui.views;

import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
import org.codefaces.ui.Images;
import org.codefaces.ui.actions.ExplorerSwitchBranchAction;
import org.codefaces.ui.events.WorkSpaceChangeEvent;
import org.codefaces.ui.events.WorkSpaceChangeEventListener;
import org.codefaces.ui.resources.Workspace;
import org.codefaces.ui.resources.WorkspaceManager;
import org.codefaces.ui.resources.Workspace.Resources;
import org.codefaces.ui.utils.Util;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ProjectExplorerViewPart extends ViewPart {

	public static final String ID = "org.codefaces.ui.view.projectExplorer";

	private TreeViewer viewer;

	private Workspace workspace;

	class ProjectExplorerContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof RepoResource) {
				return ((RepoResource) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof RepoContainer) {
				return ((RepoContainer) parent).getChildren().toArray();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			return getChildren(parent).length > 0;
		}
	}

	class ProjectExplorerLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			if (obj instanceof RepoResource) {
				return ((RepoResource) obj).getName();
			}

			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_FILE;
			if (obj instanceof RepoContainer) {
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			}
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					imageKey);
		}
	}

	class ProjectExplorerViewerComparator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object obj1, Object obj2) {
			if (obj1 instanceof RepoResource && obj2 instanceof RepoContainer) {
				return 1;
			}

			if (obj1 instanceof RepoContainer && obj2 instanceof RepoResource) {
				return -1;
			}

			if (obj1 instanceof RepoResource && obj2 instanceof RepoResource) {
				super.compare(viewer, obj1, obj2);
			}

			return 0;
		}
	}

	class FolderDoubleClickListener implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();
			RepoResource clickedRepoResource = (RepoResource) selection
					.getFirstElement();

			Viewer viewer = event.getViewer();
			if (clickedRepoResource.getType() == RepoResourceType.FOLDER
					&& viewer instanceof TreeViewer) {
				TreeViewer treeViewer = (TreeViewer) viewer;
				if (treeViewer.isExpandable(clickedRepoResource)) {
					treeViewer.setExpandedState(clickedRepoResource,
							!treeViewer.getExpandedState(clickedRepoResource));
				}
			}
		}
	}

	class FileDoubleClickListener implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();
			RepoResource clickedRepoResource = (RepoResource) selection
					.getFirstElement();

			if (clickedRepoResource.getType() == RepoResourceType.FILE) {
				IWorkbenchPage activePage = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					IViewPart viewPart = activePage.showView(
							CodeExplorerViewPart.ID, clickedRepoResource
									.getId(), IWorkbenchPage.VIEW_ACTIVATE);
					if (viewPart instanceof CodeExplorerViewPart) {
						RepoFileLite repoFileLite = (RepoFileLite) clickedRepoResource;
						RepoFile repoFile = (RepoFile) repoFileLite
								.getAdapter(RepoFile.class);
						((CodeExplorerViewPart) viewPart).setInput(repoFile);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	/**
	 * Initialize the ExplorerView 
	 */
	public void createPartControl(Composite parent) {
		// create a tool bar
		createToolBar(parent);
		// create the project viewer
		createViewer(parent);
		workspace = WorkspaceManager.getInstance().getCurrentWorkspace();
		workspace.addWorkSpaceChangeEventListener(new WorkSpaceChangeEventListener() {
			@Override
			public void workSpaceChanged(WorkSpaceChangeEvent evt) {
				// we are only interested in the working branch change
				if (evt.getResourcesChanged().contains(Resources.BRANCH)) {
					update(evt.getRepoBranch());
				}
			}
		});
	}

	/**
	 * Create and initialize the viewer
	 */
	private void createViewer(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new ProjectExplorerContentProvider());
		viewer.setLabelProvider(new ProjectExplorerLabelProvider());
		viewer.setComparator(new ProjectExplorerViewerComparator());
		viewer.addDoubleClickListener(new FileDoubleClickListener());
		viewer.addDoubleClickListener(new FolderDoubleClickListener());
		viewer.setInput(WorkspaceManager.getInstance().getCurrentWorkspace()
				.getWorkingRepoBranch());
		viewer.setComparator(new ProjectExplorerViewerComparator());
	}

	/**
	 * Update the Explorer input to the given RepoResource.
	 * 
	 * @param workingBranch
	 *            the new working branch
	 */
	public void update(RepoContainer workingBranch) {
		viewer.setInput(workingBranch);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	/**
	 * Create and fill in the tool bar
	 */
	private void createToolBar(Composite parent) {
		IToolBarManager toolbar = getViewSite().getActionBars()
				.getToolBarManager();
		Action switchBranchAction = new ExplorerSwitchBranchAction();

		switchBranchAction.setImageDescriptor(Util
				.getImageDescriptor(Images.IMG_BRANCHES));
		toolbar.add(switchBranchAction);
	}
}
