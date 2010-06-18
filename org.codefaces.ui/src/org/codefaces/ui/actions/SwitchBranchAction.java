package org.codefaces.ui.actions;

import java.util.Collection;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeEventListener;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.commands.SwitchBranchCommandHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.CommandException;
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

public class SwitchBranchAction extends Action implements IMenuCreator {

	private static final String TOOLTIP_TEXT = "Switch to another branch";

	public static final String ID = "org.codefaces.ui.actions.switchBranchAction";

	private Menu menu;

	class BranchMenuItemSelectionListener extends SelectionAdapter {
		MenuItem currentSelectedMenu;

		public void widgetSelected(SelectionEvent e) {
			MenuItem newSelectedMenu = (MenuItem) e.widget;
			if (currentSelectedMenu != newSelectedMenu) {
				newSelectedMenu.setSelection(true);
				if (currentSelectedMenu != null) {
					currentSelectedMenu.setSelection(false);
				}
				currentSelectedMenu = newSelectedMenu;
				executeSwitchRepoBranchCommand(currentSelectedMenu.getText());
			} else {
				currentSelectedMenu.setSelection(true);
			}
		}

		public void setCurrentSelected(MenuItem curSelected) {
			this.currentSelectedMenu = curSelected;
		}
	}

	public SwitchBranchAction() {
		setId(ID);
		setMenuCreator(this);
		setEnabled(false);
		setToolTipText(TOOLTIP_TEXT);

		Workspace.getCurrent().addWorkSpaceChangeEventListener(
				new WorkspaceChangeEventListener() {
					@Override
					public void workspaceChanged(WorkspaceChangeEvent evt) {
						if (menu != null) {
							menu.dispose();
						}
						menu = null; // enforce a new menu to be
						// created
						setEnabled(true); // repo change must come
						// with a branch change
					}
				});

	}

	// set the style to drop down
	public int getStyle() {
		return AS_DROP_DOWN_MENU;
	}

	/**
	 * Toggle to the next branch in the repository
	 */
	public void run() {

		Workspace ws = Workspace.getCurrent();

		if (ws.getWorkingBranch() != null) {
			RepoBranch currentBranch = ws.getWorkingBranch();
			Collection<RepoBranch> branches = currentBranch.getRepo()
					.getBranches();

			if (branches.size() == 1) {
				return;
			}// do nothing

			RepoBranch[] branchArray = branches.toArray(new RepoBranch[0]);
			RepoBranch nextBranch = null;
			for (int i = 0; i < branchArray.length; i++) {
				if (branchArray[i].getId().equals(currentBranch.getId())) {
					nextBranch = (i == branchArray.length - 1) ? branchArray[0]
							: branchArray[i + 1];
					break;
				}
			}

			if (nextBranch != null) {
				executeSwitchRepoBranchCommand(nextBranch.getName());
			}
		}
	}

	@Override
	public void dispose() {
		if (menu != null)
			menu.dispose();
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
		Workspace ws = Workspace.getCurrent();

		if (ws.getWorkingBranch() != null) {
			RepoBranch currentBranch = ws.getWorkingBranch();
			Collection<RepoBranch> branches = currentBranch.getRepo()
					.getBranches();

			BranchMenuItemSelectionListener menuListener = new BranchMenuItemSelectionListener();

			MenuItem currentBranchitem = new MenuItem(menu, SWT.CHECK);
			currentBranchitem.setText(currentBranch.getName());
			currentBranchitem.addSelectionListener(menuListener);
			currentBranchitem.setSelection(true);
			menuListener.setCurrentSelected(currentBranchitem);

			for (RepoBranch branch : branches) {
				// skip the current branch
				if (!currentBranch.equals(branch)) {
					MenuItem item = new MenuItem(menu, SWT.CHECK);
					item.addSelectionListener(menuListener);
					item.setText(branch.getName());
				}
			}
		}

		return menu;
	}

	/**
	 * Execute the switch Repository Branch command
	 * 
	 * @param newBranch
	 *            the new selected branch
	 */
	private void executeSwitchRepoBranchCommand(String newBranch) {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		ICommandService cmdService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);

		Command switchBranchCmd = cmdService
				.getCommand(SwitchBranchCommandHandler.ID);

		try {
			IParameter newBranchParam = switchBranchCmd
					.getParameter(SwitchBranchCommandHandler.PARAM_NEW_BRANCH_ID);
			Parameterization paramNewBranch = new Parameterization(
					newBranchParam, newBranch);
			ParameterizedCommand parmCommand = new ParameterizedCommand(
					switchBranchCmd, new Parameterization[] { paramNewBranch });
			handlerService.executeCommand(parmCommand, null);
		} catch (CommandException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when switching to branch " + newBranch, e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}
	}

}
