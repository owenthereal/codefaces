package org.codefaces.core.services.github;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class GitHubService {
	private static final String HTTP_WWW_GITHUB_ORG = "http://www.github.org";

	private static final String HTTP_GITHUB_COM = "http://github.com";

	private static final Pattern URL_PATTERN = Pattern.compile("(?:"
			+ Pattern.quote(HTTP_WWW_GITHUB_ORG) + "|"
			+ Pattern.quote(HTTP_GITHUB_COM) + ")/([^/]+)/([^/]+)");

	private static final String SHOW_GITHUB_BRANCHES = "http://github.com/api/v2/json/repos/show";

	private static final String SHOW_GITHUB_CHILDREN = "http://github.com/api/v2/json/tree/show";

	private static final String GITHUB_TYPE_BLOB = "blob";
	private static final String GITHUB_TYPE_TREE = "tree";
	
	private static final String GITHUB_DEFAULT_BRANCH = "master";

	private final HttpClient httpClient;

	private Gson gson;

	public GitHubService() {
		httpClient = new HttpClient();
		gson = new Gson();
	}

	/**
	 * @throws RepoConnectionException when there is connection error
	 * @throws RepoResponseException when unable to parse the server's response
	 */
	public GitHubBranchesDto getGitHubBranches(String showGitHubBranchesUrl)
			throws RepoConnectionException, RepoResponseException {
		PostMethod method = null;
		try {
			method = new PostMethod(showGitHubBranchesUrl);
			executeMethod(method);
			return gson.fromJson(new String(method.getResponseBody()),
					GitHubBranchesDto.class);
		} catch (JsonParseException e) {
			throw new RepoResponseException(e);
		} catch (IOException e) {
			throw new RepoResponseException(e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	public String createGitHubShowBranchesUrl(String owner, String repoName) {
		return SHOW_GITHUB_BRANCHES + "/" + owner + "/" + repoName
				+ "/branches";
	}

	public String createGitHubListChildrenUrl(Repo repo, RepoResource resource) {
		return SHOW_GITHUB_CHILDREN + "/" + repo.getCredential().getOwner()
				+ "/" + repo.getName() + "/" + resource.getId();
	}

	/**
	 * @throws RepoConnectionException when there is connection error
	 * @throws RepoResponseException when unable to parse the server's response
	 */
	public Set<RepoResource> listGitHubChildren(Repo repo,
			RepoContainer container) throws RepoResponseException,
			RepoConnectionException {
		String listChildrenUrl = createGitHubListChildrenUrl(repo, container);

		PostMethod method = null;
		Set<RepoResource> children = new HashSet<RepoResource>();
		try {
			method = new PostMethod(listChildrenUrl);
			executeMethod(method);

			GitHubResourcesDto retrievedResources = gson.fromJson(new String(
					method.getResponseBody()), GitHubResourcesDto.class);

			for (GitHubResourceDto rscDto : retrievedResources.getResources()) {
				RepoResource child;
				String gitHubRscType = rscDto.getType();
				if (gitHubRscType.equals(GITHUB_TYPE_BLOB)) {
					child = new RepoFileLite(rscDto.getSha(), rscDto.getName(),
							container);
				} else if (gitHubRscType.equals(GITHUB_TYPE_TREE)) {
					child = new RepoFolder(rscDto.getSha(), rscDto.getName(),
							container);
				} else {
					throw new UnsupportedOperationException(
							"Unknown Resource Type: " + gitHubRscType);
				}

				children.add(child);
			}
			
			return children;

		} catch (UnsupportedOperationException e) {
			throw new RepoResponseException(e);
		} catch (JsonParseException e) {
			throw new RepoResponseException(e);
		} catch (IOException e) {
			throw new RepoResponseException(e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	private void executeMethod(HttpMethod method) throws RepoConnectionException {
		int status;
		try {
			status = httpClient.executeMethod(method);
		} catch (Exception e) {
			throw new RepoConnectionException(e.getMessage());
		}

		switch (status) {
		case HttpStatus.SC_OK:
			break;
		case HttpStatus.SC_NOT_FOUND:
			throw new RepoConnectionException("HTTP Error: " + status 
					+ ". Request Resource Not Found.");
		case HttpStatus.SC_UNAUTHORIZED:
		case HttpStatus.SC_FORBIDDEN:
			throw new RepoConnectionException("HTTP Error: " + status 
					+ "Unauthorized Request.");
		default:
			throw new RepoConnectionException("HTTP Error: " + status);
		}
	}

	/**
	 * @throws RepoConnectionException when there is connection error
	 * @throws RepoResponseException when unable to parse the server's response
	 * @throws MalformedURLException when unable to parse the given url
	 */
	public Repo createGithubRepo(String url) throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();

			String owner = matcher.group(1);
			String repoName = matcher.group(2);

			// at this stage, set user & passwd to null
			RepoCredential credential = new RepoCredential(owner, null, null);
			Repo repo = new Repo(url, repoName, branches, credential);
			populateGitHubBranches(repo, branches, owner, repoName);

			return repo;
		}
		else{
			throw new MalformedURLException("Unable to parse the url: " + url);
		}
	}

	/**
	 * @throws RepoConnectionException when there is connection error
	 * @throws RepoResponseException when unable to parse the server's response
	 */
	private void populateGitHubBranches(Repo repo, Set<RepoBranch> branches,
			String userName, String repoName) throws RepoConnectionException,
			RepoResponseException {
		String gitHubShowBranchesUrl = createGitHubShowBranchesUrl(userName,
				repoName);
		GitHubBranchesDto gitHubBranches = getGitHubBranches(gitHubShowBranchesUrl);
		for (Entry<String, String> giHubBranchEntry : gitHubBranches
				.getBrances().entrySet()) {
			branches.add(new RepoBranch(giHubBranchEntry.getValue(),
					giHubBranchEntry.getKey(), repo));
		}
	}

	/**
	 * @return the default GitHub branch
	 * @param repo the repository
	 * @throws RepoResponseException if the default branch is not found
	 */
	public RepoBranch getGitHubDefaultBranch(Repo repo) throws RepoResponseException {
		Collection<RepoBranch> branches = repo.getBranches();
		for(RepoBranch branch: branches){
			if(branch.getName().equals(GITHUB_DEFAULT_BRANCH)){
				return branch;
			}
		}
		throw new RepoResponseException(GITHUB_DEFAULT_BRANCH
				+ " branch not found.");
	}
}
