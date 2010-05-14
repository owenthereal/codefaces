package org.codefaces.ui.commands;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.codefaces.ui.resources.WorkSpaceManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class OpenRepositoryCommandHandler extends AbstractHandler implements IHandler {
	public static final String ID = "org.codefaces.ui.commands.openRepositoryCommand";
	public static final String PARAM_REPO_URL_ID = 
		"org.codefaces.ui.commands.parameters.openRepository.repoUrl";
	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String repoUrl = event.getParameter(PARAM_REPO_URL_ID);
		try {
			Repo repo = RepoManager.getInstance().getRepoService().getRepo(
					repoUrl);
			RepoBranch branch = RepoManager.getInstance().getRepoService()
					.getDefaultBranch(repo);
			WorkSpaceManager.getInstance().getWorkSpace().update(repo, branch);
			
		} catch (RepoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	

}
