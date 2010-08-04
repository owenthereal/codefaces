package org.codefaces.ui.internal.perspectives;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.ui.internal.commands.CommandExecutor;
import org.codefaces.ui.internal.commands.OpenEditorHandler;
import org.codefaces.ui.internal.commands.OpenRepoFromURLCommandHandler;
import org.codefaces.ui.internal.editors.WelcomeEditor;
import org.eclipse.rwt.RWT;
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
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(false);
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
		showWelcomeEditor();
		tryOpenRepositoryFromURL();
	}

	private void tryOpenRepositoryFromURL() {
		String svnKind = RWT.getRequest().getParameter(
				SCMURLConfiguration.urlEncode(SCMConfigurableElement.SCM_KIND));
		String repoUrl = RWT.getRequest().getParameter(
				SCMURLConfiguration.urlEncode(SCMConfigurableElement.REPO_URL));
		
		if(svnKind != null && repoUrl != null){
			@SuppressWarnings("unchecked")
			Map<String, String[]> urlParameters = RWT.getRequest()
					.getParameterMap();
			try {
				SCMURLConfiguration configurations = SCMURLConfiguration
						.fromHTTPParametersMap(urlParameters);
				Map<String, Object> variableMap = new HashMap<String, Object>();
				variableMap.put(OpenRepoFromURLCommandHandler.VARIABLE_SCM_URL_CONFIGUTATION,
								configurations);

				CommandExecutor.execute(OpenRepoFromURLCommandHandler.ID, null,
						variableMap);

			} catch (Exception e) {
				// ignore it if there is any error in the parameter,
				// as user can type anything they like
			}
		}
	}

	private void showWelcomeEditor() {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put(OpenEditorHandler.PARAMETER_EDITOR_ID, WelcomeEditor.ID);
		CommandExecutor.execute(OpenEditorHandler.ID, paraMap, null);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new CodeFacesActionBarAdvisor(configurer);
	}
}
