package org.codefaces.ui.internal.editors;

public class WelcomeEditor extends BrowserEditor {
	public static final String ID = "org.codefaces.ui.editor.welcomeEditor";

	private static final String WELCOME_PAGE_URL = "http://blog.codefaces.org/";

	public WelcomeEditor() {
		super(WELCOME_PAGE_URL);
		setTitleToolTip("Welcome");
	}
}
