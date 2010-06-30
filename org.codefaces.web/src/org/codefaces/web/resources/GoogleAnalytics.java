package org.codefaces.web.resources;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class GoogleAnalytics implements IResource {
	private static final String RESOURCE_LOCATION =
		"org/codefaces/web/resources/google_analytics.js";
	
	@Override
	public ClassLoader getLoader() {
		return this.getClass().getClassLoader();
	}

	@Override
	public String getLocation() {
		return RESOURCE_LOCATION;
	}

	@Override
	public String getCharset() {
		return "ISO-8859-1";
	}

	@Override
	public RegisterOptions getOptions() {
		return RegisterOptions.VERSION_AND_COMPRESS;
	}

	@Override
	public boolean isJSLibrary() {
		return true;
	}

	@Override
	public boolean isExternal() {
		return false;
	}

}
