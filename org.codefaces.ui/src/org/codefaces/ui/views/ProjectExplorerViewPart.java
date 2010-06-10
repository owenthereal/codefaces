package org.codefaces.ui.views;

import org.antlr.stringtemplate.StringTemplate;
import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeEventListener;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.Images;
import org.codefaces.ui.actions.SwitchBranchAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
	private static final String DEFAULT_STATUS_MSG_TEMPLATE = 
		"[ $branch$@$repo$ ]";
	private static final String SELECTION_STATUS_MSG_TEMPLATE = 
		"[ $branch$@$repo$ ] - $resource$";
	
	private TreeViewer viewer;

	private Workspace workspace;

	class ProjectExplorerContentProvider implements ITreeContentProvider {

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
			if (parent instanceof RepoResource) {
				return ((RepoResource) parent).getChildren().toArray();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof RepoResource) {
				return ((RepoResource) parent).hasChildren();
			}
			
			return false;
		}
	}

	class ProjectExplorerLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			if (obj instanceof RepoFolderRoot) {
				RepoFolderRoot root = (RepoFolderRoot) obj;
				return root.getName() + "@"
						+ root.getBranch().getRepo().getUrl();
			}

			if (obj instanceof RepoResource) {
				return ((RepoResource) obj).getName();
			}

			return obj.toString();
		}

		public Image getImage(Object obj) {
			if (obj instanceof RepoFolderRoot){
				return Images.getImageDescriptor(Images.IMG_REPO_FOLDER_ROOT)
						.createImage();
			}
			else{
				String imageKey = ISharedImages.IMG_OBJ_FILE;
				if (obj instanceof RepoFolder) {
					imageKey = ISharedImages.IMG_OBJ_FOLDER;
				}

				if (obj instanceof RepoFolderRoot) {
					imageKey = ISharedImages.IMG_OBJ_ELEMENT;
				}

				return PlatformUI.getWorkbench().getSharedImages().getImage(
					imageKey);
			}
		}
	}

	class ProjectExplorerViewerComparator extends ViewerComparator {
		@Override
		public int compare(Viewer viewer, Object obj1, Object obj2) {
			if (obj1 instanceof RepoFile && obj2 instanceof RepoFolder) {
				return 1;
			}

			if (obj1 instanceof RepoFolder && obj2 instanceof RepoFile) {
				return -1;
			}

			if (obj1 instanceof RepoFile && obj2 instanceof RepoFile) {
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
						RepoFile repoFileLite = (RepoFile) clickedRepoResource;
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
		// set an image
		setTitleImage(Images.getImageDescriptor(Images.IMG_NAVIGATOR)
				.createImage());
		workspace = Workspace.getCurrent();
		workspace
				.addWorkSpaceChangeEventListener(new WorkspaceChangeEventListener() {
					@Override
					public void workspaceChanged(WorkspaceChangeEvent evt) {
						update(evt.getRepoBranch());
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
		viewer.setComparator(new ProjectExplorerViewerComparator());
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if(event.getSelection().isEmpty()){
					showDefaultStatusMessage();
				}
				if(event.getSelection() instanceof IStructuredSelection){
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					if(selection.size() == 1){
						RepoResource resource = (RepoResource)selection.getFirstElement();
						showSelectionStatusMessage(resource);
					}
					else{
						showDefaultStatusMessage();
					}
				}
			}
		});
	}

	/**
	 * Update the Explorer input to the given RepoResource.
	 * 
	 * @param workingBranch
	 *            the new working branch
	 */
	public void update(RepoBranch workingBranch) {
		viewer.setInput(workingBranch);
		showDefaultStatusMessage();
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
		Action switchBranchAction = new SwitchBranchAction();

		switchBranchAction.setImageDescriptor(Images
				.getImageDescriptor(Images.IMG_BRANCHES));
		toolbar.add(switchBranchAction);
	}
	
	
	
	/** 
	 * Show the given message in the status bar
	 * @param msg the message
	 */
	private void showStatusMessage(String msg){
		IStatusLineManager statusline = getViewSite().getActionBars()
				.getStatusLineManager();
		statusline.setMessage(msg);
	}
	
	/**
	 * Display a default status msg on the status bar
	 */
	private void showDefaultStatusMessage(){
		Workspace ws = Workspace.getCurrent();
		if(ws.getWorkingBranch() == null) return;
		
		StringTemplate template = new StringTemplate(DEFAULT_STATUS_MSG_TEMPLATE);
		template.setAttribute("branch", ws
				.getWorkingBranch().getName());
		template.setAttribute("repo", ws
				.getWorkingBranch().getRepo().getName());
		showStatusMessage(template.toString());
	}
	
	/**
	 * Display a message when an item is selected
	 * @param resourceSelected the selected item
	 */
	private void showSelectionStatusMessage(RepoResource resourceSelected){
		Workspace ws = Workspace.getCurrent();
		if(ws.getWorkingBranch() == null) return;
		
		StringTemplate template = new StringTemplate(SELECTION_STATUS_MSG_TEMPLATE);
		template.setAttribute("branch", ws
			.getWorkingBranch().getName());
		template.setAttribute("repo", ws
				.getWorkingBranch().getRepo().getName());
		template.setAttribute("resource", resourceSelected.getName());
		showStatusMessage(template.toString());
	}
}
