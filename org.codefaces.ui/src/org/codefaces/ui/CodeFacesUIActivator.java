package org.codefaces.ui;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesUIActivator extends AbstractUIPlugin {

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
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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

	/**
	 * Initialize the image registry
	 */
    protected void initializeImageRegistry(ImageRegistry registry) {
    	putImageInRegistry(registry, "IMG_BRANCHES", "icons/branches.gif");
    }
    
    /**
     * Put an image into the image registry
     * @param registry the image registry
     * @param imgID a image ID
     * @param path the path of the image. e.g. icons/abc.gif
     */
	private void putImageInRegistry(ImageRegistry registry,
			final String imgID, final String path) {
		registry.put(imgID, AbstractUIPlugin.imageDescriptorFromPlugin(
				PLUGIN_ID, path));
    }

}
