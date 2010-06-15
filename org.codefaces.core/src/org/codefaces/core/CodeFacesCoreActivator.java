package org.codefaces.core;

import org.codefaces.core.services.RepoService;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesCoreActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.core";

	// The shared instance
	private static CodeFacesCoreActivator plugin;

	private RepoService repoService;

	private ServiceRegistration registration;

	/**
	 * The constructor
	 */
	public CodeFacesCoreActivator() {
		repoService = new RepoService();
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
		registration = context.registerService(RepoService.class.getName(), repoService, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		repoService.dispose();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CodeFacesCoreActivator getDefault() {
		return plugin;
	}

	public RepoService getRepoService() {
		return repoService;
	}
}
