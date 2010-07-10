package org.codefaces.core.services.svn;

import static org.junit.Assert.*;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.junit.Test;

public class SvnFetchChildrenQueryTest {

	@Test
	public void test_execute(){
		RepoCredential credential = new RepoCredential(null, "guest", null);
		Repo repo = new Repo("http://subclipse.tigris.org/svn/subclipse/",
				"subclipse.tigris.org/svn/subclipse", credential);
		RepoBranch branch = new RepoBranch(repo, "root_id", "/", true);
		RepoFolderRoot folderRoot = new RepoFolderRoot(branch, "repofolder_root_id", "/");
		
		SvnFetchChildrenQuery query = new SvnFetchChildrenQuery();
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_RESOURCE, folderRoot);
		Collection<RepoResource> children = query.execute(null, para);
		assertEquals(3, children.size());
	}
}
