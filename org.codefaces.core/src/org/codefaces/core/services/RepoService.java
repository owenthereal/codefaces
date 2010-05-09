package org.codefaces.core.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoItem;
import org.codefaces.core.models.RepoRoot;

public class RepoService {
	public static final String HTTP_WWW_GITHUB_ORG = "http://www.github.org";
	public static final String HTTP_GITHUB_COM = "http://github.com";
	public static final String HTTP_GITHUB_REPO_SHOW = "http://github.com/api/v2/json/repos/show";

	public static final Pattern URL_PATTERN = Pattern.compile("(?:"
			+ Pattern.quote(HTTP_WWW_GITHUB_ORG) + "|"
			+ Pattern.quote(HTTP_GITHUB_COM) + ")/([^/]+)/([^/]+)");

	public RepoRoot getRoot(Repo repo) {
		String githubRepoShowUrl = createGithubRepoShowUrl(repo.getUserName(),
				repo.getRepoName());
		List<RepoItem> repoItems = retrieveGithubRootFileList(githubRepoShowUrl);

		return new RepoRoot(null, repoItems, null, null);
	}

	protected List<RepoItem> retrieveGithubRootFileList(String githubRepoShowUrl) {
		return null;
	}

	public Repo getRepo(String url) {
		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			return new Repo(matcher.group(1), matcher.group(2));
		}
		return null;
	}

	public String createGithubRepoShowUrl(String userName, String repoName) {
		return HTTP_GITHUB_REPO_SHOW + "/" + userName + "/" + repoName
				+ "/branches";
	}
}
