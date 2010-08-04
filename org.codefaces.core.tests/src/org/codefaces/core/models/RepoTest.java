package org.codefaces.core.models;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

public class RepoTest {

	private static final String TEST_CORRECT_MOCK_PATH = "/this/is/an/existing/path";
	private static final String TEST_INCORRECT_MOCK_PATH = "/this/is/an/nonexisting/path";
	
	@Test
	public void repoShouldReturnRepoFolderByPathIfItExists(){
		IPath path = Path.fromOSString(TEST_CORRECT_MOCK_PATH);
		RepoFolder folder = (RepoFolder)RepoModelTestingUtils.createMockRepoResourceFromPath("mockKind",
				"mockUrl", "mockUser", "mockPassword", path, false);
		Repo repo = folder.getRoot().getRepo();
		RepoResource resource = repo.getRepoResourceByPath(path);
		assertEquals("path", resource.getName());
		assertTrue(resource instanceof RepoFolder);
		assertEquals(TEST_CORRECT_MOCK_PATH, resource.getFullPath().toString());
	}
	
	@Test
	public void repoShouldReturnRepoFileByPathIfItExists(){
		IPath path = Path.fromOSString(TEST_CORRECT_MOCK_PATH);
		RepoFile file = (RepoFile)RepoModelTestingUtils.createMockRepoResourceFromPath("mockKind",
				"mockUrl", "mockUser", "mockPassword", path, true);
		Repo repo = file.getRoot().getRepo();
		RepoResource resource = repo.getRepoResourceByPath(path);
		assertEquals("path", resource.getName());
		assertTrue(resource instanceof RepoFile);
		assertEquals(TEST_CORRECT_MOCK_PATH, resource.getFullPath().toString());
	}
	
	@Test
	public void repoShouldReturnNullIfThePathDoesNotExist(){
		IPath mockPath = Path.fromOSString(TEST_CORRECT_MOCK_PATH);
		RepoResource mockResource = RepoModelTestingUtils.createMockRepoResourceFromPath("mockKind",
				"mockUrl", "mockUser", "mockPassword", mockPath, false);
		
		IPath testPath = Path.fromOSString(TEST_INCORRECT_MOCK_PATH);
		Repo repo = mockResource.getRoot().getRepo();
		RepoResource resource = repo.getRepoResourceByPath(testPath);
		assertNull(resource);
	}
	
	@Test
	public void repoShouldReturnRepoRootIfRootPathIsGiven(){
		IPath mockPath = Path.fromOSString(TEST_CORRECT_MOCK_PATH);
		RepoResource mockResource = RepoModelTestingUtils.createMockRepoResourceFromPath("mockKind",
				"mockUrl", "mockUser", "mockPassword", mockPath, false);
		
		IPath testPath = Path.ROOT;
		Repo repo = mockResource.getRoot().getRepo();
		RepoResource resource = repo.getRepoResourceByPath(testPath);
		assertTrue(resource instanceof RepoFolderRoot);
	}
	

}
