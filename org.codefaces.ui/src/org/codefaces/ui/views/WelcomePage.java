package org.codefaces.ui.views;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class WelcomePage extends ViewPart {
	public static final String ID = "org.codefaces.ui.view.welcomePage";

	private static final String WELCOME_PAGE_URL = getWelcomePageUrl();

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

	private static String getWelcomePageUrl() {
		String alias = "";

		IConfigurationElement[] configElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.equinox.http.registry", "resources");
		for (IConfigurationElement configElement : configElements) {
			String baseName = configElement.getAttribute("base-name");
			if (StringUtils.equals(baseName, "public")) {
				alias = configElement.getAttribute("alias");
				break;
			}
		}

		return alias + "/welcome.html";
	}

}
