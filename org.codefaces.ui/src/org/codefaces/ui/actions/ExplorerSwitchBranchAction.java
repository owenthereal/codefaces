package org.codefaces.ui.actions;

import java.util.Collection;
import java.util.Set;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeEventListener;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.commands.SwitchRepositoryBranchCommandHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
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

public class ExplorerSwitchBranchAction extends Action implements IMenuCreator {

	public static final String ID = "org.codefaces.ui.actions.explorerSwitchRootAction";

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

	public ExplorerSwitchBranchAction() {
		setId(ID);
		setMenuCreator(this);
		setEnabled(false);

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

	public void run() {

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

		if (ws.getWorkingRepoBranch() != null) {
			RepoBranch currentBranch = ws.getWorkingRepoBranch();
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
				.getCommand(SwitchRepositoryBranchCommandHandler.ID);

		try {
			IParameter newBranchParam = switchBranchCmd
					.getParameter(SwitchRepositoryBranchCommandHandler.PARAM_NEW_BRANCH_ID);
			Parameterization paramNewBranch = new Parameterization(
					newBranchParam, newBranch);
			ParameterizedCommand parmCommand = new ParameterizedCommand(
					switchBranchCmd, new Parameterization[] { paramNewBranch });
			handlerService.executeCommand(parmCommand, null);
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
