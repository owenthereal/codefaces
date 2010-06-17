package org.codefaces.ui.lookandfeel.spica;


import org.codefaces.ui.lookandfeel.spica.managers.MenuBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.CoolBarManager2;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.rap.ui.interactiondesign.IWindowComposer;
import org.eclipse.rap.ui.interactiondesign.PresentationFactory;
import org.eclipse.swt.SWT;

@SuppressWarnings("restriction")
public class SpicaPresentationFactory extends PresentationFactory {

	public SpicaPresentationFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MenuManager createMenuBarManager() {
		// TODO Auto-generated method stub
		return new MenuBarManager();
	}

	@Override
	public MenuManager createPartMenuManager() {
		// TODO Auto-generated method stub
		return new MenuManager();
	}

	@Override
	public ICoolBarManager2 createCoolBarManager() {
		return new CoolBarManager2(SWT.FLAT );
	}

	@Override
	public IToolBarManager2 createToolBarManager() {
		return new ToolBarManager2(SWT.FLAT | SWT.RIGHT);
	}

	@Override
	public IToolBarManager2 createViewToolBarManager() {
		return new ToolBarManager2(SWT.FLAT | SWT.RIGHT | SWT.WRAP);
	}

	@Override
	public IToolBarContributionItem createToolBarContributionItem(
			IToolBarManager toolBarManager, String id) {
		return new ToolBarContributionItem2(toolBarManager, id);
	}

	@Override
	public IWindowComposer createWindowComposer() {
		return new SpicaWindowComposer();
	}


}
