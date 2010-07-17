package org.codefaces.web.internal;

import java.util.ArrayList;
import java.util.List;

import org.codefaces.web.internal.urls.URLParsingStrategy;
import org.codefaces.web.internal.urls.github.GitHubUrlParseStrategy;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class CodeFacesWebActivator extends Plugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.core";

	// The shared instance
	private static CodeFacesWebActivator plugin;

	private List<URLParsingStrategy> urlParseStrategies;

	public CodeFacesWebActivator() {
		urlParseStrategies = new ArrayList<URLParsingStrategy>();
		urlParseStrategies.add(new GitHubUrlParseStrategy());
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

	public URLParsingStrategy getUrlParseStrategy(String url) {
		for (URLParsingStrategy strategy : urlParseStrategies) {
			if (strategy.canParse(url)) {
				return strategy;
			}
		}

		return null;
	}
}
