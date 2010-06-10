package org.codefaces.ui.views;

import org.apache.commons.lang.StringEscapeUtils;
import org.codefaces.core.models.RepoFile;
import org.codefaces.ui.CodeFacesUIActivator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CodeExplorerViewPart extends ViewPart {
	public static final String ID = "org.codefaces.ui.view.codeExplorer";

	private Browser browser;

	@Override
	public void createPartControl(final Composite parent) {
		browser = new Browser(parent, SWT.NONE);

	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	public void setInput(RepoFile repoFile) {
		setPartName(repoFile.getName());

		String langName = CodeFacesUIActivator.getDefault().getCodeLanguages()
				.parseFileName(repoFile.getName()).getName();
		CodeExplorerHTMLTemplate template = new CodeExplorerHTMLTemplate(repoFile.getName(),
				langName, langName, StringEscapeUtils.escapeHtml(repoFile.getContent()));

		browser.setText(template.toHTML());
		
	}
}
