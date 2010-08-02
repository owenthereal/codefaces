package org.codefaces.core.github.internal.operations;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubFileDTO;
import org.codefaces.core.github.internal.operations.dtos.GitHubFileDataDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.connectors.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchFileInfoHandler implements SCMOperationHandler {
	private static final String GET_GITHUB_FILE = "http://github.com/api/v2/json/blob/show";

	@Override
	public RepoFileInfo execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object file = parameter.getParameter(SCMOperationParameter.REPO_FILE);
		Assert.isTrue(file instanceof RepoFile);

		try {
			RepoFile repoFile = (RepoFile) file;
			String fileName = repoFile.getName();
			RepoFolder folder = (RepoFolder) repoFile.getParent();
			Repo repo = repoFile.getRoot().getRepo();

			String fileUrl = createFetchFileInfoUrl(repo, folder, fileName);

			GitHubFileDataDTO fileDataDto = fetchFileDataDto(
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

	private GitHubFileDataDTO fetchFileDataDto(GitHubConnector connector,
			String getGitHubFileMetadataUrl) {
		GitHubFileDTO gitHubFileDto = GitHubOperationUtil.fromJson(
				connector.getResponseBody(getGitHubFileMetadataUrl),
				GitHubFileDTO.class);

		return gitHubFileDto.getBlob();
	}

}
