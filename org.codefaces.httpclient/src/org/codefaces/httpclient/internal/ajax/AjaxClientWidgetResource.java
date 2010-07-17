package org.codefaces.httpclient.internal.ajax;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class AjaxClientWidgetResource implements IResource {
	private static final String RESOURCE_LOCATION =
		"org/codefaces/httpclient/internal/ajax/ajax_client_widget.js";
	
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
		return RegisterOptions.VERSION_AND_COMPRESS;
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
