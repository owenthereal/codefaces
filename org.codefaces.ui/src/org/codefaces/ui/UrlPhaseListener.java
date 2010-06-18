package org.codefaces.ui;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.core.services.RepoService;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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

			} catch (Exception e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when connecting to repository "
								+ repoUrl + " with branch " + branchName, e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
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
