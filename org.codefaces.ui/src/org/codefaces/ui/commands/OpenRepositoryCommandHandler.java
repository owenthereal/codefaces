package org.codefaces.ui.commands;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.codefaces.ui.views.ProjectExplorerViewPart;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenRepositoryCommandHandler extends AbstractHandler implements IHandler {
	public static final String ID = "org.codefaces.ui.commands.openRepositoryCommand";
	public static final String PARAM_EXPLORER_VIEW_ID = 
		"org.codefaces.ui.commands.parameters.openRepository.explorerViewID";
	public static final String PARAM_REPO_URL_ID = 
		"org.codefaces.ui.commands.parameters.openRepository.repoUrl";
	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String explorerViewId = event.getParameter(PARAM_EXPLORER_VIEW_ID);
		String repoUrl = event.getParameter(PARAM_REPO_URL_ID);
		
		try {
			ProjectExplorerViewPart explorerView = 
				(ProjectExplorerViewPart) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().showView(
							explorerViewId);
			explorerView.setRepoModel(getRepoModel(repoUrl));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private RepoResource getRepoModel(String repoUrl)
			throws RepoConnectionException, RepoResponseException,
			MalformedURLException {
		Repo repo = RepoManager.getInstance().getRepoService().getRepo(repoUrl);
		return RepoManager.getInstance().getRepoService().getDefaultRoot(repo);
	}

}
