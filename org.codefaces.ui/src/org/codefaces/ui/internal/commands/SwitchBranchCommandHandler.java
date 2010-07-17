package org.codefaces.ui.internal.commands;

import org.apache.commons.lang.ObjectUtils;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;

public class SwitchBranchCommandHandler extends AbstractHandler {
	public static final String ID = "org.codefaces.ui.commands.switchBranchCommand";
	public static final String PARAM_BRANCH_ID = "org.codefaces.ui.commands.parameters.switchBranchCommand.branchId";
	public static final String VARIABLE_BRANCH = "org.codefaces.ui.commands.parameters.switchBranchCommand.branch";

	/**
	 * This command calls WorkSpace to update. However, if the given branch name
	 * cannot be found in the working repository. The repository is probably be
	 * updated by some other commands. Instead of throwing an Exception, this
	 * command job the update request silently.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext context = (IEvaluationContext) event
				.getApplicationContext();
		Object branch = context.getVariable(VARIABLE_BRANCH);
		if (!(branch instanceof RepoBranch)) {
			return null;
		}

		Workspace ws = Workspace.getCurrent();
		Repo repo = ws.getWorkingBranch().getRepo();

		for (RepoBranch b : repo.getBranches()) {
			if (ObjectUtils.equals(branch, b)) {
				ws.update(b);
				break;
			}
		}

		return null;
	}
}
