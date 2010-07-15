package org.codefaces.core.impl;

import org.codefaces.core.SCMManager;
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

	private SCMManager scmManager;

	/**
	 * The constructor
	 */
	public CodeFacesCoreActivator() {

	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		service = new SCMService();
		SCMConnectorManager connectorManager = new SCMConnectorManager();
		SCMOperationHandlerManager operationHandlerManager = new SCMOperationHandlerManager();
		SCMOperationDispatcher.init(connectorManager, operationHandlerManager);
		scmManager = new SCMManager(connectorManager, operationHandlerManager);
	}

	public SCMManager getSCMManager() {
		return scmManager;
	}

	public SCMService getSCMService() {
		return service;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static CodeFacesCoreActivator getDefault() {
		return plugin;
	}
}
