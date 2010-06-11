package org.codefaces.ui.views;

import org.antlr.stringtemplate.StringTemplate;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.codeLanguages.CodeLanguage;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class CodeExplorerViewPart extends ViewPart {
	public static final String ID = "org.codefaces.ui.view.codeExplorer";
	public static final String STATUS_MSG_PATTERN = "[ $branch$@$repo$ ] - $path$";

	private Browser browser;
	private String defaultStatusMsg;

	@Override
	public void createPartControl(final Composite parent) {
		browser = new Browser(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
		showStatusMessage(defaultStatusMsg);
	}
	
	/**
	 * Display a status message in the status bar
	 */
	public void showStatusMessage(String msg){
		IStatusLineManager statusline = getViewSite().getActionBars()
				.getStatusLineManager();
		if(msg != null) statusline.setMessage(msg);
	}

	public void setInput(RepoFile repoFile) {
		setPartName(repoFile.getName());
		
		CodeLanguage language = CodeFacesUIActivator.getDefault()
				.getCodeLanguages().parseFileName(repoFile.getName());
		String langName = language.getName();
		String resourceURL = language.getResource();
		
		CodeExplorerHTMLTemplate template = new CodeExplorerHTMLTemplate(
				repoFile.getName(), langName, resourceURL, repoFile
						.getContent());

		browser.setText(template.toHTML());
		
		//Construct string as status msg
		StringBuffer path = new StringBuffer();
		RepoResource fileRepo = null;
		RepoResource fileBranch = null;
		for (RepoResource p : repoFile.getParents()) {
			if(p instanceof Repo){ fileRepo = p; }
			else if(p instanceof RepoBranch){ fileBranch = p;}
			else if(!(p instanceof RepoFolderRoot)){ //construct the file path
				path.append(p.getName()+ "/");
			}
		}
		path.append(repoFile.getName());
		
		StringTemplate statusTemplate = new StringTemplate(STATUS_MSG_PATTERN);
		if(fileBranch != null){
			statusTemplate.setAttribute("branch", fileBranch.getName());
		}
		if(fileRepo != null){
			statusTemplate.setAttribute("repo", fileRepo.getName());
		}
		statusTemplate.setAttribute("path", path.toString());
		defaultStatusMsg = statusTemplate.toString();
		
		showStatusMessage(defaultStatusMsg);
	}
}
