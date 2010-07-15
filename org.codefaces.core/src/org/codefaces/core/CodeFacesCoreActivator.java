package org.codefaces.core;

import org.codefaces.core.connectors.SCMConnectorManager;
import org.codefaces.core.operations.SCMOperationDispatcher;
import org.codefaces.core.operations.SCMOperationHandlerManager;
import org.codefaces.core.services.SCMService;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesCoreActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.core";

	// The shared instance
	private static CodeFacesCoreActivator plugin;

	private SCMService service;

	private SCMConnectorManager connectorManager;

	private SCMOperationHandlerManager operationHandlerManager;

	/**
	 * The constructor
	 */
	public CodeFacesCoreActivator() {

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
		service = new SCMService();
		connectorManager = new SCMConnectorManager();
		operationHandlerManager = new SCMOperationHandlerManager();
		SCMOperationDispatcher.init(connectorManager, operationHandlerManager);
	}

	public SCMService getSCMService() {
		return service;
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
	public static CodeFacesCoreActivator getDefault() {
		return plugin;
	}
}
