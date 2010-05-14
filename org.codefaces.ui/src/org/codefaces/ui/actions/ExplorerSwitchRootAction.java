package org.codefaces.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class ExplorerSwitchRootAction extends Action implements IMenuCreator{

	public static final String ID = "org.codefaces.ui.actions.explorerSwitchRootAction";

	public ExplorerSwitchRootAction() {
		setId(ID);
	}
	
	public void run(){
		
	}
	
	public void dispose(){
		
	}

	@Override
	public Menu getMenu(Control parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}
}
