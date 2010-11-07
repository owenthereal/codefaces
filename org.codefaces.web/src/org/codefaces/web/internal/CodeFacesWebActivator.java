package org.codefaces.web.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codefaces.web.urls.URLParsingStrategy;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class CodeFacesWebActivator extends Plugin {
	private static final String URL_PARSING_STRATEGIES_EXTENSION_POINT = "scmUrlParsingStrategies";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.web";

	// The shared instance
	private static CodeFacesWebActivator plugin;

	private URLParsingStrategy[] urlParseStrategies;

	public CodeFacesWebActivator() {
		urlParseStrategies = retrieveUrlParsingStrategiesFromExtensionPoint()
				.toArray(new URLParsingStrategy[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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
	public static CodeFacesWebActivator getDefault() {
		return plugin;
	}

	public URLParsingStrategy getUrlParseStrategies(String url) {
		for (URLParsingStrategy strategy : urlParseStrategies) {
			if (strategy.canParse(url)) {
				return strategy;
			}
		}

		return null;
	}
	
	/**
	 * retrieve parsing strategies from extension point
	 */
	protected Collection<URLParsingStrategy> retrieveUrlParsingStrategiesFromExtensionPoint(){
		Set<URLParsingStrategy> parsingStrategies = new HashSet<URLParsingStrategy>();
		
		IConfigurationElement[] extensionPoints = Platform
		.getExtensionRegistry().getConfigurationElementsFor(
				PLUGIN_ID, URL_PARSING_STRATEGIES_EXTENSION_POINT);
		for (IConfigurationElement extensionPoint : extensionPoints) {
			try {
				URLParsingStrategy strategy = (URLParsingStrategy) extensionPoint
						.createExecutableExtension("class");
				parsingStrategies.add(strategy);
			} catch (CoreException e) {
				IStatus status = new Status(
						Status.ERROR,
						CodeFacesWebActivator.PLUGIN_ID,
						"Errors occurs when loading URL Parsing Strategy extensions",
						e);
				getLog().log(status);
			}
		}
		return parsingStrategies;
	}
}
