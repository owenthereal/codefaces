package org.codefaces.ui.perspectives;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class CodeFacesWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public String getInitialWindowPerspectiveId() {
		return CodeFacesPerspectiveFactory.ID;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new CodeFacesWorkbenchWindowAdvistor(configurer);
	}
}
