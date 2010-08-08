package org.codefaces.ui.internal.commons;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoWorkspace;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.widgets.Display;

class RepoResourceContentProviderManager {
	private final TreeViewer viewer;

	private Display display;

	private Map<Object, Object> loadedResources = new ConcurrentHashMap<Object, Object>();

	private BlockingQueue<RepoResource> waitingQueue = new LinkedBlockingQueue<RepoResource>();

	private RepoResourceLoadingJob loadingJob;

	private class RepoResourceLoadingJob extends Job {
		private static final String JOB_NAME = "Repository resource fetching";

		public RepoResourceLoadingJob() {
			super(JOB_NAME);
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			RepoResource resource = null;
			try {
				while ((resource = waitingQueue.take()) != null) {
					loadResource(resource);
				}
			} catch (InterruptedException e) {
				@SuppressWarnings("null")
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when loading resource "
								+ resource.getPath().toString(), e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}

			return Status.OK_STATUS;
		}

		private void loadResource(final RepoResource resource) {
			// running get children in session;
			UICallBack.runNonUIThreadWithFakeContext(display, new Runnable() {
				@Override
				public void run() {
					resource.getChildren();
					loadedResources.put(resource, resource);
				}
			});

			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						if (!viewer.getControl().isDisposed()) {
							viewer.refresh(resource, true);
						}
					} catch (Exception e) {
						IStatus status = new Status(Status.ERROR,
								CodeFacesUIActivator.PLUGIN_ID,
								"Errors occurs when getting children "
										+ resource.getPath().toString(), e);
						CodeFacesUIActivator.getDefault().getLog().log(status);
					}
				}
			});
		}
	}

	public RepoResourceContentProviderManager(TreeViewer treeView) {
		this.viewer = treeView;
		display = viewer.getControl().getDisplay();
		loadingJob = new RepoResourceLoadingJob();
		loadingJob.schedule();
	}

	public void dispose() {
		if (loadingJob.getState() != Job.NONE) {
			loadingJob.cancel();
		}
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof RepoResource) {
			return ((RepoResource) parent).hasChildren();
		}

		return false;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof RepoWorkspace) {
			return getChildrenForWorkspace((RepoWorkspace) parent);
		}

		if (parent instanceof RepoResource) {
			return getChildrenForResource((RepoResource) parent);
		}

		return new Object[0];
	}

	private Object[] getChildrenForWorkspace(RepoWorkspace workspace) {
		return workspace.getProjects().toArray();
	}

	private Object[] getChildrenForResource(RepoResource resource) {
		if (!loadedResources.containsKey(resource)) {
			try {
				waitingQueue.put(resource);
			} catch (InterruptedException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when loading resource "
								+ resource.getPath().toString(), e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}

			return new Object[] { new LoadingItem() };
		}

		return resource.getChildren().toArray();
	}

}
