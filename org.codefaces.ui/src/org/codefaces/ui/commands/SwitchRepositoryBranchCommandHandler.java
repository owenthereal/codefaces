package org.codefaces.ui.commands;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.resources.WorkSpace;
import org.codefaces.ui.resources.WorkSpaceManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class SwitchRepositoryBranchCommandHandler extends AbstractHandler
		implements IHandler {
	public static final String ID = "org.codefaces.ui.commands.switchRepositoryBranchCommand";
	public static final String PARAM_NEW_BRANCH_ID = 
		"org.codefaces.ui.commands.parameters.switchRepositoryBranch.newBranch";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String newBranchName = event.getParameter(PARAM_NEW_BRANCH_ID);
		WorkSpace ws = WorkSpaceManager.getInstance().getWorkSpace();
		Repo repo = ws.getWorkingRepo();
		
		RepoBranch newRepoBranch = null;
		//the repository may already changed. so it is possible that we cannot 
		//find the branch
		for (RepoBranch b : repo.getBranches()) {
			if (b.getName().equals(newBranchName)) {
				newRepoBranch = b;
				break;
			}
		}
		
		if(newRepoBranch != null) ws.update(newRepoBranch);
		
		return null;
	}

}
