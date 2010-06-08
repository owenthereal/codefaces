package org.codefaces.ui.lib.js;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class Json2Resource implements IResource {

	private static final String RESOURCE_LOCATION =
		"org/codefaces/ui/lib/js/json2.js";
	
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
		//we will do our own compression
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
