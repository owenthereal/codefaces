package org.codefaces.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeListener;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.commands.SwitchBranchCommandHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

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
				executeSwitchRepoBranchCommand((RepoBranch) currentSelectedMenu
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
		setEnabled(workspace.getWorkingBranch() == null ? false : true);
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
		RepoBranch currentBranch = workspace.getWorkingBranch();
		if (currentBranch == null) {
			return;
		}

		List<RepoBranch> branches = new ArrayList<RepoBranch>(currentBranch
				.getRepo().getBranches());
		int index = branches.indexOf(currentBranch);
		RepoBranch nextBranch = branches.get((index + 1) % branches.size());
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

		if (workspace.getWorkingBranch() != null) {
			RepoBranch currentBranch = workspace.getWorkingBranch();
			Collection<RepoBranch> branches = currentBranch.getRepo()
					.getBranches();

			BranchMenuItemSelectionListener menuListener = new BranchMenuItemSelectionListener();

			for (RepoBranch branch : branches) {
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
	private void executeSwitchRepoBranchCommand(RepoBranch branch) {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		ICommandService cmdService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		Command switchBranchCmd = cmdService
				.getCommand(SwitchBranchCommandHandler.ID);

		try {
			IParameter newBranchParam = switchBranchCmd
					.getParameter(SwitchBranchCommandHandler.PARAM_BRANCH_ID);
			Parameterization paramNewBranch = new Parameterization(
					newBranchParam, branch.getId());
			ParameterizedCommand parmCommand = new ParameterizedCommand(
					switchBranchCmd, new Parameterization[] { paramNewBranch });

			ExecutionEvent switchBranchEvent = handlerService
					.createExecutionEvent(parmCommand, null);
			((IEvaluationContext) switchBranchEvent.getApplicationContext())
					.addVariable(SwitchBranchCommandHandler.VARIABLE_BRANCH,
							branch);

			switchBranchCmd.executeWithChecks(switchBranchEvent);
		} catch (CommandException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when switching to branch "
							+ branch.getName(), e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}
	}

	@Override
	public void workspaceChanged(WorkspaceChangeEvent evt) {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}

		setEnabled(workspace.getWorkingBranch() == null ? false : true);
	}

}
