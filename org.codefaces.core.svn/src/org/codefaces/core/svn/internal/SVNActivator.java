package org.codefaces.core.svn.internal;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

/**
 * The activator class controls the plug-in life cycle
 */
public class SVNActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.core.svn"; //$NON-NLS-1$

	// The shared instance
	private static SVNActivator plugin;

	/**
	 * The constructor
	 */
	public SVNActivator() {
		 //Set up connection protocols support:
		 //http:// and https://
		 DAVRepositoryFactory.setup();
		 //svn://, svn+xxx:// (svn+ssh:// in particular)
		 SVNRepositoryFactoryImpl.setup();
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
	public static SVNActivator getDefault() {
		return plugin;
	}

}
