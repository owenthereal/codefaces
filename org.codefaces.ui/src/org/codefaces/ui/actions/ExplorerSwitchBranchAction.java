package org.codefaces.ui.actions;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.events.WorkSpaceChangeEvent;
import org.codefaces.ui.events.WorkSpaceChangeEventListener;
import org.codefaces.ui.resources.WorkSpace;
import org.codefaces.ui.resources.WorkSpaceManager;
import org.codefaces.ui.resources.WorkSpace.Resources;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

public class ExplorerSwitchBranchAction extends Action implements IMenuCreator{

	public static final String ID = "org.codefaces.ui.actions.explorerSwitchRootAction";

	private Menu menu;
	
	class BranchMenuItemSelectionListener extends SelectionAdapter{
		MenuItem currentSelectedMenu;
		
		public void widgetSelected(SelectionEvent e){
			MenuItem newSelectedMenu = (MenuItem)e.widget;
			if(currentSelectedMenu != newSelectedMenu){
				newSelectedMenu.setSelection(true);
				if(currentSelectedMenu != null){
					currentSelectedMenu.setSelection(false);
				}
				currentSelectedMenu = newSelectedMenu;
			}
			else{
				currentSelectedMenu.setSelection(true);
			}
		}
		
		public void setCurrentSelected(MenuItem curSelected){
			this.currentSelectedMenu = curSelected;
		}
	}
	
	public ExplorerSwitchBranchAction() {
		setId(ID);		
		setMenuCreator(this);
		setEnabled(false);
		
		WorkSpaceManager.getInstance().getWorkSpace()
				.addWorkSpaceChangeListener(new WorkSpaceChangeEventListener() {
					@Override
					public void workSpaceChanged(WorkSpaceChangeEvent evt) {
						if (evt.getResourcesChanged().contains(Resources.REPO)) {
							if(menu != null) menu.dispose();
							menu = null; // enforce a new menu to be created
							setEnabled(true); //repo change must come with a branch change
						}
					}
				});
		
		
	}
	
	//set the style to drop down
	public int getStyle(){
		return AS_DROP_DOWN_MENU;
	}
	
	public void run(){
		
	}

	
	@Override
	public void dispose() {
		if(menu != null) menu.dispose();
	}

	@Override
	public Menu getMenu(Control parent) {
		if(menu == null){
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
	 * @param parent the parent control
	 * @return a new menu
	 */
	private Menu createMenu(Control parent){
		Menu menu = new Menu(parent);
		WorkSpace ws = WorkSpaceManager.getInstance().getWorkSpace();
		
		if(ws.getWorkingRepo() != null){
			
			Collection<RepoBranch> branches = ws.getWorkingRepo().getBranches();
			RepoBranch currentBranch = ws.getWorkingRepoBranch();
			
			BranchMenuItemSelectionListener menuListener = 
				new BranchMenuItemSelectionListener();

			MenuItem currentBranchitem = new MenuItem(menu, SWT.CHECK);
			currentBranchitem.setText(currentBranch.getName());
			currentBranchitem.addSelectionListener(menuListener);
			currentBranchitem.setSelection(true);
			menuListener.setCurrentSelected(currentBranchitem);

			for (RepoBranch b : branches) {
				// skip the current branch
				if (b.getId().equals(currentBranch.getId()) == false) {
					MenuItem item = new MenuItem(menu, SWT.CHECK);
					item.addSelectionListener(menuListener);
					item.setText(b.getName());
				}
			}
		}
		
		return menu;
	}
}
