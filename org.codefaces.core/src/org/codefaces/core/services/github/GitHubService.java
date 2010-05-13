package org.codefaces.core.services.github;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
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
import org.codefaces.core.models.RepoFile;
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

	private static final String GET_GITHUB_FILE = "http://github.com/api/v2/json/blob/show";

	private static final String GITHUB_TYPE_BLOB = "blob";
	private static final String GITHUB_TYPE_TREE = "tree";

	private final HttpClient httpClient;

	private Gson gson;

	public GitHubService() {
		httpClient = new HttpClient();
		gson = new Gson();
	}

	/**
	 * @throws RepoConnectionException
	 *             when there is connection error
	 * @throws RepoResponseException
	 *             when unable to parse the server's response
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
	 * @throws RepoConnectionException
	 *             when there is connection error
	 * @throws RepoResponseException
	 *             when unable to parse the server's response
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

	private void executeMethod(HttpMethod method)
			throws RepoConnectionException {
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
	 * @throws RepoConnectionException
	 *             when there is connection error
	 * @throws RepoResponseException
	 *             when unable to parse the server's response
	 * @throws MalformedURLException
	 *             when unable to parse the given url
	 */
	public Repo createGithubRepo(String url) throws RepoResponseException,
			MalformedURLException {
		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			String owner = matcher.group(1);
			String repoName = matcher.group(2);

			// at this stage, set user & passwd to null
			RepoCredential credential = new RepoCredential(owner, null, null);

			return new Repo(url, repoName, credential);
		}

		throw new MalformedURLException("Unable to parse the url: " + url);
	}

	public RepoFile getGitHubFile(Repo repo, RepoFileLite repoFileLite)
			throws RepoResponseException, RepoConnectionException {
		String getGitHubFileUrl = createGetGitHubFileUrl(repo, repoFileLite);

		PostMethod method = null;
		try {
			method = new PostMethod(getGitHubFileUrl);
			executeMethod(method);

			GitHubFileDto gitHubFileDto = gson.fromJson(new String(method
					.getResponseBody()), GitHubFileDto.class);
			GitHubFileDataDto gitHubFileDataDto = gitHubFileDto.getBlob();

			return new RepoFile(gitHubFileDataDto.getSha(), gitHubFileDataDto
					.getName(), gitHubFileDataDto.getData(), gitHubFileDataDto
					.getMime_type(), gitHubFileDataDto.getMode(),
					gitHubFileDataDto.getSize(), repoFileLite.getParent());
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

	public String createGetGitHubFileUrl(Repo repo, RepoFileLite repoFileLite) {
		return GET_GITHUB_FILE + "/" + repo.getCredential().getOwner() + "/"
				+ repo.getName() + "/" + repoFileLite.getParent().getId() + "/"
				+ repoFileLite.getName();
	}

	public Set<RepoBranch> listGitHubBranches(Repo repo)
			throws RepoConnectionException, RepoResponseException {
		Set<RepoBranch> branches = new HashSet<RepoBranch>();
		String gitHubShowBranchesUrl = createGitHubShowBranchesUrl(repo
				.getCredential().getOwner(), repo.getName());
		GitHubBranchesDto gitHubBranches = getGitHubBranches(gitHubShowBranchesUrl);
		for (Entry<String, String> giHubBranchEntry : gitHubBranches
				.getBrances().entrySet()) {
			branches.add(new RepoBranch(giHubBranchEntry.getValue(),
					giHubBranchEntry.getKey(), repo));
		}

		return branches;
	}
}
