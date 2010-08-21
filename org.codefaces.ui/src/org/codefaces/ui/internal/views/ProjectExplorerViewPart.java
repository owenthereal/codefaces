package org.codefaces.ui.internal.views;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.core.events.WorkspaceChangedEvent;
import org.codefaces.core.events.WorkspaceChangedListener;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
import org.codefaces.core.models.RepoWorkspace;
import org.codefaces.ui.internal.StatusManager;
import org.codefaces.ui.internal.commands.CommandExecutor;
import org.codefaces.ui.internal.commands.OpenFileCommandHandler;
import org.codefaces.ui.internal.commons.RepoFolderOpenListener;
import org.codefaces.ui.internal.commons.RepoResourceComparator;
import org.codefaces.ui.internal.commons.RepoResourceContentProvider;
import org.codefaces.ui.internal.commons.RepoResourceLabelProvider;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

public class ProjectExplorerViewPart extends ViewPart {
	public static final String ID = "org.codefaces.ui.view.projectExplorer";

	private static final String VIEWER_CONTEXT_MENU_ID = ID + "#viewer";

	private TreeViewer viewer;

	private StatusManager statusManager;

	private UpdateInputListener workspaceChangedListener = new UpdateInputListener();

	private final class UpdateInputListener implements WorkspaceChangedListener {
		@Override
		public void workspaceChanged(WorkspaceChangedEvent evt) {
			viewer.refresh();
		}
	}

	private class FileOpenListener implements IOpenListener {
		@Override
		public void open(OpenEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();
			if (selection.isEmpty()
					|| !(selection.getFirstElement() instanceof RepoResource)) {
				return;
			}

			RepoResource clickedRepoResource = (RepoResource) selection
					.getFirstElement();
			if (clickedRepoResource.getType() == RepoResourceType.FILE) {
				Map<String, String> parameterMap = new HashMap<String, String>();
				parameterMap.put(OpenFileCommandHandler.PARAM_MODE,
						OpenFileCommandHandler.MODE_DIRECT_FILES);

				Map<String, Object> variableMap = new HashMap<String, Object>();
				variableMap.put(OpenFileCommandHandler.VARIABLE_FILES,
						new RepoFile[] { (RepoFile) clickedRepoResource });

				CommandExecutor.execute(OpenFileCommandHandler.ID,
						parameterMap, variableMap);
			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		createViewer(parent);
		hookWorkspaceChangedListener();

		statusManager = new StatusManager(getViewSite().getActionBars()
				.getStatusLineManager(), getViewer());

		registerContextMenu(viewer);
	}

	private void hookWorkspaceChangedListener() {
		RepoWorkspace workspce = RepoWorkspace.getCurrent();
		workspce.addWorkspaceChangeListener(workspaceChangedListener);
	}

	@Override
	public void dispose() {
		unhookWorkspaceChangedListener();
		super.dispose();
	}

	private void unhookWorkspaceChangedListener() {
		RepoWorkspace workspce = RepoWorkspace.getCurrent();
		workspce.removeWorkspaceChangeListener(workspaceChangedListener);
	}

	/**
	 * create and register a context menu associate to the explorer tree viewer
	 * 
	 * @param viewer
	 *            - the project explorer tree-viewer
	 */
	private void registerContextMenu(TreeViewer viewer) {
		MenuManager contextMenuManager = new MenuManager();
		contextMenuManager.add(new Separator(
				IWorkbenchActionConstants.MB_ADDITIONS));
		Menu menu = contextMenuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(VIEWER_CONTEXT_MENU_ID,
				contextMenuManager, viewer);
		getSite().setSelectionProvider(viewer);
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	/**
	 * Create and initialize the viewer
	 */
	private void createViewer(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new RepoResourceContentProvider());
		viewer.setLabelProvider(new RepoResourceLabelProvider());
		viewer.setComparator(new RepoResourceComparator());
		viewer.addOpenListener(new FileOpenListener());
		viewer.addOpenListener(new RepoFolderOpenListener());
		viewer.setInput(RepoWorkspace.getCurrent());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TreeViewer getViewer() {
		return viewer;
	}

}
