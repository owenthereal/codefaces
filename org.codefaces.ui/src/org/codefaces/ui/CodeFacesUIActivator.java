package org.codefaces.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesUIActivator extends AbstractUIPlugin {
	private static final String CODE = "@{code}";

	private static final String LANG = "@{lang}";

	private static final String EDITOR_TEMPLATE_PATH = "web/code_template.html";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.ui";

	// The shared instance
	private static CodeFacesUIActivator plugin;

	/**
	 * The constructor
	 */
	public CodeFacesUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CodeFacesUIActivator getDefault() {
		return plugin;
	}

	public String getCodeHTML(String lang, String code) {
		try {
			InputStream inputStream = FileLocator.openStream(getBundle(),
					new Path(EDITOR_TEMPLATE_PATH), false);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}

			int langInsertIndex = builder.indexOf(LANG);
			builder.replace(langInsertIndex, langInsertIndex + LANG.length(),
					lang);

			int codeInsertIndex = builder.indexOf(CODE);
			builder.replace(codeInsertIndex, codeInsertIndex + CODE.length(),
					code);

			return builder.toString();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
}
