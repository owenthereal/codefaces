package org.codefaces.httpclient.ajax;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class JsonHelperResource implements IResource {

	private static final String RESOURCE_LOCATION =
		"org/codefaces/httpclient/ajax/json2.js";
	
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
		return false;
	}

	@Override
	public boolean isJSLibrary() {
		return true;
	}
}
