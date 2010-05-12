package org.codefaces.ui.perspectives;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
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
		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);
		getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
	}

	@Override
	public void postWindowCreate() {
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setMaximized(true);
	}
	
	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new CodeFacesActionBarAdvisor(configurer);
	}
}
