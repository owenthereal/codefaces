package org.codefaces.core.services.svn;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.svn.operations.SVNConstants;
import org.codefaces.core.svn.operations.SVNFetchBranchesOperationHandler;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNResourceKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SvnFetchBranchesQueryTest {
	
	private static String TEST_URL_WITHOUT_BRANCHES = "http://code.djangoproject.com/svn/django/trunk";
	private static String TEST_URL_WITH_BRANCHES = "http://code.djangoproject.com/svn/django";
	
	class MockSvnFetchBrancheQuery extends SVNFetchBranchesOperationHandler {
		private ISVNClientAdapter testClient;
		
		public void setClient(ISVNClientAdapter testClient){
			this.testClient = testClient;
		}
		
		@Override
		protected ISVNClientAdapter getSvnClient(){
			return testClient == null? new MockSVNClientAdaptor() : testClient;
		}
	}
	
	// Create a mock Repo object for testing
	private Repo createModRepo(String url, String username, String password)
			throws MalformedURLException {
		SVNUrl svnUrl = new SVNUrl(url);
		RepoCredential credential = new RepoCredential(null, username, password);
		return new Repo(svnUrl.toString(), svnUrl.toString(), credential);
	}
	
	private MockSvnFetchBrancheQuery query;
	
	@Before
	public void setUp(){
		query = new MockSvnFetchBrancheQuery();
	}
	
	@Test
	public void directoriesWithoutBranchesFolderShouldOnlyContainsDefaultBranch()
			throws MalformedURLException {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		Repo repo = createModRepo(TEST_URL_WITHOUT_BRANCHES, null, null);
		para.addParameter(SCMQuery.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(null, para);
		
		assertEquals(1, branches.size());
		
		RepoBranch defaultBranch = ((RepoBranch) branches.toArray()[0]);
		assertEquals(SVNConstants.DEFAULT_BRANCH, defaultBranch.getName());
	}
		
	@Test
	public void directoriesInsideBranchesFolderShouldBeReturnedWhenThereIsAFolderCalledBranches()
			throws MalformedURLException{
		
		//In this test, we create a mock SVN client. If a branches directory is queried, it
		//shows up one file and one folder
		final MockSVNDirEntry branchInBranchesDir = new MockSVNDirEntry();
		final MockSVNDirEntry fileInBranchesDir = new MockSVNDirEntry();
		branchInBranchesDir.setPath("branchInBranchesDir");
		fileInBranchesDir.setPath("fileInBranchesDir");
		branchInBranchesDir.setNodeKind(SVNResourceKind.DIR);
		fileInBranchesDir.setNodeKind(SVNResourceKind.FILE);
		branchInBranchesDir.setLastChangedDate(new Date());
		fileInBranchesDir.setLastChangedDate(new Date());
		
		query.setClient(new MockSVNClientAdaptor(){
			@Override
			public ISVNDirEntry[] getList(SVNUrl url, SVNRevision revision,
					boolean recurse) throws SVNClientException {
				return getList(url, revision, null, recurse);
			}
			
			@Override
			public ISVNDirEntry[] getList(SVNUrl url, SVNRevision revision,
					SVNRevision pegRevision, boolean recurse)
					throws SVNClientException {
				if(url.toString().endsWith(SVNConstants.BRANCH_DIRECTORY)){
					return  new  ISVNDirEntry[]{branchInBranchesDir, fileInBranchesDir};
				}
				else{
					return new  ISVNDirEntry[]{};
				}
			}
		});
		
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		Repo repo = createModRepo(TEST_URL_WITH_BRANCHES, null, null);
		para.addParameter(SCMQuery.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(null, para);
		
		//there should be one default branch and one branch in the Branches folder
		assertEquals(2, branches.size());
		RepoBranch[] branchesArray = branches.toArray(new RepoBranch[]{});
		assertTrue(branchesArray[0].getName() == branchInBranchesDir.getPath()
				|| branchesArray[1].getName() == branchInBranchesDir.getPath());
		assertTrue(branchesArray[0].getName() == SVNConstants.DEFAULT_BRANCH
				|| branchesArray[1].getName() == SVNConstants.DEFAULT_BRANCH);
	}
	

}
