package org.codefaces.ui.internal.commands;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class OpenRepoFromURLCommandHandler extends AbstractHandler {
	private static final String ROOT = "/";
	private static final int MAX_PATH_DEPTH = 2;
	public static final String ID = "org.codefaces.ui.internal.commands.openRepoFromURLCommand";
	public static final String VARIABLE_SCM_URL_CONFIGUTATION = "org.codefaces.ui.internal.commands.variables.openRepoFromURLCommand.scmURLConfiguration";

	private Workspace workspace;
	private Repo repo;

	public OpenRepoFromURLCommandHandler(){
		this.workspace = null;
		this.repo = null;
	}
	
	// Constructor for testing purpose
	protected OpenRepoFromURLCommandHandler(Workspace workspace, Repo repo) {
		this.workspace = workspace;
		this.repo = repo;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SCMURLConfiguration configuration = null;
		IEvaluationContext context = (IEvaluationContext) event
				.getApplicationContext();

		if (context != null) {
			Object obj = context.getVariable(VARIABLE_SCM_URL_CONFIGUTATION);
			if (obj instanceof SCMURLConfiguration) {
				configuration = (SCMURLConfiguration) obj;
				RepoFolder baseDirectory = getBaseDirFromConfig(configuration);
				if (baseDirectory != null) {
					getWorkspace().update(baseDirectory);
				}
				else{
					throw new SCMResponseException(
							"Unable to open repository: "
									+ configuration.toString());
				}
			}
		}

		return null;
	}

	/**
	 * @return
	 * 1. RepoRoot if no path or "/" is given
	 * 2. null if wrong path is given
	 * 3. throws UnsupportedOperationException if the path exists the depth limit
	 * 4. the desired resource for otherwise
	 */
	private RepoFolder getBaseDirFromConfig(SCMURLConfiguration configuration) {
		String p = configuration.get(SCMConfigurableElement.BASE_DIRECTORY);
		IPath path = null;
		if(p == null || StringUtils.equals(ROOT, p.trim())){
			path = Path.ROOT;
		}
		else{
			path = Path.fromOSString(p);
		}
		RepoFolder baseDir = null;
		Repo repo = createRepoFromConfig(configuration);
		if (repo == null) {
			return null;
		}
		
		baseDir = retrieveRepoFolderByPath(repo, path);
		return baseDir;
	}

	/**
	 * @return a normal repo if it is not set in the constructor
	 */
	private Repo createRepoFromConfig(SCMURLConfiguration configuration) {
		if(repo != null) return repo;
		
		Repo repoFromConfig = Repo.create(configuration.get(SCMConfigurableElement.SCM_KIND),
				configuration.get(SCMConfigurableElement.REPO_URL),
				configuration.get(SCMConfigurableElement.USER),
				configuration.get(SCMConfigurableElement.PASSWORD));
		return repoFromConfig;
	}

	/**
	 * @return the repo folder if
	 *  1. the path depth is less than MAX_PATH_DEPTH
	 *  2. the resource exists
	 *  3. the resource is a RepoFolder
	 */
	private RepoFolder retrieveRepoFolderByPath(Repo repo, IPath path) {
		String[] segments = path.segments();
		if(segments.length > MAX_PATH_DEPTH){
			throw new UnsupportedOperationException("Path : " + path
					+ " deeper than maximun depth supported (" + MAX_PATH_DEPTH
					+ ").");
		}
		RepoResource resource = repo.getRepoResourceByPath(path);
		if(resource instanceof RepoFolder){
			return (RepoFolder)resource;
		}
		return null;
	}

	/**
	 * @return Workspace in session singleton if it is not set in the constructor
	 */
	private Workspace getWorkspace() {
		return (workspace == null) ? Workspace.getCurrent() : workspace;
	}
}
