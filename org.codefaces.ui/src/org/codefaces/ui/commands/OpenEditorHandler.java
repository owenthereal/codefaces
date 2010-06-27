package org.codefaces.ui.commands;

import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.editors.NullEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenEditorHandler extends AbstractHandler {
	public static final String ID = "org.codefaces.ui.command.openEditor";

	public static final String VARIABLE_EDITOR_INPUT = "org.codefaces.ui.command.openEditor.editorInput";

	public static final String PARAMETER_EDITOR_ID = "org.codefaces.ui.command.openEditor.editorId";

	public static final String VARIABLE_MATCH_FLAG = "org.codefaces.ui.command.openEditor.matchFlag";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String id = event.getParameter(PARAMETER_EDITOR_ID);
		if (id == null) {
			return null;
		}

		IEditorInput input = new NullEditorInput();
		int matchFlag = IWorkbenchPage.MATCH_ID;

		IEvaluationContext context = (IEvaluationContext) event
				.getApplicationContext();
		if (context != null) {
			input = getEditorInput(context.getVariable(VARIABLE_EDITOR_INPUT));
			matchFlag = getMatchFlag(context.getVariable(VARIABLE_MATCH_FLAG));
		}

		openEditor((IEditorInput) input, (String) id, matchFlag);

		return null;
	}

	private IEditorInput getEditorInput(Object input) {
		if (input instanceof IEditorInput) {
			return (IEditorInput) input;
		}

		return new NullEditorInput();
	}

	private int getMatchFlag(Object flag) {
		if (flag instanceof Integer) {
			return (Integer) flag;
		}

		return IWorkbenchPage.MATCH_ID;
	}

	private void openEditor(IEditorInput input, String id, int matchFlag) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			page.openEditor(input, id, true, matchFlag);
		} catch (PartInitException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when opening editor " + id, e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}

	}
}
