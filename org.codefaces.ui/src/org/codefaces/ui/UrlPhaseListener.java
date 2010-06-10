package org.codefaces.ui;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.core.services.RepoService;
import org.codefaces.httpclient.RepoResponseException;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;

public class UrlPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 6817994765856272911L;

	@Override
	public void afterPhase(PhaseEvent event) {
		String repoUrl = (String) RWT.getRequest().getParameter("repo");
		String branchName = (String) RWT.getRequest().getParameter("branch");

		if (repoUrl != null) {
			try {
				RepoService repoService = CodeFacesUIActivator.getDefault()
						.getRepoService();
				Repo repo = repoService.createRepo(repoUrl);
				RepoBranch repoBranch = repo.getBranchByName(branchName);

				if (repoBranch == null) {
					repoBranch = repoService.getDefaultBranch(repo);
				}

				Workspace.getCurrent().update(repoBranch);

			} catch (RepoResponseException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// do nothing
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.PREPARE_UI_ROOT;
	}

}
