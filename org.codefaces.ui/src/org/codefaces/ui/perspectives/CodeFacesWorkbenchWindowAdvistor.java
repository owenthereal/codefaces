package org.codefaces.ui.perspectives;

import org.codefaces.httpclient.ajax.AjaxClientWidget;
import org.codefaces.ui.lookandfeel.WindowComposer;
import org.codefaces.ui.views.WelcomePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class CodeFacesWorkbenchWindowAdvistor extends WorkbenchWindowAdvisor {

	public CodeFacesWorkbenchWindowAdvistor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);
		getWindowConfigurer().setShellStyle(SWT.NO_TRIM | SWT.TITLE);
	}

	@Override
	public void postWindowCreate() {
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setMaximized(true);
	}

	@Override
	public void postWindowOpen() {
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWindow.getActivePage();
		try {
			activePage.showView(WelcomePage.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		// for building the widget
		AjaxClientWidget.getCurrent();
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new CodeFacesActionBarAdvisor(configurer);
	}
	
	/**
	 * This method override the default look and feel
	 */
	//@Override
	//public void createWindowContents(Shell shell) {
	//	WindowComposer composer = new WindowComposer();
	//	composer.createWindowContent(shell, getWindowConfigurer());
	//}
}
