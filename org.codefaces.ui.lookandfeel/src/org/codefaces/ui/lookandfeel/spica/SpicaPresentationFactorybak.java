package org.codefaces.ui.lookandfeel.spica;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.CoolBarManager2;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.swt.SWT;
import org.eclipse.ui.internal.provisional.presentations.IActionBarPresentationFactory;
import org.eclipse.ui.presentations.WorkbenchPresentationFactory;

@SuppressWarnings("restriction")
public class SpicaPresentationFactorybak extends WorkbenchPresentationFactory
		implements IActionBarPresentationFactory {

	@Override
	public ICoolBarManager2 createCoolBarManager() {
		return new CoolBarManager2(SWT.FLAT);
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

}
