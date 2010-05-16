package org.codefaces.ui.commands;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.ui.resources.WorkSpace;
import org.codefaces.ui.resources.WorkSpaceManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.Before;
import org.junit.Test;

public class SwitchRepositoryBranchTest {
	private static final String TEST_GITHUB_DEFAULT_BRANCH = "master";
	
	private static final String TEST_GITHUB_REPO_1 = "http://github.com/schacon/ruby-git";
	private static final String TEST_GITHUB_REPO_NAME_1 = "ruby-git";
	private static final String[] TEST_GITHUB_REPO_BRANCHES_1 = new String[] {
			"master", "test", "internals", "integrate" };
	
	private static final String TEST_GITHUB_REPO_2 = "http://github.com/kklo/empty_testing_project";
	private static final String TEST_GITHUB_REPO_NAME_2 = "empty_testing_project";
	private static final String[] TEST_GITHUB_REPO_BRANCHES_2 = new String[] { "master"};
	
	
	private WorkSpace ws;
	private OpenRepositoryCommandHandler openRepocmd;
	private SwitchRepositoryBranchCommandHandler switchCmd;

	private enum Event {OPEN, SWITCH};
	
	@Before
	public void setUp(){
		ws = WorkSpaceManager.getInstance().getWorkSpace();
		openRepocmd = new OpenRepositoryCommandHandler();
		switchCmd = new SwitchRepositoryBranchCommandHandler();
	}
	
	@Test
	public void test_execution_normal() throws ExecutionException{
		ExecutionEvent e = eventFactory(TEST_GITHUB_REPO_1, Event.OPEN);
		openRepocmd.execute(e);
		assertEquals(TEST_GITHUB_REPO_NAME_1, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
		
		for(int i=0; i< TEST_GITHUB_REPO_BRANCHES_1.length; i++){
			ExecutionEvent evt = eventFactory(TEST_GITHUB_REPO_BRANCHES_1[i],
					Event.SWITCH);
			switchCmd.execute(evt);
			assertEquals(TEST_GITHUB_REPO_NAME_1, ws.getWorkingRepo().getName());
			assertEquals(TEST_GITHUB_REPO_BRANCHES_1[i], ws.getWorkingRepoBranch().getName());
		}
	}
	
	/**
	 * Test when the request branch does not exist
	 */
	@Test
	public void test_execution_no_such_branch() throws ExecutionException{
		ExecutionEvent e = eventFactory(TEST_GITHUB_REPO_2, Event.OPEN);
		openRepocmd.execute(e);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
		
		ExecutionEvent evt = eventFactory(TEST_GITHUB_REPO_BRANCHES_2[0],Event.SWITCH);
		switchCmd.execute(evt);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_REPO_BRANCHES_2[0], ws.getWorkingRepoBranch().getName());
		
		evt = eventFactory(TEST_GITHUB_REPO_BRANCHES_1[2],Event.SWITCH);
		//Should be no change and no exception as update is not executed
		switchCmd.execute(evt);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_REPO_BRANCHES_2[0], ws.getWorkingRepoBranch().getName());
	}

	/**
	 * @param param url for open repository, branch name for switch branch
	 * @param option Event.OPEN or Event.SWITCH
	 * @return a mock event for open repository and switch branch
	 */
	private ExecutionEvent eventFactory(String param, Event option) {
		Map<String, String> parameter = new HashMap<String, String>();
		if (option == Event.OPEN) {
			parameter
					.put(OpenRepositoryCommandHandler.PARAM_REPO_URL_ID, param);
		} else {
			parameter.put(
					SwitchRepositoryBranchCommandHandler.PARAM_NEW_BRANCH_ID,
					param);
		}
		return new ExecutionEvent(null, parameter, null, null);
	}
}
