package org.codefaces.ui.commands;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.models.Workspace;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class OpenRepositoryCommandHandler extends AbstractHandler implements
		IHandler {
	public static final String ID = "org.codefaces.ui.commands.openRepositoryCommand";

	public static final String PARAM_REPO_URL_ID = "org.codefaces.ui.commands.parameters.openRepository.repoUrl";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String repoUrl = event.getParameter(PARAM_REPO_URL_ID);
		try {
			Repo repo = RepoManager.getInstance().getRepoService().createRepo(
					repoUrl);
			RepoBranch branch = RepoManager.getInstance().getRepoService()
					.getDefaultBranch(repo);
			Workspace.getCurrent().update(branch);

		} catch (RepoConnectionException e) {
			throw new ExecutionException("Repository Connection Problem", e);
		} catch (RepoResponseException e) {
			throw new ExecutionException("Unknown Repository Responce", e);
		} catch (MalformedURLException e) {
			throw new ExecutionException("Malformed repository URL", e);
		}

		return null;
	}
}
