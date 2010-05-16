package org.codefaces.ui.commands;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.Before;
import org.junit.Test;

import org.codefaces.ui.resources.WorkSpace;
import org.codefaces.ui.resources.WorkSpaceManager;


public class OpenRepositoryCommandHandlerTest {
	private static final String TEST_GITHUB_DEFAULT_BRANCH = "master";
	
	private static final String TEST_GITHUB_REPO_1 = "http://github.com/schacon/ruby-git";
	private static final String TEST_GITHUB_REPO_NAME_1 = "ruby-git";
	
	private static final String TEST_GITHUB_REPO_2 = "http://github.com/kklo/empty_testing_project";
	private static final String TEST_GITHUB_REPO_NAME_2 = "empty_testing_project";

	private static final String TEST_GITHUB_WRONG_REPO = "http://abc";
	
	private static final String TEST_GITHUB_UGLY_REPO_1 = "http://github.com/kklo/empty_testing_project/";
	private static final String TEST_GITHUB_UGLY_REPO_2 = "  http://github.com/kklo/empty_testing_project";
	private static final String TEST_GITHUB_UGLY_REPO_3 = "http://github.com/kklo/empty_testing_project  ";
	
	private WorkSpace ws;
	private OpenRepositoryCommandHandler cmd;
	
	@Before
	public void setUp(){
		ws = WorkSpaceManager.getInstance().getWorkSpace();
		cmd = new OpenRepositoryCommandHandler();
	}
	
	@Test
	public void test_executation_normal() throws ExecutionException {
		ExecutionEvent e = eventFactory(TEST_GITHUB_REPO_1);
		cmd.execute(e);
		assertEquals(TEST_GITHUB_REPO_NAME_1, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
		
		//Switch to another repo
		e = eventFactory(TEST_GITHUB_REPO_2);
		cmd.execute(e);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
	}
	
	@Test
	public void test_execution_wrong_url(){
		ExecutionEvent evt = eventFactory(TEST_GITHUB_WRONG_REPO);
		try {
			cmd.execute(evt);
		} catch (ExecutionException e) {
			assertTrue(e.getCause() instanceof MalformedURLException); 
		}			
	}
	
	@Test
	public void test_execution_ugly_url() throws ExecutionException{
		ExecutionEvent evt = eventFactory(TEST_GITHUB_UGLY_REPO_1);
		cmd.execute(evt);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());

		evt = eventFactory(TEST_GITHUB_UGLY_REPO_2);
		cmd.execute(evt);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
		
		evt = eventFactory(TEST_GITHUB_UGLY_REPO_3);
		cmd.execute(evt);
		assertEquals(TEST_GITHUB_REPO_NAME_2, ws.getWorkingRepo().getName());
		assertEquals(TEST_GITHUB_DEFAULT_BRANCH, ws.getWorkingRepoBranch().getName());
	}
	
	private ExecutionEvent eventFactory(String url){
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(OpenRepositoryCommandHandler.PARAM_REPO_URL_ID, url);
		return new ExecutionEvent(null, parameter, null, null);
	}
}
