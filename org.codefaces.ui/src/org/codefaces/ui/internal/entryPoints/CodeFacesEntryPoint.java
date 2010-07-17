package org.codefaces.ui.internal.entryPoints;

import org.codefaces.ui.internal.perspectives.CodeFacesWorkbenchAdvisor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class CodeFacesEntryPoint implements IEntryPoint {
	public static final String ID = "org.codefaces.ui.entrypoint.codefaces";

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new CodeFacesWorkbenchAdvisor();

		return PlatformUI.createAndRunWorkbench(display, advisor);
	}

}
