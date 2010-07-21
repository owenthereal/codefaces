package org.codefaces.ui.internal.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeListener;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.internal.commands.CommandExecutor;
import org.codefaces.ui.internal.commands.SwitchBranchCommandHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class SwitchBranchAction extends Action implements IMenuCreator,
		WorkspaceChangeListener {

	private static final String TOOLTIP_TEXT = "Switch to another branch";

	public static final String ID = "org.codefaces.ui.actions.switchBranchAction";

	private Menu menu;

	private Workspace workspace;

	private class BranchMenuItemSelectionListener extends SelectionAdapter {
		private MenuItem currentSelectedMenu;

		public void widgetSelected(SelectionEvent e) {
			MenuItem newSelectedMenu = (MenuItem) e.widget;
			if (currentSelectedMenu != newSelectedMenu) {
				if (currentSelectedMenu != null) {
					currentSelectedMenu.setSelection(false);
				}

				setSelected(newSelectedMenu);
				executeSwitchRepoBranchCommand((RepoResource) currentSelectedMenu
						.getData());
			}
		}

		public void setSelected(MenuItem curSelected) {
			this.currentSelectedMenu = curSelected;
			this.currentSelectedMenu.setSelection(true);
		}
	}

	public SwitchBranchAction() {
		setId(ID);
		setMenuCreator(this);
		setToolTipText(TOOLTIP_TEXT);

		workspace = Workspace.getCurrent();
		workspace.addWorkspaceChangeListener(this);
		setEnabled(workspace.getWorkingRepoRoot() == null ? false : true);
	}

	// set the style to drop down
	@Override
	public int getStyle() {
		return AS_DROP_DOWN_MENU;
	}

	/**
	 * Toggle to the next branch in the repository
	 */
	@Override
	public void run() {
		RepoFolder currentBranch = workspace.getWorkingRepoRoot();
		if (currentBranch == null) {
			return;
		}

		List<RepoResource> branches = new ArrayList<RepoResource>(currentBranch
				.getRoot().getChildren());
		int index = branches.indexOf(currentBranch);
		RepoResource nextBranch = branches.get((index + 1) % branches.size());
		executeSwitchRepoBranchCommand(nextBranch);
	}

	@Override
	public void dispose() {
		workspace.removeWorkspaceChangeListener(this);
		if (menu != null) {
			menu.dispose();
		}
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = createMenu(parent);
		}
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

	/**
	 * Obtain the current repo and branch information from the work space. And
	 * return a new menu.
	 * 
	 * @param parent
	 *            the parent control
	 * @return a new menu
	 */
	private Menu createMenu(Control parent) {
		Menu menu = new Menu(parent);

		if (workspace.getWorkingRepoRoot() != null) {
			RepoFolder currentBranch = workspace.getWorkingRepoRoot();
			Collection<RepoResource> branches = currentBranch.getRoot()
					.getChildren();

			BranchMenuItemSelectionListener menuListener = new BranchMenuItemSelectionListener();

			for (RepoResource branch : branches) {
				MenuItem item = new MenuItem(menu, SWT.CHECK);
				item.addSelectionListener(menuListener);
				item.setText(branch.getName());
				item.setData(branch);

				if (ObjectUtils.equals(currentBranch, branch)) {
					menuListener.setSelected(item);
				}
			}
		}

		return menu;
	}

	/**
	 * Execute the switch Repository Branch command
	 * 
	 * @param branch
	 *            the new selected branch
	 */
	private void executeSwitchRepoBranchCommand(RepoResource branch) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SwitchBranchCommandHandler.PARAM_BRANCH_ID,
				branch.getId());

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(SwitchBranchCommandHandler.VARIABLE_BRANCH, branch);

		CommandExecutor.execute(SwitchBranchCommandHandler.ID, parameterMap,
				variableMap);
	}

	@Override
	public void workspaceChanged(WorkspaceChangeEvent evt) {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}

		setEnabled(workspace.getWorkingRepoRoot() == null ? false : true);
	}

}
