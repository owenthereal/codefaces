package org.codefaces.ui.events;

import java.util.EnumSet;
import java.util.EventObject;
import static java.util.EnumSet.of;


import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.resources.WorkSpace.Resources;

public class WorkSpaceChangedEvent extends EventObject {
	private static final long serialVersionUID = 5870469991944398567L;

	private EnumSet<Resources> resourcesChanged;
	private Repo newRepo;
	private RepoBranch newRepoBranch;
	
	
	/**
	 * Constructor for working repository changed
	 * @param source the event source object
	 * @param newRepo the new repository
	 */
	public WorkSpaceChangedEvent(Object source, Repo newRepo){
		super(source);
		this.newRepo = newRepo;
		this.newRepoBranch = null;
		this.resourcesChanged = of(Resources.REPO);
	}

	/**
	 * Constructor for working branch changed
	 * @param source the event source object
	 * @param newRepoBranch the new repository branch
	 */
	public WorkSpaceChangedEvent(Object source, RepoBranch newRepoBranch){
		super(source);
		this.newRepo = null;
		this.newRepoBranch = newRepoBranch;
		this.resourcesChanged = of(Resources.BRANCHE);
	}

	
	
	/**
	 * Constructor for both working repository and branches are changed
	 * @param source the event source object
	 * @param newRepo the new repository
	 * @param newRepoBranch the new repository branch
	 */
	public WorkSpaceChangedEvent(Object source, Repo newRepo,
			RepoBranch newRepoBranch) {
		super(source);
		this.newRepo = newRepo;
		this.newRepoBranch = newRepoBranch;
		this.resourcesChanged = of(Resources.REPO, Resources.BRANCHE);
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
