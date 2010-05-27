package org.codefaces.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class WelcomePage extends ViewPart {
	public static final String ID = "org.codefaces.ui.view.welcomePage";

	private static final String WELCOME_PAGE_URL = "/welcome.html";

	private Browser browser;

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(WELCOME_PAGE_URL);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

}
