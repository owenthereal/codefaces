package org.codefaces.ui.editors;


public class WelcomeEditor extends BrowserEditor {
	public static final String ID = "org.codefaces.ui.editor.welcomeEditor";

	private static final String WELCOME_PAGE_URL = "http://codefaces.com/";

	public WelcomeEditor(){
		super(WELCOME_PAGE_URL);
		setTitleToolTip("Welcome");
	}
}
