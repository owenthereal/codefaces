package org.codefaces.core.services.github;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.httpclient.ajax.AjaxClientWidget;
import org.codefaces.httpclient.ajax.JsonGet;
import org.codefaces.httpclient.ajax.JsonResponse;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.codefaces.httpclient.http.RepoResponseException;

import com.google.gson.Gson;

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

	private static final String GITHUB_DEFAULT_BRANCH = "master";

	private final ManagedHttpClient managedClient;

	private Gson gson;

	public GitHubService(ManagedHttpClient managedClient) {
		this.managedClient = managedClient;
		gson = new Gson();
	}

	private String getResponseBody(String url) throws RepoResponseException {
//		return managedClient.getResponseBody(url);
		AjaxClientWidget widget = AjaxClientWidget.getCurrent();
		JsonResponse resp = widget.getClient().execute(new JsonGet(url));
		return resp.getContent();
	}

	/**
	 * @throws RepoConnectionException
	 *             when there is connection error
	 * @throws RepoResponseException
	 *             when unable to parse the server's response
	 */
	protected GitHubBranchesDto getGitHubBranches(String showGitHubBranchesUrl)
			throws RepoResponseException {
		try {
			return gson.fromJson(getResponseBody(showGitHubBranchesUrl),
					GitHubBranchesDto.class);
		} catch (Exception e) {
			throw new RepoResponseException(e.getMessage(), e);
		}
	}

	protected String createGitHubShowBranchesUrl(String owner, String repoName) {
		return SHOW_GITHUB_BRANCHES + "/" + owner + "/" + repoName
				+ "/branches";
	}

	protected String createGitHubListChildrenUrl(Repo repo, RepoResource resource) {
		return SHOW_GITHUB_CHILDREN + "/" + repo.getCredential().getOwner()
				+ "/" + repo.getName() + "/" + resource.getId();
	}

	/**
	 * @throws RepoConnectionException
	 *             when there is connection error
	 * @throws RepoResponseException
	 *             when unable to parse the server's response
	 */
	public Collection<RepoResource> fetchGitHubChildren(RepoResource container)
			throws RepoResponseException {
		RepoFolderRoot root = container.getRoot();
		Repo repo = root.getBranch().getRepo();
		String listChildrenUrl = createGitHubListChildrenUrl(repo, container);

		Set<RepoResource> children = new HashSet<RepoResource>();
		try {
			GitHubResourcesDto retrievedResources = gson.fromJson(
					getResponseBody(listChildrenUrl), GitHubResourcesDto.class);

			for (GitHubResourceDto rscDto : retrievedResources.getResources()) {
				RepoResource child;
				String gitHubRscType = rscDto.getType();
				if (gitHubRscType.equals(GITHUB_TYPE_BLOB)) {
					child = new RepoFile(root, container, rscDto.getSha(),
							rscDto.getName());
				} else if (gitHubRscType.equals(GITHUB_TYPE_TREE)) {
					child = new RepoFolder(root, container, rscDto.getSha(),
							rscDto.getName());
				} else {
					throw new UnsupportedOperationException(
							"Unknown Resource Type: " + gitHubRscType);
				}

				children.add(child);
			}

			return children;

		} catch (Exception e) {
			throw new RepoResponseException(e.getMessage(), e);
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

		throw new MalformedURLException("Invalid repository url: " + url);
	}

	public RepoFileInfo fetchGitHubFileInfo(RepoFile repoFileLite)
			throws RepoResponseException {
		Repo repo = repoFileLite.getRoot().getBranch().getRepo();
		String getGitHubFileUrl = createGetGitHubFileUrl(repo, repoFileLite);

		GitHubFileDto gitHubFileDto;
		try {
			gitHubFileDto = gson.fromJson(getResponseBody(getGitHubFileUrl),
					GitHubFileDto.class);
			GitHubFileDataDto gitHubFileDataDto = gitHubFileDto.getBlob();

			return new RepoFileInfo(repoFileLite, gitHubFileDataDto.getData(),
					gitHubFileDataDto.getMime_type(), gitHubFileDataDto
							.getMode(), gitHubFileDataDto.getSize());
		} catch (Exception e) {
			throw new RepoResponseException(e.getMessage(), e);
		}
	}

	protected String createGetGitHubFileUrl(Repo repo, RepoFile repoFileLite) {
		return GET_GITHUB_FILE + "/" + repo.getCredential().getOwner() + "/"
				+ repo.getName() + "/" + repoFileLite.getParent().getId() + "/"
				+ repoFileLite.getName();
	}

	public Collection<RepoBranch> fetchGitHubBranches(Repo repo)
			throws RepoResponseException {
		Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();
		String gitHubShowBranchesUrl = createGitHubShowBranchesUrl(repo
				.getCredential().getOwner(), repo.getName());
		GitHubBranchesDto gitHubBranches = getGitHubBranches(gitHubShowBranchesUrl);
		for (Entry<String, String> giHubBranchEntry : gitHubBranches
				.getBrances().entrySet()) {
			branches.add(new RepoBranch(repo, giHubBranchEntry.getValue(),
					giHubBranchEntry.getKey()));
		}

		return branches;
	}

	/**
	 * @return the default GitHub branch
	 * @param repo
	 *            the repository
	 * @throws RepoResponseException
	 *             if the default branch is not found
	 */
	public RepoBranch getGitHubDefaultBranch(Repo repo)
			throws RepoResponseException {
		Collection<RepoResource> branches = repo.getChildren();
		for (RepoResource branch : branches) {
			if (branch.getName().equals(GITHUB_DEFAULT_BRANCH)) {
				return (RepoBranch) branch;
			}
		}

		throw new RepoResponseException(GITHUB_DEFAULT_BRANCH
				+ " branch not found.");
	}
}
