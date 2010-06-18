package org.codefaces.ui;


import org.codefaces.core.services.RepoService;
import org.codefaces.ui.codeLanguages.CodeLanguages;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeFacesUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codefaces.ui";

	// The shared instance
	private static CodeFacesUIActivator plugin;

	private ServiceTracker repoServiceTracker;

	private BundleContext context;

	private RepoService repoService;

	private CodeLanguages langs;

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
		this.context = context;

		repoServiceTracker = new ServiceTracker(context, RepoService.class
				.getName(), createRepoServiceCustomizer());
		repoServiceTracker.open();
		
		langs = new CodeLanguages();
	}

	public CodeLanguages getCodeLanguages() {
		return langs;
	}

	private ServiceTrackerCustomizer createRepoServiceCustomizer() {
		return new ServiceTrackerCustomizer() {
			@Override
			public void removedService(ServiceReference reference,
					Object service) {
				synchronized (CodeFacesUIActivator.this) {
					if (service == CodeFacesUIActivator.this.repoService) {
						CodeFacesUIActivator.this.repoService = null;
					}
				}
			}

			@Override
			public void modifiedService(ServiceReference reference,
					Object service) {
				// do nothing
			}

			@Override
			public Object addingService(ServiceReference reference) {
				Object service = context.getService(reference);
				synchronized (CodeFacesUIActivator.this) {
					if (CodeFacesUIActivator.this.repoService == null) {
						CodeFacesUIActivator.this.repoService = (RepoService) service;
					}
				}

				return service;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		repoServiceTracker.close();
		plugin = null;
		super.stop(context);
	}

	public RepoService getRepoService() {
		return repoService;
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
		putImageInRegistry(registry, Images.IMG_NAVIGATOR, "icons/navigator.gif");
		putImageInRegistry(registry, Images.IMG_WELCOME, "icons/welcome.gif");
		putImageInRegistry(registry, Images.IMG_CONNECTION, "icons/connection.gif");
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
		registry.put(imgID, AbstractUIPlugin.imageDescriptorFromPlugin(
				PLUGIN_ID, path));
	}

}
