package org.codefaces.ui.views;

import java.net.MalformedURLException;
import java.net.URL;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;

public class ProjectExplorerViewPart extends ViewPart {

	public static final String ID = "org.codefaces.ui.view.projectExplorer";

	private TreeViewer viewer;

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

	/**
	 * FIXME: testing data now.
	 */
	private RepoResource createDummyModel() {
		try {
			Repo repo = RepoManager.getInstance().getRepoService().getRepo(
					"http://github.com/jnunemaker/mongomapper");
			return repo.getBranches().iterator().next();
		} catch (RepoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	class DoubleClickListener implements IDoubleClickListener {
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

				return;
			}

			if (clickedRepoResource.getType() == RepoResourceType.FILE) {
				IWorkbenchBrowserSupport browserSupport;
				browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
				try {
					int style = IWorkbenchBrowserSupport.AS_EDITOR;
					IWebBrowser browser = browserSupport.createBrowser(style,
							clickedRepoResource.getId(), "", "");
					browser.openURL(new URL("http://eclipse.org/rap"));
				} catch (Exception e) {
					e.printStackTrace();
				}

				return;
			}
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new ProjectExplorerContentProvider());
		viewer.setLabelProvider(new ProjectExplorerLabelProvider());
		viewer.setInput(createDummyModel());
		viewer.setComparator(new ProjectExplorerViewerComparator());
		viewer.addDoubleClickListener(new DoubleClickListener());
	}
	
	/**
	 * Set the Explorer input to the given RepoResource. It is called when 
	 * the user switch to another repository
	 * @param repoResource the 
	 */
	public void setRepoModel(RepoResource repoResource){
		viewer.setInput(repoResource);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
