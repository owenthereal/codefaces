package org.codefaces.core.services.github;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.RepoServiceException;

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

	private final HttpClient httpClient;

	private Gson gson;

	public GitHubService() {
		httpClient = new HttpClient();
		gson = new Gson();
	}

	public GitHubBranchesDto getGitHubBranches(String showGitHubBranchesUrl) {
		PostMethod method = null;
		try {
			method = new PostMethod(showGitHubBranchesUrl);
			executeMethod(method);
			return gson.fromJson(new String(method.getResponseBody()),
					GitHubBranchesDto.class);
		} catch (RepoServiceException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}

		return null;
	}

	public String createGitHubShowBranchesUrl(String owner, String repoName) {
		return SHOW_GITHUB_BRANCHES + "/" + owner + "/" + repoName
				+ "/branches";
	}
	
	public String createGitHubListChildrenUrl(Repo repo, RepoResource resource) {
		return SHOW_GITHUB_CHILDREN + "/" + repo.getCredential().getOwner() + "/" + repo.getName()
				+ "/" + resource.getId();
	}

	private void executeMethod(HttpMethod method) throws RepoServiceException {
		int status;
		try {
			status = httpClient.executeMethod(method);
		} catch (HttpException e) {
			throw new RepoServiceException();
		} catch (IOException e) {
			throw new RepoServiceException();
		}

		switch (status) {
		case HttpStatus.SC_OK:
			break;
		case HttpStatus.SC_UNAUTHORIZED:
		case HttpStatus.SC_FORBIDDEN:
			throw new RepoServiceException();
		default:
			throw new RepoServiceException();
		}
	}

	public Repo createGithubRepo(String url) {
		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();

			String owner = matcher.group(1);
			String repoName = matcher.group(2);
			
			//at this stage, set user & passwd to null
			RepoCredential credential = new RepoCredential(owner, null, null);
			
			Repo repo = new Repo(url, repoName, branches, credential);
			
			populateGitHubBranches(repo, branches, owner, repoName);

			return repo;
		}

		return null;
	}

	private void populateGitHubBranches(Repo repo, Set<RepoBranch> branches,
			String userName, String repoName) {
		String gitHubShowBranchesUrl = createGitHubShowBranchesUrl(userName,
				repoName);
		GitHubBranchesDto gitHubBranches = getGitHubBranches(gitHubShowBranchesUrl);
		for (Entry<String, String> giHubBranchEntry : gitHubBranches
				.getBrances().entrySet()) {
			branches.add(new RepoBranch(giHubBranchEntry.getValue(),
					giHubBranchEntry.getKey(), repo));
		}
	}
}
