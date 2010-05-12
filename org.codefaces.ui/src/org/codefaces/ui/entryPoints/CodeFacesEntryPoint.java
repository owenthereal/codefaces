package org.codefaces.ui.entryPoints;

import org.codefaces.ui.perspectives.CodeFacesWorkbenchAdvisor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class CodeFacesEntryPoint implements IEntryPoint {
	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new CodeFacesWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}

}
