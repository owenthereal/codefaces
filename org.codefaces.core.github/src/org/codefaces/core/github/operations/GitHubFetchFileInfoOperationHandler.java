package org.codefaces.core.github.operations;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.github.connectors.GitHubConnector;
import org.codefaces.core.github.operations.dto.GitHubFileDataDto;
import org.codefaces.core.github.operations.dto.GitHubFileDto;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchFileInfoOperationHandler implements SCMOperationHandler {
	private static final String GET_GITHUB_FILE = "http://github.com/api/v2/json/blob/show";

	@Override
	public RepoFileInfo execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object file = parameter.getParameter(PARA_REPO_FILE);
		Assert.isTrue(file instanceof RepoFile);

		try {
			RepoFile repoFile = (RepoFile) file;
			String fileName = repoFile.getName();
			RepoFolder folder = (RepoFolder) repoFile.getParent();
			Repo repo = repoFile.getRoot().getRepo();

			String fileUrl = createFetchFileInfoUrl(repo, folder, fileName);

			GitHubFileDataDto fileDataDto = fetchFileDataDto(
					(GitHubConnector) connector, fileUrl);

			return new RepoFileInfo(fileDataDto.getData(),
					fileDataDto.getMime_type(), fileDataDto.getMode(),
					fileDataDto.getSize());
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

	protected String createFetchFileInfoUrl(Repo repo, RepoFolder folder,
			String fileName) {
		return GitHubOperationUtil.makeURI(GET_GITHUB_FILE, repo
				.getCredential().getOwner(), repo.getName(), folder.getId(),
				fileName);
	}

	private GitHubFileDataDto fetchFileDataDto(GitHubConnector connector,
			String getGitHubFileMetadataUrl) {
		GitHubFileDto gitHubFileDto = GitHubOperationUtil.fromJson(
				connector.getResponseBody(getGitHubFileMetadataUrl),
				GitHubFileDto.class);

		return gitHubFileDto.getBlob();
	}

}
