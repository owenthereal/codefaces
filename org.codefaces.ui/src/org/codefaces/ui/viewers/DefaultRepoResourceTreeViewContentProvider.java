package org.codefaces.ui.viewers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.ExceptionListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class DefaultRepoResourceTreeViewContentProvider implements ITreeContentProvider {
	private final List<ExceptionListener> exceptionListeners;
	
	private QueuedAndCachedRepoResourceTreeViewManager manager;
	
	/**
	 * Constructor
	 */
	public DefaultRepoResourceTreeViewContentProvider(){
		exceptionListeners = new CopyOnWriteArrayList<ExceptionListener>();
	}

	public void dispose() {
		if(manager != null){
			manager.dispose();
		}
	}
	
	/**
	 * This content provider may run the operation asynchronously, client should
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
	
	/**
	 * Notify the exception listeners that an exception is thrown
	 * @param e
	 *            the exception
	 */
	private void notifyExceptionListeners(Exception e){
		for(ExceptionListener listener : exceptionListeners){
			listener.exceptionThrown(e);
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		if(manager != null){
			manager.dispose();
		}
		manager = new QueuedAndCachedRepoResourceTreeViewManager((TreeViewer) viewer);
		//register an exception listener to the manager and notify our listeners 
		//when an exception is caught
		manager.addExceptionListener(new ExceptionListener(){
			@Override
			public void exceptionThrown(Exception e) {
				notifyExceptionListeners(e);
			}
		});
	}

	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof RepoResource) {
			return ((RepoResource) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		return manager.getElement(parent);
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof RepoResource) {
			return ((RepoResource) parent).hasChildren();
		}

		return false;
	}
}