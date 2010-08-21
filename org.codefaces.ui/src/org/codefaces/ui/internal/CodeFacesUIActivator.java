package org.codefaces.ui.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.codefaces.ui.internal.codeLanguages.CodeLanguages;
import org.codefaces.ui.internal.wizards.SCMConnectorUIManager;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.ui";

	// The shared instance
	private static CodeFacesUIActivator plugin;

	private CodeLanguages langs;

	private SCMConnectorUIManager connectorUIManager;

	/**
	 * The constructor
	 */
	public CodeFacesUIActivator() {
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
		langs = new CodeLanguages();
		connectorUIManager = new SCMConnectorUIManager();
	}

	public CodeLanguages getCodeLanguages() {
		return langs;
	}

	public SCMConnectorUIManager getConnectorUIManager() {
		return connectorUIManager;
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
	public static CodeFacesUIActivator getDefault() {
		return plugin;
	}

	/**
	 * Initialize the image registry
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		putImageInRegistry(registry, Images.IMG_BRANCHES, "icons/branches.gif");
		putImageInRegistry(registry, Images.IMG_ERRORS, "icons/errors.gif");
		putImageInRegistry(registry, Images.IMG_REPO_FOLDER_ROOT,
				"icons/repo_folder_root.gif");
		putImageInRegistry(registry, Images.IMG_NAVIGATOR,
				"icons/navigator.gif");
		putImageInRegistry(registry, Images.IMG_WELCOME, "icons/welcome.gif");
		putImageInRegistry(registry, Images.IMG_CONNECTION,
				"icons/connection.gif");
		putImageInRegistry(registry, Images.IMG_FAVICON_48,
				"icons/favicon_48.png");
		putImageInRegistry(registry, Images.IMG_REPOSITORY,
				"icons/repository.gif");
	}

	/**
	 * Read the template file and return it as a string
	 * 
	 * @param path
	 *            the path of the template
	 * @return a string representation of that file
	 * @throws IOException
	 * @throws IOException
	 *             if there is IO problem
	 */
	public String readFileContent(String path) throws IOException {
		InputStream stream = FileLocator.openStream(getBundle(),
				new Path(path), false);
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(stream, writer);
		} finally {
			stream.close();
			writer.close();
		}
		return writer.toString();
	}

	/**
	 * Put an image into the image registry
	 * 
	 * @param registry
	 *            the image registry
	 * @param imgID
	 *            a image ID
	 * @param path
	 *            the path of the image. e.g. icons/abc.gif
	 */
	private void putImageInRegistry(ImageRegistry registry, final String imgID,
			final String path) {
		registry.put(imgID,
				AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path));
	}

}
