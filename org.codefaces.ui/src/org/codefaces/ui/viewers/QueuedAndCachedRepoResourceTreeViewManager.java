package org.codefaces.ui.viewers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.ExceptionListener;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.widgets.Display;

class QueuedAndCachedRepoResourceTreeViewManager {
	private final List<ExceptionListener> exceptionListeners;
	
	private final TreeViewer viewer;

	private Display display;

	private Map<Object, Object> loadedResources = new ConcurrentHashMap<Object, Object>();

	private BlockingQueue<RepoResource> waitingQueue = new LinkedBlockingQueue<RepoResource>();

	private RepoResourceLoadingJob loadingJob;

	private class RepoResourceLoadingJob extends Job {
		private static final String JOB_NAME = "SCMCommunicationJob";

		public RepoResourceLoadingJob() {
			super(JOB_NAME);
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			RepoResource resource = null;
			try {
				while ((resource = waitingQueue.take()) != null) {
					try {
						loadResource(resource);
					} catch (final Exception e) {
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								notifyExceptionListeners(e);								
							}	
						});
					}
				}
			} catch (InterruptedException e) {
				@SuppressWarnings("null")
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when loading resource "
								+ resource.getFullPath().toString(), e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}

			return Status.OK_STATUS;
		}

		private void loadResource(final RepoResource resource) {
			UICallBack.runNonUIThreadWithFakeContext(display, new Runnable() {
				@Override
				public void run() {
					resource.getChildren();
				}
			});

			display.syncExec(new Runnable() {
				@Override
				public void run() {
					try {
						loadedResources.put(resource, resource);
						viewer.refresh(resource, true);
					} catch (Exception e) {
						IStatus status = new Status(Status.ERROR,
								CodeFacesUIActivator.PLUGIN_ID,
								"Errors occurs when getting children "
										+ resource.getFullPath().toString(), e);
						CodeFacesUIActivator.getDefault().getLog().log(status);
					}
				}
			});
		}
	}

	/**
	 * Notify the exception listeners that an exception is caught
	 * @param e
	 *            the exception
	 */
	private void notifyExceptionListeners(Exception e){
		for(ExceptionListener listener : exceptionListeners){
			listener.exceptionThrown(e);
		}
	}

	/**
	 * This manager runs the operation asynchronously, client should use this
	 * use this interface to get notified if any exception is caught
	 */
	public void addExceptionListener(ExceptionListener exceptionListener){
		exceptionListeners.add(exceptionListener);
	}
	
	/**
	 * remove the given exception listener
	 */
	public void removeExceptionListener(ExceptionListener exceptionListener){
		exceptionListeners.remove(exceptionListener);
	}


	public QueuedAndCachedRepoResourceTreeViewManager(TreeViewer treeView) {
		this.viewer = treeView;
		exceptionListeners = new CopyOnWriteArrayList<ExceptionListener>();
		display = treeView.getControl().getDisplay();
		loadingJob = new RepoResourceLoadingJob();
		loadingJob.schedule();
	}

	public void dispose() {
		if (loadingJob.getState() != Job.NONE) {
			loadingJob.cancel();
		}
	}

	public Object[] getElement(Object parent) {
		if (!(parent instanceof RepoResource)) {
			return new Object[0];
		}

		RepoResource resource = (RepoResource) parent;
		if (!loadedResources.containsKey(resource)) {
			try {
				waitingQueue.put(resource);
			} catch (InterruptedException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when loading resource "
								+ resource.getFullPath().toString(), e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}

			return new Object[] { new LoadingItem() };
		}

		return resource.getChildren().toArray();
	}

	
}
