package org.codefaces.core.svn.operations;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.clientadaptor.SVNDirectoryEntry;
import org.codefaces.core.svn.clientadaptor.SVNResourceKind;
import org.codefaces.core.svn.connectors.SVNConnector;
import org.eclipse.core.runtime.Assert;

public class SVNFetchBranchesOperationHandler implements SCMOperationHandler {

	@Override
	public Collection<RepoBranch> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object repoPara = parameter.getParameter(PARA_REPO);
		Assert.isTrue(repoPara instanceof Repo);
		Repo repo = (Repo) repoPara;

		String url = repo.getUrl();
		String username = repo.getCredential().getUser();
		String password = repo.getCredential().getPassword();

		SVNConnector svnConnector = (SVNConnector) connector;

		Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();

		// Test if the branches directory exists
		try {
			String branchUrl = url + "/" + SVNConstants.BRANCH_DIRECTORY;
			SVNDirectoryEntry[] branchEntries = svnConnector.getSvnClient()
					.getDirectoryEntries(branchUrl, username, password);

			for (SVNDirectoryEntry branchEntry : branchEntries) {
				if (branchEntry.getResourceKind() == SVNResourceKind.DIRECTORY){
					RepoBranch branch = new RepoBranch(repo,
							SVNConnector.generateRepoResourceID(branchEntry),
							branchEntry.getName(), false);
					branches.add(branch);
				}
			}
		} catch (Exception e) {
			// Do nothing, there is no "branches" directory
		}

		// Create a default branch
		String defaultBranchId = repo.getId() + ":"
				+ SVNConstants.DEFAULT_BRANCH;
		RepoBranch defaultBranch = new RepoBranch(repo, defaultBranchId,
				SVNConstants.DEFAULT_BRANCH, true);
		branches.add(defaultBranch);

		return branches;
	}

}
