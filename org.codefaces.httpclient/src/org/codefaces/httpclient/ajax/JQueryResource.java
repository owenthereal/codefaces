package org.codefaces.httpclient.ajax;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class JQueryResource implements IResource {
	private static final String RESOURCE_LOCATION =
		"http://code.jquery.com/jquery-1.4.2.min.js";
	
	@Override
	public String getCharset() {
		return "ISO-8859-1";
	}

	@Override
	public ClassLoader getLoader() {
		return this.getClass().getClassLoader();
	}

	@Override
	public String getLocation() {
		return RESOURCE_LOCATION;
	}

	@Override
	public RegisterOptions getOptions() {
		return RegisterOptions.VERSION;
	}

	@Override
	public boolean isExternal() {
		return true;
	}

	@Override
	public boolean isJSLibrary() {
		return true;
	}
}
