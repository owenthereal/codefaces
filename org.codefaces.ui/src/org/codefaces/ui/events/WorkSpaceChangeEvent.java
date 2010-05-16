package org.codefaces.ui.events;

import java.util.EnumSet;
import java.util.EventObject;


import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.resources.WorkSpace.Resources;

public class WorkSpaceChangeEvent extends EventObject {
	private static final long serialVersionUID = 5870469991944398567L;

	private final EnumSet<Resources> resourcesChanged;
	private final Repo newRepo;
	private final RepoBranch newRepoBranch;
	

	/**
	 * Constructor for working branch changed
	 * @param source the event source object
	 * @param newRepoBranch the new repository branch
	 */
	public WorkSpaceChangeEvent(Object source, RepoBranch newRepoBranch){
		super(source);
		this.newRepo = null;
		this.newRepoBranch = newRepoBranch;
		this.resourcesChanged = EnumSet.of(Resources.BRANCH);
	}

	
	
	/**
	 * Constructor for both working repository and branches are changed.
	 * Changing Repository must associated with a branch change
	 * 
	 * @param source the event source object
	 * @param newRepo the new repository
	 * @param newRepoBranch the new repository branch
	 */
	public WorkSpaceChangeEvent(Object source, Repo newRepo,
			RepoBranch newRepoBranch) {
		super(source);
		this.newRepo = newRepo;
		this.newRepoBranch = newRepoBranch;
		this.resourcesChanged = EnumSet.of(Resources.REPO, Resources.BRANCH);
	}
	
	
	/**
	 * @return the resources changed
	 */
	public EnumSet<Resources> getResourcesChanged(){
		return resourcesChanged;
	}
	
	/**
	 * @return the new working repository. null if no repository changed
	 */
	public Repo getRepo(){
		return newRepo;
	}
	
	/**
	 * @return the new working branch. null if no branch changed
	 */
	public RepoBranch getRepoBranch(){
		return newRepoBranch;
	}
	

}
