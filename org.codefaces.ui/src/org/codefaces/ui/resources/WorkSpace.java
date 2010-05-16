package org.codefaces.ui.resources;

import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.events.WorkSpaceChangeEvent;
import org.codefaces.ui.events.WorkSpaceChangeEventListener;

/**
 * 
 * Ref: <Effective Java> Item 67
 * Remark: I don't think we need to use Executor at this content
 * 
 * @author kklo
 */
public class WorkSpace{
	
	public enum Resources {
		REPO, BRANCH
	}

	private Repo workingRepo;
	private RepoBranch workingRepoBranch;
	
	private final List<WorkSpaceChangeEventListener> changeListeners;
	private final Object lock = new Object();//a lock object

	/**
	 * Note: should obtain a WorkSpace via
	 * WorkSpaceManager.getInstance().getWorkSpace() method.
	 */
	public WorkSpace(){
		workingRepo = null;
		workingRepoBranch = null;
		//CopyOnWriteArrayList is ideal for implementing observer pattern
		changeListeners = new CopyOnWriteArrayList<WorkSpaceChangeEventListener>();
	}
	
	public Repo getWorkingRepo(){
		return workingRepo;
	}
	
	public RepoBranch getWorkingRepoBranch(){
		return workingRepoBranch;
	}
	
	
	/**
	 * Change the current working repository branch without changing the
	 * working repository.
	 * 
	 * @param newRepoBranch a new repository branch
	 */
	public void update(RepoBranch newRepoBranch){
		WorkSpaceChangeEvent evt;
		synchronized(lock){
			//TODO: use the getRepo() method in the new Repo model
			if(!newRepoBranch.getParent().getName().equals(workingRepo.getName())){
				// if the new repobranch's repo is not the same as current repo,
				// the update is outdated. Discard the update
				return;
			}
			workingRepoBranch = newRepoBranch;
			evt = new WorkSpaceChangeEvent(new EventObject(this), newRepoBranch);
		}
		notifyChange(evt);
	}
	
	/**
	 * Change the current working repository and branch. Please notice that
	 * any changes in repository must accompanied by a branch changes
	 *  
	 * @param newRepo a new repository
	 * @param newRepoBranch a new repository branch
	 */
	public void update(Repo newRepo, RepoBranch newRepoBranch){
		WorkSpaceChangeEvent evt;
		workingRepo = newRepo;
		workingRepoBranch = newRepoBranch;
		evt = new WorkSpaceChangeEvent(new EventObject(this), newRepo,
				newRepoBranch);
		notifyChange(evt);
	}
	
	/**
	 * Attach a listener to the listener list
	 * @param w a listener
	 */
	public void addWorkSpaceChangeEventListener(WorkSpaceChangeEventListener w){
		if(w != null) changeListeners.add(w);
	}
	
	/**
	 * Remove the given listener from the listener list
	 * @param w the listener being removed
	 */
	public void removeWorkSpaceChangeEventListener(WorkSpaceChangeEventListener w){
		changeListeners.remove(w);
	}
	

	/**
	 * Notify the listeners that the workspace is changed 
	 * NOTE: if any listener is null, it will cause a runtime error
	 */
	private void notifyChange(WorkSpaceChangeEvent evt){
		for(WorkSpaceChangeEventListener w: changeListeners){
			if(w != null){
				w.workSpaceChanged(evt);
			}
		}
	}
	
}
